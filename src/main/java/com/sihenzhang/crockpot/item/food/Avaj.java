package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.item.CrockPotBaseItemFood;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class Avaj extends Item {
    public Avaj() {
        super(new Properties().food(new Food.Builder().hunger(2).saturation(3.6F)
                .effect(() -> new EffectInstance(Effects.SPEED, (32 * 60 + 20) * 20, 2), 1.0F)
                .setAlwaysEdible().build()));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return CrockPotBaseItemFood.FAST_USE_DURATION;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.DRINK;
    }

    @Override
    public SoundEvent getEatSound() {
        return SoundEvents.ENTITY_GENERIC_DRINK;
    }
}
