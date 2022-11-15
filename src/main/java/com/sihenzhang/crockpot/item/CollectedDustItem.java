package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class CollectedDustItem extends CrockPotBaseItem {
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
        tooltipComponents.add(I18nUtils.createComponent("tooltip", "collected_dust").withStyle(ChatFormatting.DARK_AQUA));
        super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
    }
}
