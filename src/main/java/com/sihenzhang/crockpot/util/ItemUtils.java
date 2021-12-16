package com.sihenzhang.crockpot.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.PlayerMainInvWrapper;

public final class ItemUtils {
    public static boolean giveItemToPlayer(PlayerEntity player, ItemStack stack) {
        if (stack.isEmpty() || player == null) {
            return false;
        }
        IItemHandler inventory = new PlayerMainInvWrapper(player.inventory);
        ItemStack remainder = ItemHandlerHelper.insertItem(inventory, stack, false);
        return remainder.isEmpty();
    }
}
