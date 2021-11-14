package com.sihenzhang.crockpot.recipe.pot;

import com.sihenzhang.crockpot.base.FoodValues;
import net.minecraft.item.ItemStack;

import java.util.List;

public final class CrockPotRecipeInput {
    public final FoodValues mergedFoodValues;
    public final List<ItemStack> stacks;
    public final int potLevel;

    public CrockPotRecipeInput(FoodValues mergedFoodValues, List<ItemStack> stacks, int potLevel) {
        this.mergedFoodValues = mergedFoodValues;
        this.stacks = stacks;
        this.potLevel = potLevel;
    }
}
