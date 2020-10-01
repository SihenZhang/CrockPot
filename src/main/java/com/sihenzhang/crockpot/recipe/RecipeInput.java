package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.base.FoodValueSum;
import net.minecraft.item.ItemStack;

import java.util.List;

public final class RecipeInput {
    public final FoodValueSum foodValueSum;
    public final List<ItemStack> stacks;
    public final int potLevel;

    public RecipeInput(FoodValueSum foodValueSum, List<ItemStack> stacks, int potLevel) {
        this.foodValueSum = foodValueSum;
        this.stacks = stacks;
        this.potLevel = potLevel;
    }
}
