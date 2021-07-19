package com.sihenzhang.crockpot.recipe.bartering;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PiglinBarteringRecipeManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(PiglinBarteringRecipe.class, new PiglinBarteringRecipe.Serializer()).create();
    private static final Logger LOGGER = LogManager.getLogger();
    private List<PiglinBarteringRecipe> recipes = ImmutableList.of();
    private final LoadingCache<Item, PiglinBarteringRecipe> cachedRecipes;

    public PiglinBarteringRecipeManager() {
        super(GSON_INSTANCE, "piglin_bartering");
        this.cachedRecipes = CacheBuilder.newBuilder().maximumSize(64).build(new CacheLoader<Item, PiglinBarteringRecipe>() {
            @Override
            public PiglinBarteringRecipe load(Item key) {
                return recipes.stream().filter(r -> r.test(key)).findFirst().orElse(PiglinBarteringRecipe.EMPTY);
            }
        });
    }

    public List<PiglinBarteringRecipe> getRecipes() {
        return recipes;
    }

    public PiglinBarteringRecipe match(ItemStack stack) {
        return stack.isEmpty() ? PiglinBarteringRecipe.EMPTY : this.cachedRecipes.getUnchecked(stack.getItem());
    }

    public PiglinBarteringRecipe match(Item item) {
        return item == null || item == Items.AIR ? PiglinBarteringRecipe.EMPTY : this.cachedRecipes.getUnchecked(item);
    }

    public String serialize() {
        JsonArray recipeList = new JsonArray();
        this.recipes.forEach(recipe -> {
            recipeList.add(GSON_INSTANCE.toJsonTree(recipe).getAsJsonObject());
        });
        return recipeList.toString();
    }

    public void deserialize(String str) {
        JsonArray array = GSON_INSTANCE.fromJson(str, JsonArray.class);
        List<PiglinBarteringRecipe> recipes = new ArrayList<>();
        for (JsonElement e : array) {
            JsonObject o = e.getAsJsonObject();
            PiglinBarteringRecipe recipe = GSON_INSTANCE.fromJson(o, PiglinBarteringRecipe.class);
            recipes.add(recipe);
        }
        this.recipes = recipes;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        List<PiglinBarteringRecipe> recipes = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            try {
                PiglinBarteringRecipe recipe = GSON_INSTANCE.fromJson(entry.getValue(), PiglinBarteringRecipe.class);
                if (!recipe.isEmpty()) {
                    recipes.add(recipe);
                }
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading Special Piglin Bartering recipe {}", resourceLocation, exception);
            }
        }

        this.recipes = ImmutableList.copyOf(recipes);
        this.cachedRecipes.invalidateAll();

        LOGGER.info("Loaded {} Special Piglin Bartering recipes", recipes.size());
    }
}
