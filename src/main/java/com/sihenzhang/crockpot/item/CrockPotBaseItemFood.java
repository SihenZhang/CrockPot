package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotBaseItemFood extends Item {
    public static int SUPER_FAST_USE_DURATION = 16;
    public static int FAST_USE_DURATION = 24;
    public static int NORMAL_USE_DURATION = 32;
    public static int SLOW_USE_DURATION = 40;
    public static int SUPER_SLOW_USE_DURATION = 48;

    private final int useDuration;
    private final boolean isDrink;

    public CrockPotBaseItemFood(int hunger, float saturation, int useDuration, boolean isDrink) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).build()));
        this.useDuration = useDuration;
        this.isDrink = isDrink;
    }

    public CrockPotBaseItemFood(int hunger, float saturation, int useDuration) {
        this(hunger, saturation, useDuration, false);
    }

    public CrockPotBaseItemFood(int hunger, float saturation, boolean isDrink) {
        this(hunger, saturation, CrockPotBaseItemFood.NORMAL_USE_DURATION, isDrink);
    }

    public CrockPotBaseItemFood(int hunger, float saturation) {
        this(hunger, saturation, CrockPotBaseItemFood.NORMAL_USE_DURATION, false);
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect, int useDuration, boolean isDrink) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).effect(effect, 1.0F).build()));
        this.useDuration = useDuration;
        this.isDrink = isDrink;
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect, int useDuration) {
        this(hunger, saturation, effect, useDuration, false);
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect, boolean isDrink) {
        this(hunger, saturation, effect, CrockPotBaseItemFood.NORMAL_USE_DURATION, isDrink);
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect) {
        this(hunger, saturation, effect, CrockPotBaseItemFood.NORMAL_USE_DURATION, false);
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect1, Supplier<EffectInstance> effect2, int useDuration, boolean isDrink) {
        super(new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).effect(effect1, 1.0F).effect(effect2, 1.0F).build()));
        this.useDuration = useDuration;
        this.isDrink = isDrink;
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect1, Supplier<EffectInstance> effect2, int useDuration) {
        this(hunger, saturation, effect1, effect2, useDuration, false);
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect1, Supplier<EffectInstance> effect2, boolean isDrink) {
        this(hunger, saturation, effect1, effect2, CrockPotBaseItemFood.NORMAL_USE_DURATION, isDrink);
    }

    public CrockPotBaseItemFood(int hunger, float saturation, Supplier<EffectInstance> effect1, Supplier<EffectInstance> effect2) {
        this(hunger, saturation, effect1, effect2, CrockPotBaseItemFood.NORMAL_USE_DURATION, false);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.useDuration;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (isDrink) {
            return UseAction.DRINK;
        } else {
            return super.getUseAction(stack);
        }
    }

    @Override
    public SoundEvent getEatSound() {
        if (isDrink) {
            return SoundEvents.ENTITY_GENERIC_DRINK;
        } else {
            return super.getEatSound();
        }
    }
}
