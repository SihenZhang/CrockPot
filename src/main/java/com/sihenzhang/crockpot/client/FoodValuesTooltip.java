package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.Config;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public final class FoodValuesTooltip {
    private static final MutableComponent DELIMITER = Component.literal(", ").withStyle(ChatFormatting.WHITE);

    private FoodValuesTooltip() {
    }

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        if (!Config.SHOW_FOOD_VALUES_TOOLTIP.get()) {
            return;
        }
        var player = event.getEntity();
        if (player == null || player.level() == null) {
            return;
        }
        var foodValues = FoodValuesDefinition.getFoodValues(event.getItemStack(), player.level());
        if (foodValues.isEmpty()) {
            return;
        }
        var tooltip = Component.empty();
        var first = true;
        for (var entry : foodValues.entrySet()) {
            var foodValuesText = I18nUtil.tooltip(
                    "food_values",
                    Component.translatable("item." + CrockPot.MOD_ID + ".food_category_" + entry.getKey().unwrapKey().map(ResourceKey::identifier).map(id -> id.getPath()).orElse("unknown")),
                    entry.getValue()
            ).withStyle(Style.EMPTY.withColor(entry.getKey().value().color()));
            if (!first) {
                tooltip.append(DELIMITER);
            }
            tooltip.append(foodValuesText);
            first = false;
        }
        event.getToolTip().add(tooltip);
    }
}
