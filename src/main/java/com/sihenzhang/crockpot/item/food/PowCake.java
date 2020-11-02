package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.item.CrockPotAlwaysEdibleItemFood;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PowCake extends CrockPotAlwaysEdibleItemFood {
    public PowCake() {
        super(2, 0.1F);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.attackEntityFrom(CrockPotDamageSource.POW_CAKE, 1.0F);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
