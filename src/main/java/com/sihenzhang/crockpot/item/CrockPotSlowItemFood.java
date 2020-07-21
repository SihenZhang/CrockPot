package com.sihenzhang.crockpot.item;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;

import java.util.function.Supplier;

public class CrockPotSlowItemFood extends CrockPotBaseItemFood {
    public CrockPotSlowItemFood(int hunger, float saturation) {
        super(hunger, saturation);
    }

    public CrockPotSlowItemFood(int hunger, float saturation, Supplier<EffectInstance> effect) {
        super(hunger, saturation, effect);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 48;
    }
}
