package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrockPotFoodItem extends Item {
    private final CrockPotFoodProperties foodProperties;

    public CrockPotFoodItem(CrockPotFoodProperties foodProperties) {
        super(foodProperties.itemProperties);
        this.foodProperties = foodProperties;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        super.finishUsingItem(pStack, pLevel, pLivingEntity);
        if (pLivingEntity instanceof Player player) {
            foodProperties.addCooldown(this, player);
        }
        foodProperties.hurt(pLevel, pLivingEntity);
        foodProperties.heal(pLevel, pLivingEntity);
        foodProperties.removeEffects(pLevel, pLivingEntity);
        var containerStack = this.getCraftingRemainingItem(pStack);
        if (pStack.isEmpty()) {
            return containerStack;
        } else {
            if (pLivingEntity instanceof Player player && !player.getAbilities().instabuild && !player.getInventory().add(containerStack)) {
                player.drop(containerStack, false);
            }
            return pStack;
        }
    }

    @Override
    public int getUseDuration(ItemStack pStack) {
        return foodProperties.getUseDuration();
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return foodProperties.getUseAnimation();
    }

    @Override
    public SoundEvent getEatingSound() {
        return foodProperties.getSound();
    }

    @Override
    public SoundEvent getDrinkingSound() {
        return foodProperties.getSound();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.addAll(foodProperties.getTooltips());
        if (pLevel != null && Minecraft.getInstance().player != null) {
            Minecraft.getInstance().player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY)
                    .ifPresent(foodCounter -> pTooltipComponents.addAll(foodProperties.getEffectTooltips(foodCounter.hasEaten(this))));
        }
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
