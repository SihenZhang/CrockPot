package com.sihenzhang.crockpot.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FlowerSalad extends CrockPotBaseItemFood {
    public FlowerSalad() {
        super(4, 1.2F, () -> new EffectInstance(Effects.INSTANT_HEALTH, 1), 24);
    }

    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            double currentXPos = entityLiving.getPosX();
            double currentYPos = entityLiving.getPosY();
            double currentZPos = entityLiving.getPosZ();
            for (int i = 0; i < 16; ++i) {
                double potentialXPos = currentXPos + (entityLiving.getRNG().nextDouble() - 0.5D) * 16.0D;
                double potentialYPos = MathHelper.clamp(currentYPos + (double) (entityLiving.getRNG().nextInt(16) - 8), 0.0D, (double) (worldIn.getActualHeight() - 1));
                double potentialZPos = currentZPos + (entityLiving.getRNG().nextDouble() - 0.5D) * 16.0D;
                if (entityLiving.isPassenger()) {
                    entityLiving.stopRiding();
                }
                if (entityLiving.attemptTeleport(potentialXPos, potentialYPos, potentialZPos, true)) {
                    worldIn.playSound((PlayerEntity) null, currentXPos, currentYPos, currentZPos, SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, SoundCategory.PLAYERS, 1.0F, 1.0F);
                    entityLiving.playSound(SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT, 1.0F, 1.0F);
                    break;
                }
            }
            if (entityLiving instanceof PlayerEntity) {
                ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, 20);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
