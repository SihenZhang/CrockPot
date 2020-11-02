package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.item.CrockPotAlwaysEdibleItemFood;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class IceCream extends CrockPotAlwaysEdibleItemFood {
    public IceCream() {
        super(4, 0.4F, FoodUseDuration.FAST);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.clearActivePotions();
            if (entityLiving instanceof PlayerEntity) {
                ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, 20);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
