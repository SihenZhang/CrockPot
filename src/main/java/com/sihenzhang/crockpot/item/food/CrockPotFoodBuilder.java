package com.sihenzhang.crockpot.item.food;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CrockPotFoodBuilder {
    Item.Properties properties = new Item.Properties();
    FoodProperties.Builder foodBuilder = new FoodProperties.Builder();
    int duration = FoodUseDuration.NORMAL.val;
    boolean isDrink;
    SoundEvent eatingSound;
    int cooldown;
    float heal;
    Pair<ResourceKey<DamageType>, Float> damage;
    final List<MobEffect> removedEffects = new ArrayList<>();
    final List<Supplier<Component>> tooltips = new ArrayList<>();
    boolean hideEffects;
    final List<Supplier<Component>> effectTooltips = new ArrayList<>();

    public CrockPotFoodBuilder() {
    }

    public CrockPotFoodBuilder(int nutrition, float saturationModifier) {
        this.foodBuilder = this.foodBuilder.nutrition(nutrition).saturationMod(saturationModifier);
    }

    public CrockPotFoodBuilder nutrition(int nutrition) {
        this.foodBuilder = this.foodBuilder.nutrition(nutrition);
        return this;
    }

    public CrockPotFoodBuilder saturationMod(float saturationModifier) {
        this.foodBuilder = this.foodBuilder.saturationMod(saturationModifier);
        return this;
    }

    public CrockPotFoodBuilder meat() {
        this.foodBuilder = this.foodBuilder.meat();
        return this;
    }

    public CrockPotFoodBuilder alwaysEat() {
        this.foodBuilder = this.foodBuilder.alwaysEat();
        return this;
    }

    public CrockPotFoodBuilder duration(FoodUseDuration duration) {
        this.duration = duration.val;
        return this;
    }

    public CrockPotFoodBuilder effect(Supplier<MobEffectInstance> effect, float probability) {
        this.foodBuilder = this.foodBuilder.effect(effect, probability);
        return this;
    }

    public CrockPotFoodBuilder effect(Supplier<MobEffectInstance> effect) {
        this.foodBuilder = this.foodBuilder.effect(effect, 1.0F);
        return this;
    }

    public CrockPotFoodBuilder effect(MobEffect effect, int duration, int amplifier, float probability) {
        return this.effect(() -> new MobEffectInstance(effect, duration, amplifier), probability);
    }

    public CrockPotFoodBuilder effect(MobEffect effect, int duration, int amplifier) {
        return this.effect(effect, duration, amplifier, 1.0F);
    }

    public CrockPotFoodBuilder effect(MobEffect effect, int duration, float probability) {
        return this.effect(() -> new MobEffectInstance(effect, duration), probability);
    }

    public CrockPotFoodBuilder effect(MobEffect effect, int duration) {
        return this.effect(effect, duration, 1.0F);
    }

    public CrockPotFoodBuilder effect(Supplier<? extends MobEffect> effect, int duration, int amplifier, float probability) {
        return this.effect(() -> new MobEffectInstance(effect.get(), duration, amplifier), probability);
    }

    public CrockPotFoodBuilder effect(Supplier<? extends MobEffect> effect, int duration, int amplifier) {
        return this.effect(effect, duration, amplifier, 1.0F);
    }

    public CrockPotFoodBuilder effect(Supplier<? extends MobEffect> effect, int duration, float probability) {
        return this.effect(() -> new MobEffectInstance(effect.get(), duration), probability);
    }

    public CrockPotFoodBuilder effect(Supplier<? extends MobEffect> effect, int duration) {
        return this.effect(effect, duration, 1.0F);
    }

    public CrockPotFoodBuilder drink() {
        this.isDrink = true;
        return this;
    }

    public CrockPotFoodBuilder eatingSound(SoundEvent eatingSound) {
        this.eatingSound = eatingSound;
        return this;
    }

    public CrockPotFoodBuilder cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public CrockPotFoodBuilder heal(float heal) {
        this.heal = heal;
        return this;
    }

    public CrockPotFoodBuilder damage(ResourceKey<DamageType> damageSource, float damageAmount) {
        this.damage = Pair.of(damageSource, damageAmount);
        return this;
    }

    public CrockPotFoodBuilder removeEffect(MobEffect effect) {
        this.removedEffects.add(effect);
        return this;
    }

    public CrockPotFoodBuilder tooltip(String key) {
        this.tooltips.add(() -> I18nUtils.createTooltipComponent(key));
        return this;
    }

    public CrockPotFoodBuilder tooltip(String key, ChatFormatting... formats) {
        this.tooltips.add(() -> I18nUtils.createTooltipComponent(key).withStyle(formats));
        return this;
    }

    public CrockPotFoodBuilder hideEffects() {
        this.hideEffects = true;
        return this;
    }

    public CrockPotFoodBuilder effectTooltip(Component tooltip) {
        this.effectTooltips.add(() -> tooltip);
        return this;
    }

    public CrockPotFoodBuilder effectTooltip(String key, ChatFormatting... formats) {
        return this.effectTooltip(I18nUtils.createTooltipComponent("effect." + key).withStyle(formats));
    }

    public CrockPotFoodBuilder effectTooltip(String key) {
        return this.effectTooltip(I18nUtils.createTooltipComponent("effect." + key));
    }

    public CrockPotFoodBuilder stacksTo(int maxStackSize) {
        this.properties = this.properties.stacksTo(maxStackSize);
        return this;
    }

    public CrockPotFoodBuilder rarity(Rarity rarity) {
        this.properties = this.properties.rarity(rarity);
        return this;
    }

    public CrockPotFoodBuilder craftRemainder(Item craftingRemainingItem) {
        this.properties = this.properties.craftRemainder(craftingRemainingItem);
        return this;
    }

    public CrockPotFoodItem build() {
        return new CrockPotFoodItem(this);
    }

    public CrockPotFoodBlockItem build(Block block) {
        return new CrockPotFoodBlockItem(block, this);
    }
}
