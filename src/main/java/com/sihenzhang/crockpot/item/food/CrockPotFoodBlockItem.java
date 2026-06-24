package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.item.SneakPlaceBlockItem;
import com.sihenzhang.crockpot.core.ModDataComponents;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

public class CrockPotFoodBlockItem extends SneakPlaceBlockItem {
    public CrockPotFoodBlockItem(Block block, Item.Properties properties) {
        super(block, properties.component(ModDataComponents.CONSUMABLE_TOOLTIPS, Unit.INSTANCE));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (itemStack.has(ModDataComponents.CONSUMABLE_TOOLTIPS) && display.shows(ModDataComponents.CONSUMABLE_TOOLTIPS.get())) {
            Consumables.addToTooltip(itemStack, context, builder, tooltipFlag);
        }
        builder.accept(Component.empty());
        builder.accept(I18nUtil.tooltip("placeable_while_sneaking").withStyle(ChatFormatting.GRAY));
    }
}
