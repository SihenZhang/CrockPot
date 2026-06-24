package com.sihenzhang.crockpot.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class MilkmadeHatItem extends Item {
    public MilkmadeHatItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if (slot == EquipmentSlot.HEAD && owner instanceof Player player && player.getFoodData().needsFood() && !player.getCooldowns().isOnCooldown(itemStack)) {
            itemStack.hurtAndBreak(1, player, EquipmentSlot.HEAD);
            player.getFoodData().eat(1, 0.05F);
            var useCooldown = itemStack.get(DataComponents.USE_COOLDOWN);
            if (useCooldown != null) {
                useCooldown.apply(itemStack, player);
            }
        }
    }
}
