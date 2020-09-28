package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.item.CrockPotAlwaysEdibleItemFood;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class HotCocoa extends CrockPotAlwaysEdibleItemFood {
    public HotCocoa() {
        super(2, 0.1F, () -> new EffectInstance(Effects.SPEED, 8 * 60 * 20, 1));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.SLOWNESS);
            entityLiving.removePotionEffect(Effects.MINING_FATIGUE);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
