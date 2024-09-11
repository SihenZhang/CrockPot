package com.sihenzhang.crockpot.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemLore;

import java.util.List;

public final class NbtUtils {
    public static ItemStack setLoreString(ItemStack stack, String string) {
        stack.set(DataComponents.LORE, new ItemLore(List.of(Component.literal(string))));
        return stack;
    }
}
