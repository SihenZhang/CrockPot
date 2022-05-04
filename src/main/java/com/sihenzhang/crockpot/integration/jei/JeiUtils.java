package com.sihenzhang.crockpot.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class JeiUtils {
    public static List<List<ItemStack>> getPagedItemStacks(List<ItemStack> stacks, IFocusGroup focuses, RecipeIngredientRole role, int size) {
        List<ItemStack> copiedStacks = new ArrayList<>(stacks);
        if (focuses.getFocuses(VanillaTypes.ITEM_STACK, role).findAny().isPresent()) {
            copiedStacks = copiedStacks.stream().filter(stack -> focuses.getFocuses(VanillaTypes.ITEM_STACK, role).anyMatch(focus -> stack.sameItem(focus.getTypedValue().getIngredient()))).toList();
        }
        List<List<ItemStack>> pagedItemStacks = new ArrayList<>();
        if (copiedStacks.size() <= size) {
            copiedStacks.forEach(stack -> pagedItemStacks.add(Collections.singletonList(stack)));
        } else {
            for (int i = 0; i < size; i++) {
                List<ItemStack> expandedStacks = new ArrayList<>();
                expandedStacks.add(copiedStacks.get(i));
                pagedItemStacks.add(expandedStacks);
            }
            int pages = copiedStacks.size() / size + (copiedStacks.size() % size == 0 ? 0 : 1);
            for (int i = 1; i < pages; i++) {
                for (int j = 0; j < size; j++) {
                    pagedItemStacks.get(j).add(i * size + j < copiedStacks.size() ? copiedStacks.get(i * size + j) : null);
                }
            }
        }
        return pagedItemStacks;
    }
}
