package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.EnumMap;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CategoryTooltip {
    private static final IFormattableTextComponent DELIMITER = new StringTextComponent(", ").setStyle(Style.EMPTY.withColor(Color.parseColor("white")));

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        EnumMap<FoodCategory, Float> values = CrockPot.FOOD_CATEGORY_MANAGER.valuesOf(item);
        if (!values.isEmpty()) {
            IFormattableTextComponent tooltip = null;
            for (Map.Entry<FoodCategory, Float> category : values.entrySet()) {
                IFormattableTextComponent categoryText = new StringTextComponent(I18n.get("item." + CrockPot.MOD_ID + ".food_category_" + category.getKey().name().toLowerCase()) + ": " + category.getValue()).setStyle(Style.EMPTY.withColor(category.getKey().color));
                if (tooltip == null) {
                    tooltip = categoryText;
                } else {
                    tooltip.append(DELIMITER).append(categoryText);
                }
            }
            event.getToolTip().add(tooltip);
        }
    }
}
