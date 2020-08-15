package com.sihenzhang.crockpot.item;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class Avaj extends Item {
    public Avaj() {
        super(new Properties().food(new Food.Builder().hunger(2).saturation(7.2F).effect(() -> new EffectInstance(Effects.SPEED, 8 * 60 * 20, 2), 1.0F).setAlwaysEdible().build()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 24;
    }
}
