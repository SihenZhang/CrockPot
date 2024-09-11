package com.sihenzhang.crockpot.integration.curios;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosUtils {
    private CuriosUtils() {
    }

    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, Item item) {
        return CuriosApi.getCuriosInventory(livingEntity).map(curiosItemHandler -> curiosItemHandler.isEquipped(item)).orElse(false);
    }

    public static boolean anyMatchInEquippedCurios(LivingEntity livingEntity, TagKey<Item> tag) {
        return CuriosApi.getCuriosInventory(livingEntity).map(curiosItemHandler -> curiosItemHandler.isEquipped(item -> item.is(tag))).orElse(false);
    }
}
