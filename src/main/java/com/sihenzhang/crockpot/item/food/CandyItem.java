package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class CandyItem extends CrockPotFoodBlockItem {
    public CandyItem(Item.Properties properties) {
        super(ModBlocks.CANDY.get(), properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> tooltip, TooltipFlag flag) {
        if (flag.hasShiftDown()) {
            tooltip.accept(I18nUtil.tooltip("candy.real").withStyle(ChatFormatting.ITALIC).withStyle(Style.EMPTY.withColor(0x270727)));
        } else {
            tooltip.accept(I18nUtil.tooltip("candy").withStyle(ChatFormatting.DARK_AQUA));
        }
        super.appendHoverText(stack, context, display, tooltip, flag);
    }
}
