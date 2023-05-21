package com.sihenzhang.crockpot.util;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.item.ItemStack;

public final class NbtUtils {
    public static ItemStack setLoreString(ItemStack stack, String string) {
        CompoundTag displayTag = new CompoundTag();
        CompoundTag loreTag = new CompoundTag();
        ListTag loreListTag = new ListTag();
        loreListTag.add(StringTag.valueOf("{\"text\":\"" + string + "\"}"));
        loreTag.put("Lore", loreListTag);
        displayTag.put("display", loreTag);
        stack.setTag(displayTag);
        return stack;
    }
}
