package com.sihenzhang.crockpot.recipe.explosion;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExplosionCraftingRecipeManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(ExplosionCraftingRecipe.class, new ExplosionCraftingRecipe.Serializer()).create();
    private static final Logger LOGGER = LogManager.getLogger();
    private List<ExplosionCraftingRecipe> recipes = ImmutableList.of();
    private final LoadingCache<Item, ExplosionCraftingRecipe> cachedNotOnlyBlockRecipes;
    private final LoadingCache<Block, ExplosionCraftingRecipe> cachedBlockRecipes;

    public ExplosionCraftingRecipeManager() {
        super(GSON_INSTANCE, "explosion_crafting");
        this.cachedNotOnlyBlockRecipes = CacheBuilder.newBuilder().maximumSize(64).build(new CacheLoader<Item, ExplosionCraftingRecipe>() {
            @Override
            public ExplosionCraftingRecipe load(Item key) {
                return recipes.stream().filter(r -> !r.isOnlyBlock() && r.test(key)).findFirst().orElse(ExplosionCraftingRecipe.EMPTY);
            }
        });
        this.cachedBlockRecipes = CacheBuilder.newBuilder().maximumSize(128).build(new CacheLoader<Block, ExplosionCraftingRecipe>() {
            @Override
            public ExplosionCraftingRecipe load(Block key) {
                return recipes.stream().filter(r -> r.test(key.asItem())).findFirst().orElse(ExplosionCraftingRecipe.EMPTY);
            }
        });
    }

    public List<ExplosionCraftingRecipe> getRecipes() {
        return this.recipes;
    }

    public ExplosionCraftingRecipe match(ItemStack stack) {
        return stack.isEmpty() ? ExplosionCraftingRecipe.EMPTY : this.cachedNotOnlyBlockRecipes.getUnchecked(stack.getItem());
    }

    public ExplosionCraftingRecipe match(BlockState state) {
        return state.getBlock() == Blocks.AIR ? ExplosionCraftingRecipe.EMPTY : this.cachedBlockRecipes.getUnchecked(state.getBlock());
    }

    public String serialize() {
        JsonArray recipeList = new JsonArray();
        this.recipes.forEach(recipe -> recipeList.add(GSON_INSTANCE.toJsonTree(recipe).getAsJsonObject()));
        return recipeList.toString();
    }

    public void deserialize(String str) {
        JsonArray array = GSON_INSTANCE.fromJson(str, JsonArray.class);
        List<ExplosionCraftingRecipe> recipes = new ArrayList<>();
        for (JsonElement e : array) {
            JsonObject o = e.getAsJsonObject();
            ExplosionCraftingRecipe recipe = GSON_INSTANCE.fromJson(o, ExplosionCraftingRecipe.class);
            recipes.add(recipe);
        }
        this.recipes = recipes;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        List<ExplosionCraftingRecipe> recipes = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            try {
                ExplosionCraftingRecipe recipe = GSON_INSTANCE.fromJson(entry.getValue(), ExplosionCraftingRecipe.class);
                recipes.add(recipe);
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading explosion crafting recipe {}", resourceLocation, exception);
            }
        }

        this.recipes = ImmutableList.copyOf(recipes);

        LOGGER.info("Loaded {} explosion crafting recipes", recipes.size());
    }
}
