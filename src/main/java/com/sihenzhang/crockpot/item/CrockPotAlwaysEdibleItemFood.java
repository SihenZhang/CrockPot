package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
public class CrockPotAlwaysEdibleItemFood extends Item {
    private final int useDuration;

    public CrockPotAlwaysEdibleItemFood(int hunger, float saturation, int useDuration) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).setAlwaysEdible().build()));
        this.useDuration = useDuration;
    }

    public CrockPotAlwaysEdibleItemFood(int hunger, float saturation) {
        this(hunger, saturation, 32);
    }

    public CrockPotAlwaysEdibleItemFood(int hunger, float saturation, Supplier<EffectInstance> effect, int useDuration) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).effect(effect, 1.0F).setAlwaysEdible().build()));
        this.useDuration = useDuration;
    }

    public CrockPotAlwaysEdibleItemFood(int hunger, float saturation, Supplier<EffectInstance> effect) {
        this(hunger, saturation, effect, 32);
    }

    public CrockPotAlwaysEdibleItemFood(int hunger, float saturation, Supplier<EffectInstance> effect1, Supplier<EffectInstance> effect2, int useDuration) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).effect(effect1, 1.0F).effect(effect2, 1.0F).setAlwaysEdible().build()));
        this.useDuration = useDuration;
    }

    public CrockPotAlwaysEdibleItemFood(int hunger, float saturation, Supplier<EffectInstance> effect1, Supplier<EffectInstance> effect2) {
        this(hunger, saturation, effect1, effect2, 32);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.useDuration;
    }
}
