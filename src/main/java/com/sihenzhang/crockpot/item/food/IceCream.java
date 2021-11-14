package com.sihenzhang.crockpot.item.food;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IceCream extends CrockPotFood {
    public IceCream() {
        super(CrockPotFood.builder().hunger(4).saturation(0.4F).duration(FoodUseDuration.FAST).cooldown(20));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            entityLiving.removeAllEffects();
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }
}
