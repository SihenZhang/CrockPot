package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

import java.util.function.Supplier;

public class WetGoop extends Item {
    private static final Supplier<EffectInstance> nauseaEffect = () -> new EffectInstance(Effects.NAUSEA, 5 * 20);
    private static final Supplier<EffectInstance> poisonEffect = () -> new EffectInstance(Effects.POISON, 20);

    public WetGoop() {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(0).saturation(0F).effect(nauseaEffect, 1F).effect(poisonEffect, 1F).build()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 48;
    }
}
