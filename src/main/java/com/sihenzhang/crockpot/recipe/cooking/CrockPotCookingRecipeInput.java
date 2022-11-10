package com.sihenzhang.crockpot.recipe.cooking;

import com.sihenzhang.crockpot.base.FoodValues;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public record CrockPotCookingRecipeInput(FoodValues mergedFoodValues, List<ItemStack> stacks, int potLevel) {
}
