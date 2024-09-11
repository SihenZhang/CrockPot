package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.attachment.ModAttachmentTypes;
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
    public int getUseDuration(ItemStack stack, LivingEntity entity) {
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
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.addAll(foodProperties.getTooltips());
        if (Minecraft.getInstance().player != null) {
            var foodCounter = Minecraft.getInstance().player.getData(ModAttachmentTypes.FOOD_COUNTER);
            tooltipComponents.addAll(foodProperties.getEffectTooltips(context, foodCounter.hasEaten(this)));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }
}
