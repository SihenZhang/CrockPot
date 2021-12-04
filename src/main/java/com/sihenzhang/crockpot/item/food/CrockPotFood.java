package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.CrockPot;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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
    private final int useDuration;
    private final boolean drink;
    private final int cooldown;
    private final float heal;
    private final Pair<Supplier<DamageSource>, Float> damage;
    private final List<Supplier<Effect>> removedPotions;
    private final List<Supplier<ITextComponent>> tooltips;

    protected CrockPotFood(CrockPotFoodBuilder builder) {
        super(builder.properties.food(builder.foodBuilder.build()));
        this.useDuration = builder.useDuration;
        this.drink = builder.drink;
        this.cooldown = builder.cooldown;
        this.heal = builder.heal;
        this.damage = builder.damage;
        this.removedPotions = builder.removedPotions;
        this.tooltips = builder.tooltips;
    }

    public static CrockPotFoodBuilder builder() {
        return new CrockPotFoodBuilder();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isClientSide) {
            if (this.damage != null && this.damage.getValue() > 1E-6F) {
                entityLiving.hurt(this.damage.getKey().get(), this.damage.getValue());
            }
            if (this.heal > 1E-6F) {
                entityLiving.heal(this.heal);
            }
            if (!this.removedPotions.isEmpty()) {
                this.removedPotions.forEach(potion -> entityLiving.removeEffect(potion.get()));
            }
            if (this.cooldown > 0 && entityLiving instanceof PlayerEntity) {
                ((PlayerEntity) entityLiving).getCooldowns().addCooldown(this, this.cooldown);
            }
        }
        return super.finishUsingItem(stack, worldIn, entityLiving);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.useDuration;
    }

    @Override
    public UseAction getUseAnimation(ItemStack stack) {
        if (this.drink) {
            return UseAction.DRINK;
        } else {
            return super.getUseAnimation(stack);
        }
    }

    @Override
    public SoundEvent getEatingSound() {
        if (this.drink) {
            return SoundEvents.GENERIC_DRINK;
        } else {
            return super.getEatingSound();
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!this.tooltips.isEmpty()) {
            this.tooltips.forEach(tip -> tooltip.add(tip.get()));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    public static class CrockPotFoodBuilder {
        private Item.Properties properties = new Item.Properties().tab(CrockPot.ITEM_GROUP);
        private int maxStackSize = 64;
        private Rarity rarity = Rarity.COMMON;
        private Food.Builder foodBuilder = new Food.Builder();
        private int useDuration = FoodUseDuration.NORMAL.val;
        private boolean drink;
        private int cooldown;
        private float heal;
        private Pair<Supplier<DamageSource>, Float> damage;
        private final List<Supplier<Effect>> removedPotions = new ArrayList<>();
        private final List<Supplier<ITextComponent>> tooltips = new ArrayList<>();

        @Deprecated
        public CrockPotFoodBuilder hunger(int hungerIn) {
            return this.nutrition(hungerIn);
        }

        public CrockPotFoodBuilder nutrition(int nutrition) {
            this.foodBuilder = this.foodBuilder.nutrition(nutrition);
            return this;
        }

        @Deprecated
        public CrockPotFoodBuilder saturation(float saturationIn) {
            return this.saturationMod(saturationIn);
        }

        public CrockPotFoodBuilder saturationMod(float saturationModifier) {
            this.foodBuilder = this.foodBuilder.saturationMod(saturationModifier);
            return this;
        }

        public CrockPotFoodBuilder effect(Supplier<EffectInstance> effectIn, float probability) {
            this.foodBuilder = this.foodBuilder.effect(effectIn, probability);
            return this;
        }

        public CrockPotFoodBuilder effect(Supplier<EffectInstance> effectIn) {
            this.foodBuilder = this.foodBuilder.effect(effectIn, 1.0F);
            return this;
        }

        public CrockPotFoodBuilder effect(Effect potionIn, int durationIn, int amplifierIn, float probability) {
            return this.effect(() -> new EffectInstance(potionIn, durationIn, amplifierIn), probability);
        }

        public CrockPotFoodBuilder effect(Effect potionIn, int durationIn, int amplifierIn) {
            return this.effect(potionIn, durationIn, amplifierIn, 1.0F);
        }

        public CrockPotFoodBuilder effect(Effect potionIn, int durationIn, float probability) {
            return this.effect(() -> new EffectInstance(potionIn, durationIn), probability);
        }

        public CrockPotFoodBuilder effect(Effect potionIn, int durationIn) {
            return this.effect(potionIn, durationIn, 1.0F);
        }

        @Deprecated
        public CrockPotFoodBuilder effect(EffectInstance effectIn, float probability) {
            return this.effect(() -> effectIn, probability);
        }

        @Deprecated
        public CrockPotFoodBuilder effect(EffectInstance effectIn) {
            return this.effect(() -> effectIn);
        }

        public CrockPotFoodBuilder meat() {
            this.foodBuilder = this.foodBuilder.meat();
            return this;
        }

        @Deprecated
        public CrockPotFoodBuilder setAlwaysEdible() {
            return this.alwaysEat();
        }

        public CrockPotFoodBuilder alwaysEat() {
            this.foodBuilder = this.foodBuilder.alwaysEat();
            return this;
        }

        public CrockPotFoodBuilder duration(FoodUseDuration durationIn) {
            this.useDuration = durationIn.val;
            return this;
        }

        @Deprecated
        public CrockPotFoodBuilder setDrink() {
            return this.drink();
        }

        public CrockPotFoodBuilder drink() {
            this.drink = true;
            return this;
        }

        public CrockPotFoodBuilder cooldown(int cooldownIn) {
            this.cooldown = cooldownIn;
            return this;
        }

        public CrockPotFoodBuilder heal(float healIn) {
            this.heal = healIn;
            return this;
        }

        public CrockPotFoodBuilder damage(DamageSource damageSourceIn, float damageAmountIn) {
            this.damage = Pair.of(() -> damageSourceIn, damageAmountIn);
            return this;
        }

        @Deprecated
        public CrockPotFoodBuilder removePotion(Effect potionIn) {
            return this.removeEffect(potionIn);
        }

        public CrockPotFoodBuilder removeEffect(Effect effect) {
            this.removedPotions.add(() -> effect);
            return this;
        }

        public CrockPotFoodBuilder tooltip(String keyIn) {
            this.tooltips.add(() -> new TranslationTextComponent("tooltip.crockpot." + keyIn));
            return this;
        }

        public CrockPotFoodBuilder tooltip(String keyIn, TextFormatting... formatsIn) {
            this.tooltips.add(() -> new TranslationTextComponent("tooltip.crockpot." + keyIn).withStyle(formatsIn));
            return this;
        }

        @Deprecated
        public CrockPotFoodBuilder setHidden() {
            return this.hide();
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

        @Deprecated
        public CrockPotFoodBuilder maxStackSize(int maxStackSizeIn) {
            return this.stacksTo(maxStackSizeIn);
        }

        public CrockPotFoodBuilder stacksTo(int maxStackSize) {
            this.maxStackSize = maxStackSize;
            this.properties = this.properties.stacksTo(this.maxStackSize);
            return this;
        }

        public CrockPotFoodBuilder rarity(Rarity rarityIn) {
            this.rarity = rarityIn;
            this.properties = this.properties.rarity(this.rarity);
            return this;
        }

        public CrockPotFood build() {
            return new CrockPotFood(this);
        }
    }
}
