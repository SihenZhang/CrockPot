package com.sihenzhang.crockpot.item.food;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
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
        pTooltipComponents.addAll(foodProperties.getEffectTooltips(this, Minecraft.getInstance().player));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
