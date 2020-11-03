package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CategoryTooltip {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        EnumMap<FoodCategory, Float> values = CrockPot.FOOD_CATEGORY_MANAGER.valuesOf(item);
        if (!values.isEmpty()) {
            List<ITextComponent> toolTip = event.getToolTip();
            List<String> result = new ArrayList<>();
            for (Map.Entry<FoodCategory, Float> category : values.entrySet()) {
                result.add(I18n.format("item." + CrockPot.MOD_ID + ".food_category_" + category.getKey().name().toLowerCase()) + ": " + category.getValue());
            }
            toolTip.add(new StringTextComponent(String.join(", ", result)));
        }
    }
}
