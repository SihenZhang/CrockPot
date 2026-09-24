package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class CollectedDustItem extends CrockPotBaseItem {
    @Override
    public void appendHoverText(ItemStack pStack, TooltipContext pContext, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        pTooltipComponents.add(I18nUtils.createTooltipComponent("collected_dust").withStyle(ChatFormatting.DARK_AQUA));
        super.appendHoverText(pStack, pContext, pTooltipComponents, pIsAdvanced);
    }
}
