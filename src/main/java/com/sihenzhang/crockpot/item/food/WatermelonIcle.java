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
public class WatermelonIcle extends CrockPotBaseItemFood {
    public WatermelonIcle() {
        super(5, 0.4F, () -> new EffectInstance(Effects.SPEED, 3 * 60 * 20), () -> new EffectInstance(Effects.JUMP_BOOST, 3 * 60 * 20), CrockPotBaseItemFood.FAST_USE_DURATION);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.SLOWNESS);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
