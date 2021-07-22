package com.sihenzhang.crockpot.integration.curios;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosUtils {
    public static boolean anyMatchInEquippedCurios(PlayerEntity player, ItemStack stack) {
        return anyMatchInEquippedCurios(player, stack.getItem());
    }

    public static boolean anyMatchInEquippedCurios(PlayerEntity player, Item item) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i).getItem() == item) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public static boolean anyMatchInEquippedCurios(PlayerEntity player, Class<? extends Item> itemClass) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemClass.isInstance(itemHandler.getStackInSlot(i).getItem())) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }
}
