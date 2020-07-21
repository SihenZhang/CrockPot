package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class Taffy extends Item {
    private static final Supplier<EffectInstance> luckEffect = () -> new EffectInstance(Effects.LUCK, 2 * 60 * 20);

    public Taffy() {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(3).saturation(0.6F).effect(luckEffect, 1F).build()));
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
