package com.sihenzhang.crockpot.integration.curios;

import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import top.theillusivec4.curios.api.CuriosApi;

public final class CuriosUtils {
    private CuriosUtils() {
    }

    public static boolean anyMatchInEquippedCurios(LivingEntity entity, TagKey<Item> tag) {
        return CuriosApi.getCuriosInventory(entity)
                .map(inventory -> inventory.isEquipped(stack -> stack.is(tag)))
                .orElse(false);
    }
}
