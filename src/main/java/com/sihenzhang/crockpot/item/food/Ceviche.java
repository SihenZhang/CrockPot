package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class Ceviche extends Item {
    public Ceviche() {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(7).saturation(0.7F)
                .effect(() -> new EffectInstance(Effects.SPEED, 20 * 20, 2), 1.0F)
                .effect(() -> new EffectInstance(Effects.RESISTANCE, 20 * 20, 1), 1.0F)
                .effect(() -> new EffectInstance(Effects.ABSORPTION, 20 * 20, 2), 1.0F)
                .setAlwaysEdible().build()));
    }
}
