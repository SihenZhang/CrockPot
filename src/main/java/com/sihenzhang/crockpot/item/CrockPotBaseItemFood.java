package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;

import java.util.function.Supplier;

public class CrockPotBaseItemFood extends Item {
    public CrockPotBaseItemFood(int hunger, float saturation) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).build()));
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).effect(effect, 1F).build()));
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect1, Supplier<EffectInstance> effect2) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).effect(effect1, 1F).effect(effect2, 1F).build()));
    }
}
