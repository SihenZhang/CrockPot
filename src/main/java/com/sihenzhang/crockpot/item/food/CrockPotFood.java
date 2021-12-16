package com.sihenzhang.crockpot.item.food;

import com.google.common.collect.ImmutableList;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import com.sihenzhang.crockpot.util.MathUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.*;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotFood extends Item {
    private final int duration;
    private final boolean isDrink;
    private final int cooldown;
    private final float heal;
    private final Pair<DamageSource, Float> damage;
    private final List<Effect> removedEffects;
    private final List<Supplier<ITextComponent>> tooltips;
    private final boolean hideEffects;
    private final List<Supplier<ITextComponent>> effectTooltips;

    protected CrockPotFood(CrockPotFoodBuilder builder) {
        super(builder.properties.food(builder.foodBuilder.build()));
        this.duration = builder.duration;
        this.isDrink = builder.isDrink;
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

    public List<Pair<EffectInstance, Float>> getEffects() {
        Food foodProperties = this.getFoodProperties();
        return foodProperties == null ? ImmutableList.of() : foodProperties.getEffects().stream().map(p -> Pair.of(p.getFirst(), p.getSecond())).collect(ImmutableList.toImmutableList());
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            if (damage != null && damage.getValue() > 0.0F) {
                entityLiving.hurt(damage.getKey(), damage.getValue());
            }
            if (heal > 0.0F) {
                entityLiving.heal(heal);
            }
            removedEffects.forEach(entityLiving::removeEffect);
            if (cooldown > 0 && entityLiving instanceof PlayerEntity) {
                ((PlayerEntity) entityLiving).getCooldowns().addCooldown(this, cooldown);
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.duration;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        if (this.isDrink) {
            return UseAction.DRINK;
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
    public void appendHoverText(ItemStack stack, @Nullable World level, List<ITextComponent> tooltipComponents, ITooltipFlag isAdvanced) {
        if (!this.tooltips.isEmpty()) {
            this.tooltips.forEach(tip -> tooltipComponents.add(tip.get()));
        }
        if (level != null && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> {
                if (!CrockPotConfig.SHOW_FOOD_EFFECTS_TOOLTIP.get() || hideEffects) {
                    return;
                }
                if (!foodCounter.hasEaten(this)) {
                    tooltipComponents.add(new TranslationTextComponent("tooltip.crockpot.effect.not_eat").withStyle(TextFormatting.GRAY, TextFormatting.ITALIC));
                } else {
                    if (this.getEffects().isEmpty() && effectTooltips.isEmpty() && removedEffects.isEmpty() && heal <= 0.0F && (damage == null || damage.getValue() <= 0.0F)) {
                        tooltipComponents.add(new TranslationTextComponent("tooltip.crockpot.effect.no_effect").withStyle(TextFormatting.GRAY));
                        return;
                    }
                    this.getEffects().forEach(p -> {
                        EffectInstance effect = p.getKey();
                        IFormattableTextComponent tooltip = new TranslationTextComponent(effect.getDescriptionId());
                        if (effect.getAmplifier() > 0) {
                            tooltip = new TranslationTextComponent("potion.withAmplifier", tooltip, new TranslationTextComponent("potion.potency." + effect.getAmplifier()));
                        }
                        if (effect.getDuration() > 20) {
                            tooltip = new TranslationTextComponent("potion.withDuration", tooltip, EffectUtils.formatDuration(effect, 1.0F));
                        }
                        float probability = p.getValue();
                        if (probability < 1.0F) {
                            tooltip = new TranslationTextComponent("tooltip.crockpot.effect.with_probability", StringUtils.format(probability, "0.##%"), tooltip);
                        }
                        tooltipComponents.add(tooltip.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
                    });
                    if (!effectTooltips.isEmpty() || !removedEffects.isEmpty() || heal > 0.0F || (damage != null && damage.getValue() > 0.0F)) {
                        tooltipComponents.add(StringTextComponent.EMPTY);
                        tooltipComponents.add(new TranslationTextComponent("tooltip.crockpot.effect.when_" + (isDrink ? "drunk" : "eaten")).withStyle(TextFormatting.DARK_PURPLE));
                    }
                    effectTooltips.forEach(tip -> tooltipComponents.add(tip.get()));
                    removedEffects.forEach(e -> tooltipComponents.add(new TranslationTextComponent("tooltip.crockpot.effect.remove", new TranslationTextComponent(e.getDescriptionId())).withStyle(TextFormatting.GOLD)));
                    if (heal > 0.0F) {
                        float hearts = heal / 2.0F;
                        tooltipComponents.add(new TranslationTextComponent("tooltip.crockpot.effect.heal." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(TextFormatting.BLUE));
                    }
                    if (damage != null && damage.getValue() > 0.0F) {
                        float hearts = damage.getValue() / 2.0F;
                        tooltipComponents.add(new TranslationTextComponent("tooltip.crockpot.effect.damage." + (MathUtils.fuzzyEquals(hearts, 1.0F) ? "single" : "multiple"), StringUtils.format(hearts, "0.#")).withStyle(TextFormatting.RED));
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
        private Food.Builder foodBuilder = new Food.Builder();
        private int duration = FoodUseDuration.NORMAL.val;
        private boolean isDrink;
        private int cooldown;
        private float heal;
        private Pair<DamageSource, Float> damage;
        private final List<Effect> removedEffects = new ArrayList<>();
        private final List<Supplier<ITextComponent>> tooltips = new ArrayList<>();
        private boolean hideEffects;
        private final List<Supplier<ITextComponent>> effectTooltips = new ArrayList<>();

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

        public CrockPotFoodBuilder duration(FoodUseDuration durationIn) {
            this.duration = durationIn.val;
            return this;
        }

        public CrockPotFoodBuilder effect(Supplier<EffectInstance> effect, float probability) {
            this.foodBuilder = this.foodBuilder.effect(effect, probability);
            return this;
        }

        public CrockPotFoodBuilder effect(Supplier<EffectInstance> effect) {
            this.foodBuilder = this.foodBuilder.effect(effect, 1.0F);
            return this;
        }

        public CrockPotFoodBuilder effect(Effect effect, int duration, int amplifier, float probability) {
            return this.effect(() -> new EffectInstance(effect, duration, amplifier), probability);
        }

        public CrockPotFoodBuilder effect(Effect effect, int duration, int amplifier) {
            return this.effect(effect, duration, amplifier, 1.0F);
        }

        public CrockPotFoodBuilder effect(Effect effect, int duration, float probability) {
            return this.effect(() -> new EffectInstance(effect, duration), probability);
        }

        public CrockPotFoodBuilder effect(Effect effect, int duration) {
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

        public CrockPotFoodBuilder removeEffect(Effect effect) {
            this.removedEffects.add(effect);
            return this;
        }

        public CrockPotFoodBuilder tooltip(String key) {
            this.tooltips.add(() -> new TranslationTextComponent("tooltip.crockpot." + key));
            return this;
        }

        public CrockPotFoodBuilder tooltip(String key, TextFormatting... formats) {
            this.tooltips.add(() -> new TranslationTextComponent("tooltip.crockpot." + key).withStyle(formats));
            return this;
        }

        public CrockPotFoodBuilder hideEffects() {
            this.hideEffects = true;
            return this;
        }

        public CrockPotFoodBuilder effectTooltip(Supplier<ITextComponent> tooltip) {
            this.effectTooltips.add(tooltip);
            return this;
        }

        public CrockPotFoodBuilder effectTooltip(ITextComponent tooltip) {
            return this.effectTooltip(() -> tooltip);
        }

        public CrockPotFoodBuilder effectTooltip(String key, TextFormatting... formats) {
            return this.effectTooltip(new TranslationTextComponent("tooltip.crockpot.effect." + key).withStyle(formats));
        }

        public CrockPotFoodBuilder effectTooltip(String key) {
            return this.effectTooltip(new TranslationTextComponent("tooltip.crockpot.effect." + key));
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
