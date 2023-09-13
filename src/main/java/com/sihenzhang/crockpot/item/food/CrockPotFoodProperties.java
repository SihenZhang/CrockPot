package com.sihenzhang.crockpot.item.food;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CrockPotFoodProperties {
    private final FoodProperties foodProperties;
    final Item.Properties itemProperties;
    private final int duration;
    private final boolean isDrink;
    private final SoundEvent sound;
    private final int cooldown;
    private final float heal;
    private final Pair<ResourceKey<DamageType>, Float> damage;
    private final List<MobEffect> removedEffects;
    private final List<Component> tooltips;
    private final boolean hideEffects;
    private final List<Component> effectTooltips;

    public CrockPotFoodProperties(Builder builder) {
        foodProperties = builder.foodBuilder.build();
        itemProperties = builder.properties.food(foodProperties);
        duration = builder.duration;
        isDrink = builder.isDrink;
        sound = builder.sound;
        cooldown = builder.cooldown;
        heal = builder.heal;
        damage = builder.damage;
        removedEffects = List.copyOf(builder.removedEffects);
        tooltips = List.copyOf(builder.tooltips);
        hideEffects = builder.hideEffects;
        effectTooltips = List.copyOf(builder.effectTooltips);
    }

    public static Builder builder() {
        return new Builder();
    }

    public static Builder builder(int nutrition, float saturationModifier) {
        return new Builder(nutrition, saturationModifier);
    }

    public int getUseDuration() {
        return duration;
    }

    public UseAnim getUseAnimation() {
        return isDrink ? UseAnim.DRINK : UseAnim.EAT;
    }

    public SoundEvent getSound() {
        return sound == null ? (isDrink ? SoundEvents.GENERIC_DRINK : SoundEvents.GENERIC_EAT) : sound;
    }

    public void hurt(Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && damage != null) {
            var damageTypeKey = damage.getFirst();
            // Should hurt even if the damage is 0, just like hit by a thrown egg
            level.registryAccess().registry(Registries.DAMAGE_TYPE).flatMap(reg -> reg.getHolder(damageTypeKey)).ifPresent(damageType -> livingEntity.hurt(new DamageSource(damageType), damage.getSecond()));
        }
    }

    public void heal(Level level, LivingEntity livingEntity) {
        if (!level.isClientSide && heal > 0.0F) {
            livingEntity.heal(heal);
        }
    }

    public void removeEffects(Level level, LivingEntity livingEntity) {
        if (!level.isClientSide) {
            removedEffects.forEach(livingEntity::removeEffect);
        }
    }

    public void addCooldown(Item item, Player player) {
        player.getCooldowns().addCooldown(item, cooldown);
    }

    public List<Component> getTooltips() {
        return tooltips;
    }

    public List<Component> getEffectTooltips(Item item, Player player) {
        if (player == null) {
            return List.of();
        }
        var builder = ImmutableList.<Component>builder();
        player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> {
            if (!CrockPotConfigs.SHOW_FOOD_EFFECTS_TOOLTIP.get() || hideEffects) {
                return;
            }
            if (!foodCounter.hasEaten(item)) {
                builder.add(I18nUtils.createTooltipComponent("effect.not_eat").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            } else {
                var effects = foodProperties.getEffects();
                if (effects.isEmpty() && effectTooltips.isEmpty() && removedEffects.isEmpty() && heal <= 0.0F && (damage == null || damage.getSecond() <= 0.0F)) {
                    builder.add(I18nUtils.createTooltipComponent("effect.no_effect").withStyle(ChatFormatting.DARK_GRAY));
                    return;
                }
                effects.forEach(p -> {
                    var effect = p.getFirst();
                    var tooltip = Component.translatable(effect.getDescriptionId());
                    if (effect.getAmplifier() > 0) {
                        tooltip = Component.translatable("potion.withAmplifier", tooltip, Component.translatable("potion.potency." + effect.getAmplifier()));
                    }
                    if (effect.getDuration() > 20) {
                        tooltip = Component.translatable("potion.withDuration", tooltip, MobEffectUtil.formatDuration(effect, 1.0F));
                    }
                    var probability = p.getSecond();
                    if (probability < 1.0F) {
                        tooltip = I18nUtils.createTooltipComponent("effect.with_probability", StringUtils.format(probability, "0.##%"), tooltip);
                    }
                    builder.add(tooltip.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
                });
                if (!effectTooltips.isEmpty() || !removedEffects.isEmpty() || heal > 0.0F || (damage != null && damage.getSecond() > 0.0F)) {
                    builder.add(Component.empty());
                    builder.add(I18nUtils.createTooltipComponent("effect.when_" + (isDrink ? "drunk" : "eaten")).withStyle(ChatFormatting.DARK_PURPLE));
                }
                effectTooltips.forEach(builder::add);
                removedEffects.forEach(e -> builder.add(I18nUtils.createTooltipComponent("effect.remove", Component.translatable(e.getDescriptionId())).withStyle(ChatFormatting.GOLD)));
                if (heal > 0.0F) {
                    var hearts = heal / 2.0F;
                    builder.add(I18nUtils.createTooltipComponent("effect.heal." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(ChatFormatting.BLUE));
                }
                if (damage != null && damage.getSecond() > 0.0F) {
                    var hearts = damage.getSecond() / 2.0F;
                    builder.add(I18nUtils.createTooltipComponent("effect.damage." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(ChatFormatting.RED));
                }
            }
        });
        return builder.build();
    }

    public static class Builder {
        Item.Properties properties = new Item.Properties();
        FoodProperties.Builder foodBuilder = new FoodProperties.Builder();
        int duration = FoodUseDuration.NORMAL.val;
        boolean isDrink;
        SoundEvent sound;
        int cooldown;
        float heal;
        Pair<ResourceKey<DamageType>, Float> damage;
        final List<MobEffect> removedEffects = new ArrayList<>();
        final List<Component> tooltips = new ArrayList<>();
        boolean hideEffects;
        final List<Component> effectTooltips = new ArrayList<>();

        public Builder() {
        }

        public Builder(int nutrition, float saturationModifier) {
            foodBuilder = foodBuilder.nutrition(nutrition).saturationMod(saturationModifier);
        }

        public Builder nutrition(int nutrition) {
            this.foodBuilder = this.foodBuilder.nutrition(nutrition);
            return this;
        }

        public Builder saturationMod(float saturationModifier) {
            this.foodBuilder = this.foodBuilder.saturationMod(saturationModifier);
            return this;
        }

        public Builder meat() {
            this.foodBuilder = this.foodBuilder.meat();
            return this;
        }

        public Builder alwaysEat() {
            this.foodBuilder = this.foodBuilder.alwaysEat();
            return this;
        }

        public Builder duration(FoodUseDuration duration) {
            this.duration = duration.val;
            return this;
        }

        public Builder effect(Supplier<MobEffectInstance> effect, float probability) {
            this.foodBuilder = this.foodBuilder.effect(effect, probability);
            return this;
        }

        public Builder effect(Supplier<MobEffectInstance> effect) {
            this.foodBuilder = this.foodBuilder.effect(effect, 1.0F);
            return this;
        }

        public Builder effect(MobEffect effect, int duration, int amplifier, float probability) {
            return this.effect(() -> new MobEffectInstance(effect, duration, amplifier), probability);
        }

        public Builder effect(MobEffect effect, int duration, int amplifier) {
            return this.effect(effect, duration, amplifier, 1.0F);
        }

        public Builder effect(MobEffect effect, int duration, float probability) {
            return this.effect(() -> new MobEffectInstance(effect, duration), probability);
        }

        public Builder effect(MobEffect effect, int duration) {
            return this.effect(effect, duration, 1.0F);
        }

        public Builder effect(Supplier<? extends MobEffect> effect, int duration, int amplifier, float probability) {
            return this.effect(() -> new MobEffectInstance(effect.get(), duration, amplifier), probability);
        }

        public Builder effect(Supplier<? extends MobEffect> effect, int duration, int amplifier) {
            return this.effect(effect, duration, amplifier, 1.0F);
        }

        public Builder effect(Supplier<? extends MobEffect> effect, int duration, float probability) {
            return this.effect(() -> new MobEffectInstance(effect.get(), duration), probability);
        }

        public Builder effect(Supplier<? extends MobEffect> effect, int duration) {
            return this.effect(effect, duration, 1.0F);
        }

        public Builder drink() {
            this.isDrink = true;
            return this;
        }

        public Builder sound(SoundEvent sound) {
            this.sound = sound;
            return this;
        }

        public Builder cooldown(int cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        public Builder heal(float heal) {
            this.heal = heal;
            return this;
        }

        public Builder damage(ResourceKey<DamageType> damageSource, float damageAmount) {
            this.damage = Pair.of(damageSource, damageAmount);
            return this;
        }

        public Builder removeEffect(MobEffect effect) {
            this.removedEffects.add(effect);
            return this;
        }

        public Builder tooltip(String key) {
            this.tooltips.add(I18nUtils.createTooltipComponent(key));
            return this;
        }

        public Builder tooltip(String key, ChatFormatting... formats) {
            this.tooltips.add(I18nUtils.createTooltipComponent(key).withStyle(formats));
            return this;
        }

        public Builder hideEffects() {
            this.hideEffects = true;
            return this;
        }

        public Builder effectTooltip(Component tooltip) {
            effectTooltips.add(tooltip);
            return this;
        }

        public Builder effectTooltip(String key, ChatFormatting... formats) {
            return this.effectTooltip(I18nUtils.createTooltipComponent("effect." + key).withStyle(formats));
        }

        public Builder effectTooltip(String key) {
            return this.effectTooltip(I18nUtils.createTooltipComponent("effect." + key));
        }

        public Builder stacksTo(int maxStackSize) {
            this.properties = this.properties.stacksTo(maxStackSize);
            return this;
        }

        public Builder rarity(Rarity rarity) {
            this.properties = this.properties.rarity(rarity);
            return this;
        }

        public Builder craftRemainder(Item craftingRemainingItem) {
            this.properties = this.properties.craftRemainder(craftingRemainingItem);
            return this;
        }

        public CrockPotFoodProperties build() {
            return new CrockPotFoodProperties(this);
        }
    }
}
