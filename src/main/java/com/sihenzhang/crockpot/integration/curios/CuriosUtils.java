package com.sihenzhang.crockpot.integration.curios;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosUtils {
    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, ItemStack stack) {
        return anyMatchInEquippedCurios(livingEntity, stack.getItem());
    }

    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, Item item) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(livingEntity).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i).getItem() == item) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, Class<? extends Item> itemClass) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(livingEntity).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemClass.isInstance(itemHandler.getStackInSlot(i).getItem())) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }
}
