package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public class FoodValuesTooltip {
    private static final MutableComponent DELIMITER = new TextComponent(", ").setStyle(Style.EMPTY.withColor(TextColor.parseColor("white")));

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        FoodValues foodValues = FoodValuesDefinition.getFoodValues(event.getItemStack().getItem(), Minecraft.getInstance().level.getRecipeManager());
        if (!foodValues.isEmpty()) {
            MutableComponent tooltip = null;
            for (Pair<FoodCategory, Float> entry : foodValues.entrySet()) {
                MutableComponent foodValuesText = new TextComponent(I18n.get("item." + CrockPot.MOD_ID + ".food_category_" + entry.getKey().name().toLowerCase()) + ": " + entry.getValue()).setStyle(Style.EMPTY.withColor(entry.getKey().color));
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
