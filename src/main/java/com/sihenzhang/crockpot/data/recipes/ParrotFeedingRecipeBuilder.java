package com.sihenzhang.crockpot.data.recipes;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.RangedItem;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class ParrotFeedingRecipeBuilder extends AbstractRecipeBuilder {
    private final RangedItem result;
    private final Ingredient ingredient;

    public ParrotFeedingRecipeBuilder(ItemLike result, int resultMinimumCount, int resultMaximumCount, Ingredient ingredient) {
        this.result = new RangedItem(result.asItem(), resultMinimumCount, resultMaximumCount);
        this.ingredient = ingredient;
    }

    public static ParrotFeedingRecipeBuilder parrotFeeding(Ingredient ingredient, ItemLike result, int resultMinimumCount, int resultMaximumCount) {
        return new ParrotFeedingRecipeBuilder(result, resultMinimumCount, resultMaximumCount, ingredient);
    }

    public static ParrotFeedingRecipeBuilder parrotFeeding(Ingredient ingredient, ItemLike result, int resultCount) {
        return parrotFeeding(ingredient, result, resultCount, resultCount);
    }

    public static ParrotFeedingRecipeBuilder parrotFeeding(Ingredient ingredient, ItemLike result) {
        return parrotFeeding(ingredient, result, 1);
    }

    @Override
    public Item getResult() {
        return result.item;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, ingredient, result));
    }

    public static class Result extends AbstractFinishedRecipe {
        private final Ingredient ingredient;
        private final RangedItem result;

        public Result(ResourceLocation id, Ingredient ingredient, RangedItem result) {
            super(id);
            this.ingredient = ingredient;
            this.result = result;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            pJson.add("result", result.toJson());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CrockPotRecipes.PARROT_FEEDING_RECIPE_SERIALIZER.get();
        }
    }
}
