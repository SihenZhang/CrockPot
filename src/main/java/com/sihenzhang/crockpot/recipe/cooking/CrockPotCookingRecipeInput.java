package com.sihenzhang.crockpot.recipe.cooking;

import com.sihenzhang.crockpot.base.FoodValues;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public final class CrockPotCookingRecipeInput {
    public final FoodValues mergedFoodValues;
    public final List<ItemStack> stacks;
    public final int potLevel;

    public CrockPotCookingRecipeInput(FoodValues mergedFoodValues, List<ItemStack> stacks, int potLevel) {
        this.mergedFoodValues = mergedFoodValues;
        this.stacks = stacks;
        this.potLevel = potLevel;
    }
}
