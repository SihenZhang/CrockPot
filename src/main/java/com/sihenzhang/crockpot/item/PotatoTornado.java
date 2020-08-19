package com.sihenzhang.crockpot.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PotatoTornado extends CrockPotBaseItemFood {
    public PotatoTornado() {
        super(6, 3.6F, 24);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.HUNGER);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
