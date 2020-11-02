package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.base.CrockPotDamageSource;
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
public class MonsterLasagna extends CrockPotBaseItemFood {
    public MonsterLasagna() {
        super(7, 0.2F, () -> new EffectInstance(Effects.HUNGER, 15 * 20), () -> new EffectInstance(Effects.POISON, 2 * 20));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            entityLiving.attackEntityFrom(CrockPotDamageSource.MONSTER_FOOD, 6.0F);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }
}
