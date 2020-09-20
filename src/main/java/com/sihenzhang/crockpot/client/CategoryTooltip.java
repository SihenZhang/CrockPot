package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.CategoryDefinitionItem;
import com.sihenzhang.crockpot.base.FoodCategory;
import net.minecraft.item.Item;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class CategoryTooltip {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        Map<FoodCategory, Float> values = CrockPot.INGREDIENT_MANAGER.valuesOf(item);
        if (values != null) {
            StringBuilder result = new StringBuilder();
            List<ITextComponent> toolTip = event.getToolTip();
            boolean isFirstValue = true;
            for (Map.Entry<FoodCategory, Float> category : values.entrySet()) {
                if (!isFirstValue) {
                    result.append(", ");
                }
                result.append(new TranslationTextComponent("item." + CrockPot.MOD_ID + ".ingredient_" + category.getKey().name().toLowerCase()).getFormattedText())
                        .append(": ").append(category.getValue());
                isFirstValue = false;
            }
            toolTip.add(new StringTextComponent(result.toString()));
        }
    }
}
