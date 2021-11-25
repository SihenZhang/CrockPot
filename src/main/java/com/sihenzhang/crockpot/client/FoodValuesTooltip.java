package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public class FoodValuesTooltip {
    private static final IFormattableTextComponent DELIMITER = new StringTextComponent(", ").setStyle(Style.EMPTY.withColor(Color.parseColor("white")));

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        FoodValues foodValues = FoodValuesDefinition.getFoodValues(event.getItemStack().getItem(), Minecraft.getInstance().level.getRecipeManager());
        if (!foodValues.isEmpty()) {
            IFormattableTextComponent tooltip = null;
            for (Pair<FoodCategory, Float> entry : foodValues.entrySet()) {
                IFormattableTextComponent foodValuesText = new StringTextComponent(I18n.get("item." + CrockPot.MOD_ID + ".food_category_" + entry.getKey().name().toLowerCase()) + ": " + entry.getValue()).setStyle(Style.EMPTY.withColor(entry.getKey().color));
                if (tooltip == null) {
                    tooltip = foodValuesText;
                } else {
                    tooltip.append(DELIMITER).append(foodValuesText);
                }
            }
            event.getToolTip().add(tooltip);
        }
    }
}
