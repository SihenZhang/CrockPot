package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrockPotFoodBlockItem extends BlockItem {
    private final CrockPotFoodProperties foodProperties;

    public CrockPotFoodBlockItem(Block pBlock, CrockPotFoodProperties foodProperties) {
        super(pBlock, foodProperties.itemProperties);
        this.foodProperties = foodProperties;
    }

    @Override
    public InteractionResult place(BlockPlaceContext pContext) {
        if (!pContext.getPlayer().isShiftKeyDown()) {
            return InteractionResult.FAIL;
        }
        return super.place(pContext);
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
        pTooltipComponents.add(Component.empty());
        pTooltipComponents.add(I18nUtils.createTooltipComponent("placeable_while_sneaking").withStyle(ChatFormatting.GRAY));
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }
}
