package com.sihenzhang.crockpot.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Salsa extends CrockPotBaseItemFood {
    public Salsa(){
        super(7, 4.2F, 24);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.SLOWNESS);
            entityLiving.removePotionEffect(Effects.MINING_FATIGUE);
            entityLiving.removePotionEffect(Effects.BLINDNESS);
            entityLiving.removePotionEffect(Effects.WEAKNESS);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
