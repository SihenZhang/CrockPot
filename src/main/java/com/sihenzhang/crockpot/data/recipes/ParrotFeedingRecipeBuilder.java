package com.sihenzhang.crockpot.data.recipes;

import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import com.sihenzhang.crockpot.recipe.RangedItem;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public class ParrotFeedingRecipeBuilder extends AbstractRecipeBuilder {
    private final Ingredient ingredient;
    private final Item result;
    private final int min;
    private final int max;

    public ParrotFeedingRecipeBuilder(Ingredient ingredient, ItemLike result, int min, int max) {
        this.ingredient = ingredient;
        this.result = result.asItem();
        this.min = min;
        this.max = max;
    }

    public static ParrotFeedingRecipeBuilder parrotFeeding(Ingredient ingredient, ItemLike result, int min, int max) {
        return new ParrotFeedingRecipeBuilder(ingredient, result, min, max);
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        output.accept(id, new ParrotFeedingRecipe(ingredient, new RangedItem(result, min, max)), null);
    }
}
