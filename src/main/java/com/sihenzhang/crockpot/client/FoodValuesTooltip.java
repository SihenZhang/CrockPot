package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public class FoodValuesTooltip {
    private static final MutableComponent DELIMITER = Component.literal(", ").withStyle(ChatFormatting.WHITE);

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        Player player = event.getEntity();
        if (player != null && player.level != null) {
            FoodValues foodValues = FoodValuesDefinition.getFoodValues(event.getItemStack().getItem(), player.level.getRecipeManager());
            if (!foodValues.isEmpty()) {
                MutableComponent tooltip = null;
                for (Pair<FoodCategory, Float> entry : foodValues.entrySet()) {
                    MutableComponent foodValuesText = Component.translatable("tooltip.crockpot.food_values", Component.translatable("item." + CrockPot.MOD_ID + ".food_category_" + entry.getKey().name().toLowerCase()), entry.getValue()).withStyle(Style.EMPTY.withColor(entry.getKey().color));
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
}
