package com.sihenzhang.crockpot.item.food;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlowerSalad extends CrockPotFood {
    public FlowerSalad() {
        super(CrockPotFood.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().effect(Effects.REGENERATION, 20 * 20).heal(4.0F).cooldown(60).effectTooltip("flower_salad", TextFormatting.LIGHT_PURPLE));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            double currentX = entityLiving.getX();
            double currentY = entityLiving.getY();
            double currentZ = entityLiving.getZ();
            Random rand = entityLiving.getRandom();
            for (int i = 0; i < 16; i++) {
                double potentialX = currentX + MathHelper.nextDouble(rand, -8.0, 8.0);
                double potentialY = MathHelper.clamp(currentY + MathHelper.nextDouble(rand, -8.0, 8.0), 0.0, worldIn.getHeight() - 1.0);
                double potentialZ = currentZ + MathHelper.nextDouble(rand, -8.0, 8.0);
                if (entityLiving.isPassenger()) {
                    entityLiving.stopRiding();
                }
                if (entityLiving.randomTeleport(potentialX, potentialY, potentialZ, true)) {
                    SoundEvent soundevent = entityLiving instanceof FoxEntity ? SoundEvents.FOX_TELEPORT : SoundEvents.CHORUS_FRUIT_TELEPORT;
                    worldIn.playSound(null, currentX, currentY, currentZ, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entityLiving.playSound(soundevent, 1.0F, 1.0F);
                    break;
                }
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
}
