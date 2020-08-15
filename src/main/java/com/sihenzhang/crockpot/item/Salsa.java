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
public class Salsa extends CrockPotBaseItemFood {
    public Salsa(){
        super(6, 3.6F, () -> new EffectInstance(Effects.SATURATION, 7));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.SLOWNESS);
            entityLiving.removePotionEffect(Effects.WEAKNESS);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
