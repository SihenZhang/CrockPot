package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.block.CrockPotBlocks;
import net.minecraft.ChatFormatting;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IceCreamItem extends CrockPotFoodBlockItem {
    public IceCreamItem() {
        super(CrockPotBlocks.ICE_CREAM.get(), CrockPotFoodProperties.builder(4, 0.4F)
                .duration(FoodUseDuration.FAST)
                .cooldown(20)
                .effectTooltip("ice_cream", ChatFormatting.AQUA)
                .build()
        );
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            pLivingEntity.removeAllEffects();
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}
