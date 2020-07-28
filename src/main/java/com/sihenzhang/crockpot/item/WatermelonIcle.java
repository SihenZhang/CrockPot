package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.function.Supplier;

public class WatermelonIcle extends Item {
    private static final Supplier<EffectInstance> speedEffect = () -> new EffectInstance(Effects.SPEED, (60 + 30) * 20);
    private static final Supplier<EffectInstance> jumpBoostEffect = () -> new EffectInstance(Effects.JUMP_BOOST, (60 + 30) * 20, 1);

    public WatermelonIcle() {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(3).saturation(0.9F).effect(speedEffect, 1F).effect(jumpBoostEffect, 1F).build()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 24;
    }
}
