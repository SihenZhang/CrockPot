package com.sihenzhang.crockpot.recipe.pot;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sihenzhang.crockpot.CrockPotConfig;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;

@ParametersAreNonnullByDefault
public final class CrockPotRecipeManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(CrockPotRecipe.class, new CrockPotRecipe.Serializer()).create();
    private static final Logger LOGGER = LogManager.getLogger();
    private List<CrockPotRecipe> recipes = ImmutableList.of();

    private static ExecutorService EXECUTOR;

    public static void initExecutor() {
        if (CrockPotConfig.ASYNC_RECIPE_MATCHING.get()) {
            EXECUTOR = Executors.newFixedThreadPool(CrockPotConfig.ASYNC_RECIPE_MATCHING_POOL_SIZE.get(),
                    new ThreadFactoryBuilder().setNameFormat("CrockPotMatchingWorker-%d").setDaemon(true).build());
        } else {
            EXECUTOR = MoreExecutors.newDirectExecutorService();
        }
    }

    public CrockPotRecipeManager() {
        super(GSON_INSTANCE, "crock_pot");
    }

    public CompletableFuture<CrockPotRecipe> match(CrockPotRecipeInput input) {
        return CompletableFuture.supplyAsync(() -> matchBlocking(input), EXECUTOR);
    }

    private CrockPotRecipe matchBlocking(CrockPotRecipeInput input) {
        Iterator<CrockPotRecipe> itr = recipes.iterator();
        CrockPotRecipe r;

        List<CrockPotRecipe> matched = new ArrayList<>();

        boolean isFirst = true;
        int p = 0;
        while (itr.hasNext()) {
            r = itr.next();
            if (isFirst) {
                if (r.test(input)) {
                    p = r.priority;
                    matched.add(r);
                    isFirst = false;
                }
            } else {
                if (r.priority != p) {
                    break;
                } else {
                    if (r.test(input)) {
                        matched.add(r);
                    }
                }
            }
        }
        if (matched.isEmpty()) {
            return CrockPotRecipe.EMPTY;
        }
        int sum = 0;
        for (CrockPotRecipe e : matched) {
            sum += e.weight;
        }
        int rand = ThreadLocalRandom.current().nextInt(sum) + 1;
        for (CrockPotRecipe e : matched) {
            rand -= e.weight;
            if (rand <= 0) {
                return e;
            }
        }
        return CrockPotRecipe.EMPTY;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        List<CrockPotRecipe> recipes = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            try {
                CrockPotRecipe recipe = GSON_INSTANCE.fromJson(entry.getValue(), CrockPotRecipe.class);
                recipes.add(recipe);
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading crock pot recipe {}", resourceLocation, exception);
            }
        }

        recipes.sort(Comparator.comparingInt((CrockPotRecipe r) -> r.priority).reversed());
        this.recipes = ImmutableList.copyOf(recipes);

        LOGGER.info("Loaded {} crock pot recipes", recipes.size());
    }
}
