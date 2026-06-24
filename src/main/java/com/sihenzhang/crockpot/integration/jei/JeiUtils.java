package com.sihenzhang.crockpot.integration.jei;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;

import java.util.Comparator;
import java.util.List;

public final class JeiUtils {
    private JeiUtils() {
    }

    public static List<ItemStack> getItemsFromIngredientWithoutEmptyTag(Ingredient ingredient) {
        return ingredient.items()
                .map(holder -> holder.value().getDefaultInstance())
                .filter(stack -> !(stack.is(Blocks.BARRIER.asItem()) && stack.getHoverName().getString().contains("Empty Tag: ")))
                .sorted(Comparator.comparing(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem())))
                .toList();
    }
}
