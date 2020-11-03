package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.gson.*;
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

@ParametersAreNonnullByDefault
public final class RecipeManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(Recipe.class, new Recipe.Serializer()).create();
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Random RANDOM = new Random();
    private List<Recipe> recipes = ImmutableList.of();

    private static ExecutorService EXECUTOR;

    public static void initExecutor() {
        if (CrockPotConfig.ASYNC_RECIPE_MATCHING.get()) {
            EXECUTOR = Executors.newFixedThreadPool(CrockPotConfig.ASYNC_RECIPE_MATCHING_POOL_SIZE.get(),
                    new ThreadFactoryBuilder().setNameFormat("CrockPotMatchingWorker-%d").setDaemon(true).build());
        } else {
            EXECUTOR = MoreExecutors.newDirectExecutorService();
        }
    }

    public RecipeManager() {
        super(GSON_INSTANCE, "crock_pot");
    }

    public CompletableFuture<Recipe> match(RecipeInput input) {
        return CompletableFuture.supplyAsync(() -> matchBlocking(input), EXECUTOR);
    }

    private Recipe matchBlocking(RecipeInput input) {
        Iterator<Recipe> itr = recipes.iterator();
        Recipe r;

        List<Recipe> matched = new LinkedList<>();

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
            return Recipe.EMPTY;
        }
        int sum = 0;
        for (Recipe e : matched) {
            sum += e.weight;
        }
        int rand = RANDOM.nextInt(sum) + 1;
        for (Recipe e : matched) {
            rand -= e.weight;
            if (rand <= 0) {
                return e;
            }
        }
        return Recipe.EMPTY;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        List<Recipe> recipes = new LinkedList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            try {
                Recipe recipe = GSON_INSTANCE.fromJson(entry.getValue(), Recipe.class);
                recipes.add(recipe);
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading crock pot recipe {}", resourceLocation, exception);
            }
        }

        recipes.sort(Comparator.comparingInt((Recipe r) -> r.priority).reversed());
        this.recipes = ImmutableList.copyOf(recipes);

        LOGGER.info("Loaded {} crock pot recipes", recipes.size());
    }
}
