package com.sihenzhang.crockpot.item;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class CrockPotSlowItemFood extends CrockPotBaseItemFood {
    public CrockPotSlowItemFood(int hunger, float saturation) {
        super(hunger, saturation);
    }

    public CrockPotSlowItemFood(int hunger, float saturation, Supplier<EffectInstance> effect) {
        super(hunger, saturation, effect);
    }

    public CrockPotSlowItemFood(int hunger, float saturation, Supplier<EffectInstance> effect1, Supplier<EffectInstance> effect2) {
        super(hunger, saturation, effect1, effect2);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return 48;
    }
}
