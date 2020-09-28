package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.item.CrockPotAlwaysEdibleItemFood;
import com.sihenzhang.crockpot.item.CrockPotBaseItemFood;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AsparagusSoup extends CrockPotAlwaysEdibleItemFood {
    public AsparagusSoup() {
        super(4, 0.3F, CrockPotBaseItemFood.FAST_USE_DURATION, true);
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.WEAKNESS);
            entityLiving.removePotionEffect(Effects.MINING_FATIGUE);
            entityLiving.removePotionEffect(Effects.BLINDNESS);
            entityLiving.removePotionEffect(Effects.HUNGER);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
