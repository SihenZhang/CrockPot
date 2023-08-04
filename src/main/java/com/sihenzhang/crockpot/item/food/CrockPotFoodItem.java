package com.sihenzhang.crockpot.item.food;

import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;

public class CrockPotFoodItem extends Item {
    private final int duration;
    private final boolean isDrink;
    private final SoundEvent eatingSound;
    private final int cooldown;
    private final float heal;
    private final Pair<ResourceKey<DamageType>, Float> damage;
    private final List<MobEffect> removedEffects;
    private final List<Supplier<Component>> tooltips;
    private final boolean hideEffects;
    private final List<Supplier<Component>> effectTooltips;

    protected CrockPotFoodItem(CrockPotFoodBuilder builder) {
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

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            if (damage != null) {
                var damageTypeKey = damage.getFirst();
                // Should hurt even if the damage is 0, just like hit by a thrown egg
                pLevel.registryAccess().registry(Registries.DAMAGE_TYPE).flatMap(reg -> reg.getHolder(damageTypeKey)).ifPresent(damageType -> pLivingEntity.hurt(new DamageSource(damageType), damage.getSecond()));
            }
            if (heal > 0.0F) {
                pLivingEntity.heal(heal);
            }
            removedEffects.forEach(pLivingEntity::removeEffect);
            if (cooldown > 0 && pLivingEntity instanceof Player player) {
                player.getCooldowns().addCooldown(this, cooldown);
            }
        }
        var containerStack = this.getCraftingRemainingItem(pStack);
        var remainedStack = super.finishUsingItem(pStack, pLevel, pLivingEntity);
        if (remainedStack.isEmpty()) {
            return containerStack;
        } else {
            if (pLivingEntity instanceof Player player && !player.getAbilities().instabuild && !player.getInventory().add(containerStack)) {
                player.drop(containerStack, false);
            }
            return remainedStack;
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return duration;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return isDrink ? UseAnim.DRINK : super.getUseAnimation(pStack);
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
                        pTooltipComponents.add(tooltip.withStyle(effect.getEffect().getCategory().getTooltipFormatting()));
                    });
                    if (!effectTooltips.isEmpty() || !removedEffects.isEmpty() || heal > 0.0F || (damage != null && damage.getSecond() > 0.0F)) {
                        pTooltipComponents.add(Component.empty());
                        pTooltipComponents.add(I18nUtils.createTooltipComponent("effect.when_" + (isDrink ? "drunk" : "eaten")).withStyle(ChatFormatting.DARK_PURPLE));
                    }
                    effectTooltips.forEach(tip -> pTooltipComponents.add(tip.get()));
                    removedEffects.forEach(e -> pTooltipComponents.add(I18nUtils.createTooltipComponent("effect.remove", Component.translatable(e.getDescriptionId())).withStyle(ChatFormatting.GOLD)));
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
}
