package com.sihenzhang.crockpot.item;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Taffy extends CrockPotBaseItemFood {
    public Taffy() {
        super(3, 0.3F, () -> new EffectInstance(Effects.LUCK, 2 * 60 * 20));
    }

    @Override
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (entityLiving instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)entityLiving;
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayerentity, stack);
            serverplayerentity.addStat(Stats.ITEM_USED.get(this));
        }
        if (!worldIn.isRemote) {
            entityLiving.removePotionEffect(Effects.POISON);
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 12;
    }
}
