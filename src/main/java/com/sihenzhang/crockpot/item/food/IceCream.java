package com.sihenzhang.crockpot.item.food;

import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IceCream extends CrockPotFood {
    public IceCream() {
        super(CrockPotFood.builder().nutrition(4).saturationMod(0.4F).duration(FoodUseDuration.FAST).cooldown(20).effectTooltip("ice_cream", ChatFormatting.AQUA));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
        if (!level.isClientSide) {
            livingEntity.removeAllEffects();
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
