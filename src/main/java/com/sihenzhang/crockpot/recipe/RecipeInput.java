package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.base.IngredientSum;
import net.minecraft.item.ItemStack;

import java.util.List;

public final class RecipeInput {
    public final IngredientSum ingredients;
    public final List<ItemStack> stacks;
    public final int potLevel;

    public RecipeInput(IngredientSum ingredients, List<ItemStack> stacks, int potLevel) {
        this.ingredients = ingredients;
        this.stacks = stacks;
        this.potLevel = potLevel;
    }
}
