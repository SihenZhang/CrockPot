package com.sihenzhang.crockpot.util;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.wrapper.PlayerMainInvWrapper;

public final class ItemUtils {
    public static boolean giveItemToPlayer(Player player, ItemStack stack) {
        if (stack.isEmpty() || player == null) {
            return false;
        }
        IItemHandler inventory = new PlayerMainInvWrapper(player.getInventory());
        ItemStack remainder = ItemHandlerHelper.insertItem(inventory, stack, false);
        return remainder.isEmpty();
    }
}
