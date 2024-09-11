package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public class FoodValuesTooltip {
    private static final MutableComponent DELIMITER = Component.literal(", ").withStyle(ChatFormatting.WHITE);

    @SubscribeEvent
    public static void onTooltip(final ItemTooltipEvent event) {
        if (!CrockPotConfigs.SHOW_FOOD_VALUES_TOOLTIP.get()) {
            return;
        }
        var player = event.getEntity();
        // #57: level can be null when loading resource pack
        if (player != null && player.level() != null) {
            var foodValues = FoodValuesDefinition.getFoodValues(event.getItemStack(), player.level());
            if (!foodValues.isEmpty()) {
                var tooltip = foodValues.entrySet().stream()
                        .map(entry -> I18nUtils.createTooltipComponent("food_values",
                                Component.translatable("item." + CrockPot.MOD_ID + ".food_category_" + entry.getKey().name().toLowerCase()), entry.getValue()).withStyle(Style.EMPTY.withColor(entry.getKey().getColor())).withStyle(Style.EMPTY.withColor(entry.getKey().getColor())))
                        .reduce(null, (acc, foodValuesText) ->
                                acc == null ? foodValuesText : acc.append(DELIMITER).append(foodValuesText));
                event.getToolTip().add(tooltip);
            }
        }
    }
}
