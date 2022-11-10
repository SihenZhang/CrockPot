package com.sihenzhang.crockpot.data.recipes;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Consumer;

public class ExplosionCraftingRecipeBuilder extends AbstractRecipeBuilder {
    private final Item result;
    private final int resultCount;
    private final Ingredient ingredient;
    private float lossRate = 0.0F;
    private boolean onlyBlock = false;

    public ExplosionCraftingRecipeBuilder(ItemLike result, int resultCount, Ingredient ingredient) {
        this.result = result.asItem();
        this.resultCount = resultCount;
        this.ingredient = ingredient;
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, int resultCount, Ingredient ingredient) {
        return new ExplosionCraftingRecipeBuilder(result, resultCount, ingredient);
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, Ingredient ingredient) {
        return explosionCrafting(result, 1, ingredient);
    }

    public ExplosionCraftingRecipeBuilder lossRate(float lossRate) {
        this.lossRate = Mth.clamp(lossRate, 0.0F, 1.0F);
        return this;
    }

    public ExplosionCraftingRecipeBuilder onlyBlock() {
        onlyBlock = true;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, ingredient, result, resultCount, lossRate, onlyBlock));
    }

    public static class Result extends AbstractFinishedRecipe {
        private final Ingredient ingredient;
        private final Item result;
        private final int resultCount;
        private final float lossRate;
        private final boolean onlyBlock;

        public Result(ResourceLocation id, Ingredient ingredient, Item result, int resultCount, float lossRate, boolean onlyBlock) {
            super(id);
            this.ingredient = ingredient;
            this.result = result;
            this.resultCount = resultCount;
            this.lossRate = lossRate;
            this.onlyBlock = onlyBlock;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            var resultKey = ForgeRegistries.ITEMS.getKey(result).toString();
            if (resultCount > 1) {
                var resultObject = new JsonObject();
                resultObject.addProperty("item", resultKey);
                resultObject.addProperty("count", resultCount);
                pJson.add("result", resultObject);
            } else {
                pJson.addProperty("result", resultKey);
            }
            if (lossRate > 0.0F) {
                pJson.addProperty("lossrate", Math.min(lossRate, 1.0F));
            }
            if (onlyBlock) {
                pJson.addProperty("onlyblock", true);
            }
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_SERIALIZER.get();
        }
    }
}
