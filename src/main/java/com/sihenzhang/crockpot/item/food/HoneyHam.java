package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.item.CrockPotBaseItemFood;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HoneyHam extends CrockPotBaseItemFood {
    public HoneyHam() {
        super(12, 0.8F, () -> new EffectInstance(Effects.REGENERATION, 20 * 20), () -> new EffectInstance(Effects.ABSORPTION, 60 * 20, 1));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.heal(6.0F);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
