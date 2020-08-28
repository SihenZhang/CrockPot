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
public class WatermelonIcle extends CrockPotBaseItemFood {
    public WatermelonIcle() {
        super(3, 0.3F, () -> new EffectInstance(Effects.SPEED, (60 + 30) * 20), () -> new EffectInstance(Effects.JUMP_BOOST, (60 + 30) * 20, 1), 24);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.SLOWNESS);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
