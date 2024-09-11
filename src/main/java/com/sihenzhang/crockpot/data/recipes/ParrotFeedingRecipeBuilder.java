package com.sihenzhang.crockpot.data.recipes;

import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import com.sihenzhang.crockpot.recipe.RangedItem;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

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
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id, new ParrotFeedingRecipe(ingredient, result), null);
    }
}
