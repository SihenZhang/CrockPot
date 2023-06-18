package com.sihenzhang.crockpot.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class JeiUtils {
    private JeiUtils() {
    }

    public static List<List<ItemStack>> getPagedItemStacks(List<ItemStack> stacks, IFocusGroup focuses, RecipeIngredientRole role, int size) {
        var focusedStacks = getFocusedItemStacks(stacks, focuses, role);
        if (focusedStacks.size() <= size) {
            return focusedStacks.stream().map(List::of).toList();
        } else {
            List<List<ItemStack>> pagedItemStacks = new ArrayList<>();
            for (var i = 0; i < size; i++) {
                List<ItemStack> expandedStacks = new ArrayList<>();
                expandedStacks.add(focusedStacks.get(i));
                pagedItemStacks.add(expandedStacks);
            }
            var pages = focusedStacks.size() / size + (focusedStacks.size() % size == 0 ? 0 : 1);
            for (var i = 1; i < pages; i++) {
                for (var j = 0; j < size; j++) {
                    pagedItemStacks.get(j).add(i * size + j < focusedStacks.size() ? focusedStacks.get(i * size + j) : null);
                }
            }
            return pagedItemStacks;
        }
    }

    private static List<ItemStack> getFocusedItemStacks(List<ItemStack> stacks, IFocusGroup focuses, RecipeIngredientRole role) {
        if (focuses.getFocuses(VanillaTypes.ITEM_STACK, role).findAny().isPresent()) {
            return stacks.stream().filter(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, role).anyMatch(focus -> ItemStack.isSameItem(stack, focus.getTypedValue().getIngredient()))).toList();
        }
        return List.copyOf(stacks);
    }

    public static List<ItemStack> getItemsFromIngredientWithoutEmptyTag(Ingredient ingredient) {
        return Arrays.stream(ingredient.getItems()).filter(stack -> !(stack.is(Blocks.BARRIER.asItem()) && stack.getHoverName().getContents().toString().contains("Empty Tag: "))).toList();
    }
}
