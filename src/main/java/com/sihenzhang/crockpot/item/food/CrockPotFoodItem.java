package com.sihenzhang.crockpot.item.food;

import com.sihenzhang.crockpot.core.ModDataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Unit;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

import java.util.function.Consumer;

public class CrockPotFoodItem extends Item {
    public CrockPotFoodItem(Item.Properties properties) {
        super(properties.component(ModDataComponents.CONSUMABLE_TOOLTIPS, Unit.INSTANCE));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> builder, TooltipFlag tooltipFlag) {
        if (itemStack.has(ModDataComponents.CONSUMABLE_TOOLTIPS) && display.shows(ModDataComponents.CONSUMABLE_TOOLTIPS.get())) {
            Consumables.addToTooltip(itemStack, context, builder, tooltipFlag);
        }
    }
}
