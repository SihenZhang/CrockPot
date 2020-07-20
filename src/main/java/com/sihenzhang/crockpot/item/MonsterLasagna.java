package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.function.Supplier;

public class MonsterLasagna extends Item {
    private static final Supplier<EffectInstance> hungerEffect = () -> new EffectInstance(Effects.HUNGER, 15 * 20);
    private static final Supplier<EffectInstance> poisonEffect = () -> new EffectInstance(Effects.POISON, 5 * 20);

    public MonsterLasagna() {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(7).saturation(4.2F).effect(hungerEffect, 1F).effect(poisonEffect, 1F).build()));
    }
}
