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
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlowerSalad extends CrockPotFood {
    public FlowerSalad() {
        super(CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).setAlwaysEdible().effect(Effects.REGENERATION, 20 * 20).heal(4.0F).cooldown(60));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            double currentX = entityLiving.getPosX();
            double currentY = entityLiving.getPosY();
            double currentZ = entityLiving.getPosZ();
            Random rand = entityLiving.getRNG();
            for (int i = 0; i < 16; i++) {
                double potentialX = currentX + MathHelper.nextDouble(rand, -8.0, 8.0);
                // func_234938_ad_: getActualHeight
                double potentialY = MathHelper.clamp(currentY + MathHelper.nextDouble(rand, -8.0, 8.0), 0.0, worldIn.func_234938_ad_() - 1.0);
                double potentialZ = currentZ + MathHelper.nextDouble(rand, -8.0, 8.0);
                if (entityLiving.isPassenger()) {
                    entityLiving.stopRiding();
                }
                if (entityLiving.attemptTeleport(potentialX, potentialY, potentialZ, true)) {
                    SoundEvent soundevent = entityLiving instanceof FoxEntity ? SoundEvents.ENTITY_FOX_TELEPORT : SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT;
                    worldIn.playSound(null, currentX, currentY, currentZ, soundevent, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entityLiving.playSound(soundevent, 1.0F, 1.0F);
                    break;
                }
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
