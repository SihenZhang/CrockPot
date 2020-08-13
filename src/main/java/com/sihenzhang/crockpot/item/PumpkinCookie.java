package com.sihenzhang.crockpot.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PumpkinCookie extends CrockPotAlwaysEdibleItemFood {
    public PumpkinCookie() {
        super(8, 2.4F, () -> new EffectInstance(Effects.LUCK, 3 * 60 * 20), 24);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.POISON);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
