package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.block.CrockPotBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FlowerSaladItem extends CrockPotFoodBlockItem {
    public FlowerSaladItem() {
        super(CrockPotBlocks.FLOWER_SALAD.get(), CrockPotFoodProperties.builder(6, 0.3F)
                .duration(FoodUseDuration.FAST)
                .alwaysEat()
                .effect(MobEffects.REGENERATION, 20 * 20)
                .heal(4.0F)
                .cooldown(60)
                .effectTooltip("flower_salad", ChatFormatting.LIGHT_PURPLE)
                .build()
        );
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            var currentX = pLivingEntity.getX();
            var currentY = pLivingEntity.getY();
            var currentZ = pLivingEntity.getZ();
            var rand = pLivingEntity.getRandom();
            for (var i = 0; i < 16; i++) {
                var potentialX = currentX + Mth.nextDouble(rand, -8.0, 8.0);
                var potentialY = Mth.clamp(currentY + Mth.nextDouble(rand, -8.0, 8.0), 0.0, pLevel.getHeight() - 1.0);
                var potentialZ = currentZ + Mth.nextDouble(rand, -8.0, 8.0);
                if (pLivingEntity.isPassenger()) {
                    pLivingEntity.stopRiding();
                }
                if (pLivingEntity.randomTeleport(potentialX, potentialY, potentialZ, true)) {
                    var soundevent = pLivingEntity instanceof Fox ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    pLevel.playSound(null, currentX, currentY, currentZ, soundevent, SoundSource.PLAYERS, 1.0F, 1.0F);
                    pLivingEntity.playSound(soundevent, 1.0F, 1.0F);
                    break;
                }
            }
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}
