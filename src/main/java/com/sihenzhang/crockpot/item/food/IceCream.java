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
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.clearActivePotions();
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
