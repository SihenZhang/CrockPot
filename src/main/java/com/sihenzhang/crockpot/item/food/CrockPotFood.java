package com.sihenzhang.crockpot.item.food;

import com.google.common.collect.ImmutableList;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import com.sihenzhang.crockpot.util.MathUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CrockPotFood extends Item {
    private final int duration;
    private final boolean isDrink;
    private final SoundEvent eatingSound;
    private final int cooldown;
    private final float heal;
    private final Pair<DamageSource, Float> damage;
    private final List<MobEffect> removedEffects;
    private final List<Supplier<Component>> tooltips;
    private final boolean hideEffects;
    private final List<Supplier<Component>> effectTooltips;

    protected CrockPotFood(CrockPotFoodBuilder builder) {
        super(builder.properties.food(builder.foodBuilder.build()));
        this.duration = builder.duration;
        this.isDrink = builder.isDrink;
        this.eatingSound = builder.eatingSound;
        this.cooldown = builder.cooldown;
        this.heal = builder.heal;
        this.damage = builder.damage;
        this.removedEffects = builder.removedEffects;
        this.tooltips = builder.tooltips;
        this.hideEffects = builder.hideEffects;
        this.effectTooltips = builder.effectTooltips;
    }

    public static CrockPotFoodBuilder builder() {
        return new CrockPotFoodBuilder();
    }

    public static CrockPotFoodBuilder builder(int nutrition, float saturationModifier) {
        return new CrockPotFoodBuilder(nutrition, saturationModifier);
    }

    @SuppressWarnings("deprecation")
    public List<Pair<MobEffectInstance, Float>> getEffects() {
        FoodProperties foodProperties = this.getFoodProperties();
        return foodProperties == null ? ImmutableList.of() : foodProperties.getEffects().stream().map(p -> Pair.of(p.getFirst(), p.getSecond())).collect(ImmutableList.toImmutableList());
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide) {
            if (damage != null && damage.getValue() > 0.0F) {
                livingEntity.hurt(damage.getKey(), damage.getValue());
            }
            if (heal > 0.0F) {
                livingEntity.heal(heal);
            }
            removedEffects.forEach(livingEntity::removeEffect);
            if (cooldown > 0 && livingEntity instanceof Player player) {
                player.getCooldowns().addCooldown(this, cooldown);
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.duration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        if (this.isDrink) {
            return UseAnim.DRINK;
        } else {
            return super.getUseAnimation(stack);
        }
    }

    @Override
    public SoundEvent getEatingSound() {
        if (this.eatingSound != null) {
            return eatingSound;
        }
        if (this.isDrink) {
            return SoundEvents.GENERIC_DRINK;
        }
        return super.getEatingSound();
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (!this.tooltips.isEmpty()) {
            this.tooltips.forEach(tip -> tooltipComponents.add(tip.get()));
        }
        if (level != null && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> {
                if (!CrockPotConfigs.SHOW_FOOD_EFFECTS_TOOLTIP.get() || hideEffects) {
                    return;
                }
                if (!foodCounter.hasEaten(this)) {
                    tooltipComponents.add(new TranslatableComponent("tooltip.crockpot.effect.not_eat").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                } else {
                    if (this.getEffects().isEmpty() && effectTooltips.isEmpty() && removedEffects.isEmpty() && heal <= 0.0F && (damage == null || damage.getValue() <= 0.0F)) {
                        tooltipComponents.add(new TranslatableComponent("tooltip.crockpot.effect.no_effect").withStyle(ChatFormatting.GRAY));
                        return;
                    }
                    this.getEffects().forEach(p -> {
                        MobEffectInstance effect = p.getKey();
                        MutableComponent tooltip = new TranslatableComponent(effect.getDescriptionId());
                        if (effect.getAmplifier() > 0) {
                            tooltip = new TranslatableComponent("potion.withAmplifier", tooltip, new TranslatableComponent("potion.potency." + effect.getAmplifier()));
                        }
                        if (effect.getDuration() > 20) {
                            tooltip = new TranslatableComponent("potion.withDuration", tooltip, MobEffectUtil.formatDuration(effect, 1.0F));
                        }
                        float probability = p.getValue();
                        if (probability < 1.0F) {
                            tooltip = new TranslatableComponent("tooltip.crockpot.effect.with_probability", StringUtils.format(probability, "0.##%"), tooltip);
                        }
                        tooltipComponents.add(tooltip.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
                    });
                    if (!effectTooltips.isEmpty() || !removedEffects.isEmpty() || heal > 0.0F || (damage != null && damage.getValue() > 0.0F)) {
                        tooltipComponents.add(TextComponent.EMPTY);
                        tooltipComponents.add(new TranslatableComponent("tooltip.crockpot.effect.when_" + (isDrink ? "drunk" : "eaten")).withStyle(ChatFormatting.DARK_PURPLE));
                    }
                    effectTooltips.forEach(tip -> tooltipComponents.add(tip.get()));
                    removedEffects.forEach(e -> tooltipComponents.add(new TranslatableComponent("tooltip.crockpot.effect.remove", new TranslatableComponent(e.getDescriptionId())).withStyle(ChatFormatting.GOLD)));
                    if (heal > 0.0F) {
                        float hearts = heal / 2.0F;
                        tooltipComponents.add(new TranslatableComponent("tooltip.crockpot.effect.heal." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(ChatFormatting.BLUE));
                    }
                    if (damage != null && damage.getValue() > 0.0F) {
                        float hearts = damage.getValue() / 2.0F;
                        tooltipComponents.add(new TranslatableComponent("tooltip.crockpot.effect.damage." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(ChatFormatting.RED));
                    }
                }
            });
        }
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }

    public static class CrockPotFoodBuilder {
        private Properties properties = new Properties().tab(CrockPot.ITEM_GROUP);
        private int maxStackSize = 64;
        private Rarity rarity = Rarity.COMMON;
        private FoodProperties.Builder foodBuilder = new FoodProperties.Builder();
        private int duration = FoodUseDuration.NORMAL.val;
        private boolean isDrink;
        private SoundEvent eatingSound;
        private int cooldown;
        private float heal;
        private Pair<DamageSource, Float> damage;
        private final List<MobEffect> removedEffects = new ArrayList<>();
        private final List<Supplier<Component>> tooltips = new ArrayList<>();
        private boolean hideEffects;
        private final List<Supplier<Component>> effectTooltips = new ArrayList<>();

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

        public CrockPotFoodBuilder damage(DamageSource damageSource, float damageAmount) {
            this.damage = Pair.of(damageSource, damageAmount);
            return this;
        }

        public CrockPotFoodBuilder removeEffect(MobEffect effect) {
            this.removedEffects.add(effect);
            return this;
        }

        public CrockPotFoodBuilder tooltip(String key) {
            this.tooltips.add(() -> new TranslatableComponent("tooltip.crockpot." + key));
            return this;
        }

        public CrockPotFoodBuilder tooltip(String key, ChatFormatting... formats) {
            this.tooltips.add(() -> new TranslatableComponent("tooltip.crockpot." + key).withStyle(formats));
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
            return this.effectTooltip(new TranslatableComponent("tooltip.crockpot.effect." + key).withStyle(formats));
        }

        public CrockPotFoodBuilder effectTooltip(String key) {
            return this.effectTooltip(new TranslatableComponent("tooltip.crockpot.effect." + key));
        }

        public CrockPotFoodBuilder hide() {
            this.properties = new Item.Properties();
            if (this.maxStackSize != 64) {
                this.properties = this.properties.stacksTo(this.maxStackSize);
            }
            if (this.rarity != Rarity.COMMON) {
                this.properties = this.properties.rarity(this.rarity);
            }
            return this;
        }

        public CrockPotFoodBuilder stacksTo(int maxStackSize) {
            this.maxStackSize = maxStackSize;
            this.properties = this.properties.stacksTo(this.maxStackSize);
            return this;
        }

        public CrockPotFoodBuilder rarity(Rarity rarity) {
            this.rarity = rarity;
            this.properties = this.properties.rarity(this.rarity);
            return this;
        }

        public CrockPotFood build() {
            return new CrockPotFood(this);
        }
    }
}
