package com.sihenzhang.crockpot.item.food;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
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

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CrockPotFoodItem extends Item {
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

    protected CrockPotFoodItem(CrockPotFoodItemBuilder builder) {
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

    public static CrockPotFoodItemBuilder builder() {
        return new CrockPotFoodItemBuilder();
    }

    public static CrockPotFoodItemBuilder builder(int nutrition, float saturationModifier) {
        return new CrockPotFoodItemBuilder(nutrition, saturationModifier);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            if (damage != null && damage.getSecond() > 0.0F) {
                pLivingEntity.hurt(damage.getFirst(), damage.getSecond());
            }
            if (heal > 0.0F) {
                pLivingEntity.heal(heal);
            }
            removedEffects.forEach(pLivingEntity::removeEffect);
            if (cooldown > 0 && pLivingEntity instanceof Player player) {
                player.getCooldowns().addCooldown(this, cooldown);
            }
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return duration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        if (isDrink) {
            return UseAnim.DRINK;
        } else {
            return super.getUseAnimation(pStack);
        }
    }

    @Override
    public SoundEvent getEatingSound() {
        if (eatingSound != null) {
            return eatingSound;
        }
        if (isDrink) {
            return SoundEvents.GENERIC_DRINK;
        }
        return super.getEatingSound();
    }

    @SuppressWarnings("deprecation")
    public List<Pair<MobEffectInstance, Float>> getEffects() {
        var foodProperties = this.getFoodProperties();
        return foodProperties == null ? List.of() : foodProperties.getEffects().stream().map(p -> Pair.of(p.getFirst(), p.getSecond())).toList();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (!tooltips.isEmpty()) {
            tooltips.forEach(tip -> pTooltipComponents.add(tip.get()));
        }
        if (pLevel != null && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> {
                if (!CrockPotConfigs.SHOW_FOOD_EFFECTS_TOOLTIP.get() || hideEffects) {
                    return;
                }
                if (!foodCounter.hasEaten(this)) {
                    pTooltipComponents.add(I18nUtils.createTooltipComponent("effect.not_eat").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                } else {
                    var effects = this.getEffects();
                    if (effects.isEmpty() && effectTooltips.isEmpty() && removedEffects.isEmpty() && heal <= 0.0F && (damage == null || damage.getSecond() <= 0.0F)) {
                        pTooltipComponents.add(I18nUtils.createTooltipComponent("effect.no_effect").withStyle(ChatFormatting.GRAY));
                        return;
                    }
                    effects.forEach(p -> {
                        var effect = p.getFirst();
                        var tooltip = new TranslatableComponent(effect.getDescriptionId());
                        if (effect.getAmplifier() > 0) {
                            tooltip = new TranslatableComponent("potion.withAmplifier", tooltip, new TranslatableComponent("potion.potency." + effect.getAmplifier()));
                        }
                        if (effect.getDuration() > 20) {
                            tooltip = new TranslatableComponent("potion.withDuration", tooltip, MobEffectUtil.formatDuration(effect, 1.0F));
                        }
                        var probability = p.getSecond();
                        if (probability < 1.0F) {
                            tooltip = I18nUtils.createTooltipComponent("effect.with_probability", StringUtils.format(probability, "0.##%"), tooltip);
                        }
                        pTooltipComponents.add(tooltip.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
                    });
                    if (!effectTooltips.isEmpty() || !removedEffects.isEmpty() || heal > 0.0F || (damage != null && damage.getSecond() > 0.0F)) {
                        pTooltipComponents.add(TextComponent.EMPTY);
                        pTooltipComponents.add(I18nUtils.createTooltipComponent("effect.when_" + (isDrink ? "drunk" : "eaten")).withStyle(ChatFormatting.DARK_PURPLE));
                    }
                    effectTooltips.forEach(tip -> pTooltipComponents.add(tip.get()));
                    removedEffects.forEach(e -> pTooltipComponents.add(I18nUtils.createTooltipComponent("effect.remove", new TranslatableComponent(e.getDescriptionId())).withStyle(ChatFormatting.GOLD)));
                    if (heal > 0.0F) {
                        var hearts = heal / 2.0F;
                        pTooltipComponents.add(I18nUtils.createTooltipComponent("effect.heal." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(ChatFormatting.BLUE));
                    }
                    if (damage != null && damage.getSecond() > 0.0F) {
                        var hearts = damage.getSecond() / 2.0F;
                        pTooltipComponents.add(I18nUtils.createTooltipComponent("effect.damage." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(ChatFormatting.RED));
                    }
                }
            });
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static class CrockPotFoodItemBuilder {
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

        public CrockPotFoodItemBuilder() {
        }

        public CrockPotFoodItemBuilder(int nutrition, float saturationModifier) {
            this.foodBuilder = this.foodBuilder.nutrition(nutrition).saturationMod(saturationModifier);
        }

        public CrockPotFoodItemBuilder nutrition(int nutrition) {
            this.foodBuilder = this.foodBuilder.nutrition(nutrition);
            return this;
        }

        public CrockPotFoodItemBuilder saturationMod(float saturationModifier) {
            this.foodBuilder = this.foodBuilder.saturationMod(saturationModifier);
            return this;
        }

        public CrockPotFoodItemBuilder meat() {
            this.foodBuilder = this.foodBuilder.meat();
            return this;
        }

        public CrockPotFoodItemBuilder alwaysEat() {
            this.foodBuilder = this.foodBuilder.alwaysEat();
            return this;
        }

        public CrockPotFoodItemBuilder duration(FoodUseDuration duration) {
            this.duration = duration.val;
            return this;
        }

        public CrockPotFoodItemBuilder effect(Supplier<MobEffectInstance> effect, float probability) {
            this.foodBuilder = this.foodBuilder.effect(effect, probability);
            return this;
        }

        public CrockPotFoodItemBuilder effect(Supplier<MobEffectInstance> effect) {
            this.foodBuilder = this.foodBuilder.effect(effect, 1.0F);
            return this;
        }

        public CrockPotFoodItemBuilder effect(MobEffect effect, int duration, int amplifier, float probability) {
            return this.effect(() -> new MobEffectInstance(effect, duration, amplifier), probability);
        }

        public CrockPotFoodItemBuilder effect(MobEffect effect, int duration, int amplifier) {
            return this.effect(effect, duration, amplifier, 1.0F);
        }

        public CrockPotFoodItemBuilder effect(MobEffect effect, int duration, float probability) {
            return this.effect(() -> new MobEffectInstance(effect, duration), probability);
        }

        public CrockPotFoodItemBuilder effect(MobEffect effect, int duration) {
            return this.effect(effect, duration, 1.0F);
        }

        public CrockPotFoodItemBuilder effect(Supplier<? extends MobEffect> effect, int duration, int amplifier, float probability) {
            return this.effect(() -> new MobEffectInstance(effect.get(), duration, amplifier), probability);
        }

        public CrockPotFoodItemBuilder effect(Supplier<? extends MobEffect> effect, int duration, int amplifier) {
            return this.effect(effect, duration, amplifier, 1.0F);
        }

        public CrockPotFoodItemBuilder effect(Supplier<? extends MobEffect> effect, int duration, float probability) {
            return this.effect(() -> new MobEffectInstance(effect.get(), duration), probability);
        }

        public CrockPotFoodItemBuilder effect(Supplier<? extends MobEffect> effect, int duration) {
            return this.effect(effect, duration, 1.0F);
        }

        public CrockPotFoodItemBuilder drink() {
            this.isDrink = true;
            return this;
        }

        public CrockPotFoodItemBuilder eatingSound(SoundEvent eatingSound) {
            this.eatingSound = eatingSound;
            return this;
        }

        public CrockPotFoodItemBuilder cooldown(int cooldown) {
            this.cooldown = cooldown;
            return this;
        }

        public CrockPotFoodItemBuilder heal(float heal) {
            this.heal = heal;
            return this;
        }

        public CrockPotFoodItemBuilder damage(DamageSource damageSource, float damageAmount) {
            this.damage = Pair.of(damageSource, damageAmount);
            return this;
        }

        public CrockPotFoodItemBuilder removeEffect(MobEffect effect) {
            this.removedEffects.add(effect);
            return this;
        }

        public CrockPotFoodItemBuilder tooltip(String key) {
            this.tooltips.add(() -> I18nUtils.createTooltipComponent(key));
            return this;
        }

        public CrockPotFoodItemBuilder tooltip(String key, ChatFormatting... formats) {
            this.tooltips.add(() -> I18nUtils.createTooltipComponent(key).withStyle(formats));
            return this;
        }

        public CrockPotFoodItemBuilder hideEffects() {
            this.hideEffects = true;
            return this;
        }

        public CrockPotFoodItemBuilder effectTooltip(Component tooltip) {
            this.effectTooltips.add(() -> tooltip);
            return this;
        }

        public CrockPotFoodItemBuilder effectTooltip(String key, ChatFormatting... formats) {
            return this.effectTooltip(I18nUtils.createTooltipComponent("effect." + key).withStyle(formats));
        }

        public CrockPotFoodItemBuilder effectTooltip(String key) {
            return this.effectTooltip(I18nUtils.createTooltipComponent("effect." + key));
        }

        public CrockPotFoodItemBuilder hide() {
            this.properties = new Properties();
            if (this.maxStackSize != 64) {
                this.properties = this.properties.stacksTo(this.maxStackSize);
            }
            if (this.rarity != Rarity.COMMON) {
                this.properties = this.properties.rarity(this.rarity);
            }
            return this;
        }

        public CrockPotFoodItemBuilder stacksTo(int maxStackSize) {
            this.maxStackSize = maxStackSize;
            this.properties = this.properties.stacksTo(this.maxStackSize);
            return this;
        }

        public CrockPotFoodItemBuilder rarity(Rarity rarity) {
            this.rarity = rarity;
            this.properties = this.properties.rarity(this.rarity);
            return this;
        }

        public CrockPotFoodItem build() {
            return new CrockPotFoodItem(this);
        }
    }
}
