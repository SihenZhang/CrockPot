package com.sihenzhang.crockpot.integration.curios;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosUtils {
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

    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, ResourceLocation tag) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(livingEntity).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i).getItem().getTags().contains(tag)) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }
}
