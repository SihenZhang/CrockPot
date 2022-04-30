package com.sihenzhang.crockpot.integration.curios;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
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

    @SuppressWarnings("deprecation")
    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, ResourceLocation tag) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(livingEntity).map(itemHandler -> {
            for (int i = 0; i < itemHandler.getSlots(); i++) {
                Item item = itemHandler.getStackInSlot(i).getItem();
                if (item.builtInRegistryHolder().tags().anyMatch(t -> t.location().equals(tag))) {
                    return true;
                }
            }
            return false;
        }).orElse(false);
    }
}
