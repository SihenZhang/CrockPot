package com.sihenzhang.crockpot.integration.curios;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosUtils {
    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, Item item) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(livingEntity).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i).is(item)) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }

    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, TagKey<Item> tag) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(livingEntity).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                if (itemHandler.getStackInSlot(i).is(tag)) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }
}
