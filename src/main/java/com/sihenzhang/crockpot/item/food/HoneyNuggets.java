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
public class HoneyNuggets extends CrockPotBaseItemFood {
    public HoneyNuggets() {
        super(8, 0.7F, () -> new EffectInstance(Effects.REGENERATION, 20 * 20));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.heal(4.0F);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
