package com.sihenzhang.crockpot.item.food;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPot;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.UseAction;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
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
        super(new Properties().group(CrockPot.ITEM_GROUP).food(builder.foodBuilder.build()));
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
    public ItemStack onItemUseFinish(ItemStack stack, World worldIn, LivingEntity entityLiving) {
        if (!worldIn.isRemote) {
            if (this.damage != null && this.damage.getSecond() > 1E-6F) {
                entityLiving.attackEntityFrom(this.damage.getFirst().get(), this.damage.getSecond());
            }
            if (this.heal > 1E-6F) {
                entityLiving.heal(this.heal);
            }
            if (!this.removedPotions.isEmpty()) {
                this.removedPotions.forEach(potion -> entityLiving.removePotionEffect(potion.get()));
            }
            if (this.cooldown > 0 && entityLiving instanceof PlayerEntity) {
                ((PlayerEntity) entityLiving).getCooldownTracker().setCooldown(this, this.cooldown);
            }
        }
        return super.onItemUseFinish(stack, worldIn, entityLiving);
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return this.useDuration;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        if (this.drink) {
            return UseAction.DRINK;
        } else {
            return super.getUseAction(stack);
        }
    }

    @Override
    public SoundEvent getEatSound() {
        if (this.drink) {
            return SoundEvents.ENTITY_GENERIC_DRINK;
        } else {
            return super.getEatSound();
        }
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (!this.tooltips.isEmpty()) {
            this.tooltips.forEach(tip -> tooltip.add(tip.get()));
        }
        super.addInformation(stack, worldIn, tooltip, flagIn);
    }

    public static class CrockPotFoodBuilder {
        private Food.Builder foodBuilder = new Food.Builder();
        private int useDuration = FoodUseDuration.NORMAL.val;
        private boolean drink;
        private int cooldown;
        private float heal;
        private Pair<Supplier<DamageSource>, Float> damage;
        private final List<Supplier<Effect>> removedPotions = Lists.newArrayList();
        private final List<Supplier<ITextComponent>> tooltips = Lists.newArrayList();

        public CrockPotFoodBuilder hunger(int hungerIn) {
            this.foodBuilder = this.foodBuilder.hunger(hungerIn);
            return this;
        }

        public CrockPotFoodBuilder saturation(float saturationIn) {
            this.foodBuilder = this.foodBuilder.saturation(saturationIn);
            return this;
        }

        public CrockPotFoodBuilder effect(EffectInstance effectIn, float probability) {
            this.foodBuilder = this.foodBuilder.effect(() -> effectIn, probability);
            return this;
        }

        public CrockPotFoodBuilder effect(Effect potionIn, int durationIn, int amplifierIn, float probability) {
            return this.effect(new EffectInstance(potionIn, durationIn, amplifierIn), probability);
        }

        public CrockPotFoodBuilder effect(EffectInstance effectIn) {
            return this.effect(effectIn, 1.0F);
        }

        public CrockPotFoodBuilder effect(Effect potionIn, int durationIn, int amplifierIn) {
            return this.effect(new EffectInstance(potionIn, durationIn, amplifierIn));
        }

        public CrockPotFoodBuilder effect(Effect potionIn, int durationIn) {
            return this.effect(new EffectInstance(potionIn, durationIn));
        }

        public CrockPotFoodBuilder meat() {
            this.foodBuilder = this.foodBuilder.meat();
            return this;
        }

        public CrockPotFoodBuilder setAlwaysEdible() {
            this.foodBuilder = this.foodBuilder.setAlwaysEdible();
            return this;
        }

        public CrockPotFoodBuilder duration(FoodUseDuration durationIn) {
            this.useDuration = durationIn.val;
            return this;
        }

        public CrockPotFoodBuilder setDrink() {
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

        public CrockPotFoodBuilder removePotion(Effect potionIn) {
            this.removedPotions.add(() -> potionIn);
            return this;
        }

        public CrockPotFoodBuilder tooltip(String keyIn) {
            this.tooltips.add(() -> new TranslationTextComponent("tooltip.crockpot." + keyIn));
            return this;
        }

        public CrockPotFoodBuilder tooltip(String keyIn, TextFormatting... formatsIn) {
            this.tooltips.add(() -> new TranslationTextComponent("tooltip.crockpot." + keyIn).mergeStyle(formatsIn));
            return this;
        }

        public CrockPotFood build() {
            return new CrockPotFood(this);
        }
    }
}
