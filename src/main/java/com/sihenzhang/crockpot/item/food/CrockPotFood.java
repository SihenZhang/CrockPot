package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
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
    private final int cooldown;
    private final float heal;
    private final Pair<DamageSource, Float> damage;
    private final List<MobEffect> removedEffects;
    private final List<Supplier<Component>> tooltips;

    protected CrockPotFood(CrockPotFoodBuilder builder) {
        super(builder.properties.food(builder.foodBuilder.build()));
        this.duration = builder.duration;
        this.isDrink = builder.isDrink;
        this.cooldown = builder.cooldown;
        this.heal = builder.heal;
        this.damage = builder.damage;
        this.removedEffects = builder.removedEffects;
        this.tooltips = builder.tooltips;
    }

    public static CrockPotFoodBuilder builder() {
        return new CrockPotFoodBuilder();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide) {
            if (this.damage != null && this.damage.getValue() > 1E-6F) {
                livingEntity.hurt(this.damage.getKey(), this.damage.getValue());
            }
            if (this.heal > 1E-6F) {
                livingEntity.heal(this.heal);
            }
            this.removedEffects.forEach(livingEntity::removeEffect);
            if (this.cooldown > 0 && livingEntity instanceof Player) {
                ((Player) livingEntity).getCooldowns().addCooldown(this, this.cooldown);
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
        if (this.isDrink) {
            return SoundEvents.GENERIC_DRINK;
        } else {
            return super.getEatingSound();
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        if (!this.tooltips.isEmpty()) {
            this.tooltips.forEach(tip -> tooltipComponents.add(tip.get()));
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
        private int cooldown;
        private float heal;
        private Pair<DamageSource, Float> damage;
        private final List<MobEffect> removedEffects = new ArrayList<>();
        private final List<Supplier<Component>> tooltips = new ArrayList<>();

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

        public CrockPotFoodBuilder drink() {
            this.isDrink = true;
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

        public CrockPotFoodBuilder hide() {
            this.properties = new Properties();
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
