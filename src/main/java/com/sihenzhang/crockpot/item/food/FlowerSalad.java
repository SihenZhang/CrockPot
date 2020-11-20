package com.sihenzhang.crockpot.item.food;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlowerSalad extends CrockPotFood {
    public FlowerSalad() {
        super(CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).heal(4.0F).cooldown(20));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            double currentXPos = entityLiving.getPosX();
            double currentYPos = entityLiving.getPosY();
            double currentZPos = entityLiving.getPosZ();
            for (int i = 0; i < 16; ++i) {
                double potentialXPos = currentXPos + (entityLiving.getRNG().nextDouble() - 0.5) * 16.0;
                double potentialYPos = MathHelper.clamp(currentYPos + (double) (entityLiving.getRNG().nextInt(16) - 8), 0.0, (double) (worldIn.getActualHeight() - 1));
                double potentialZPos = currentZPos + (entityLiving.getRNG().nextDouble() - 0.5) * 16.0;
                if (entityLiving.isPassenger()) {
                    entityLiving.stopRiding();
                }
                if (entityLiving.attemptTeleport(potentialXPos, potentialYPos, potentialZPos, true)) {
                    worldIn.playSound(null, currentXPos, currentYPos, currentZPos, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entityLiving.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    break;
                }
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
