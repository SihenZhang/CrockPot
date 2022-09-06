package com.sihenzhang.crockpot.data.recipes;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class ExplosionCraftingRecipeBuilder extends AbstractRecipeBuilder {
    private final Item result;
    private final int resultCount;
    private final Ingredient ingredient;
    private final float lossRate;
    private final boolean onlyBlock;

    public ExplosionCraftingRecipeBuilder(ItemLike result, int resultCount, Ingredient ingredient, float lossRate, boolean onlyBlock) {
        this.result = result.asItem();
        this.resultCount = resultCount;
        this.ingredient = ingredient;
        this.lossRate = lossRate;
        this.onlyBlock = onlyBlock;
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, int resultCount, Ingredient ingredient, float lossRate, boolean onlyBlock) {
        return new ExplosionCraftingRecipeBuilder(result, resultCount, ingredient, lossRate, onlyBlock);
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, int resultCount, Ingredient ingredient, float lossRate) {
        return explosionCrafting(result, resultCount, ingredient, lossRate, false);
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, int resultCount, Ingredient ingredient, boolean onlyBlock) {
        return explosionCrafting(result, resultCount, ingredient, 0.0F, onlyBlock);
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, int resultCount, Ingredient ingredient) {
        return explosionCrafting(result, resultCount, ingredient, 0.0F, false);
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {

    }

    public static class Result extends AbstractFinishedRecipe {
        private final Ingredient ingredient;
        private final Item result;
        private final float lossRate;
        private final boolean onlyBlock;

        public Result(ResourceLocation id, Ingredient ingredient, Item result, float lossRate, boolean onlyBlock) {
            super(id);
            this.ingredient = ingredient;
            this.result = result;
            this.lossRate = lossRate;
            this.onlyBlock = onlyBlock;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            var ingredient = JsonUtils.getAsIngredient(pJson, "ingredient");
        }

        @Override
        public RecipeSerializer<?> getType() {
            return null;
        }
    }
}
