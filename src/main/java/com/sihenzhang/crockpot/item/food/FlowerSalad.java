package com.sihenzhang.crockpot.item.food;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Random;

public class FlowerSalad extends CrockPotFood {
    public FlowerSalad() {
        super(CrockPotFood.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().effect(MobEffects.REGENERATION, 20 * 20).heal(4.0F).cooldown(60));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide) {
            double currentX = livingEntity.getX();
            double currentY = livingEntity.getY();
            double currentZ = livingEntity.getZ();
            Random rand = livingEntity.getRandom();
            for (int i = 0; i < 16; i++) {
                double potentialX = currentX + Mth.nextDouble(rand, -8.0, 8.0);
                double potentialY = Mth.clamp(currentY + Mth.nextDouble(rand, -8.0, 8.0), 0.0, level.getHeight() - 1.0);
                double potentialZ = currentZ + Mth.nextDouble(rand, -8.0, 8.0);
                if (livingEntity.isPassenger()) {
                    livingEntity.stopRiding();
                }
                if (livingEntity.randomTeleport(potentialX, potentialY, potentialZ, true)) {
                    SoundEvent soundevent = livingEntity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    level.playSound(null, currentX, currentY, currentZ, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    livingEntity.playSound(soundevent, 1.0F, 1.0F);
                    break;
                }
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
