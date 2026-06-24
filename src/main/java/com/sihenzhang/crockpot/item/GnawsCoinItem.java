package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.effect.ModEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.Nullable;

public class GnawsCoinItem extends Item {
    public GnawsCoinItem(Properties properties) {
        super(properties);
    }

    @Override
    public void inventoryTick(ItemStack itemStack, ServerLevel level, Entity owner, @Nullable EquipmentSlot slot) {
        if (owner instanceof LivingEntity livingEntity && livingEntity.tickCount % 19 == 0) {
            livingEntity.addEffect(new MobEffectInstance(ModEffects.GNAWS_GIFT, 20, 0, true, false));
        }
    }
}
