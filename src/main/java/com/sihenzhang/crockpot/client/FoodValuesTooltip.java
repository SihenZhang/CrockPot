package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public class FoodValuesTooltip {
    private static final IFormattableTextComponent DELIMITER = new StringTextComponent(", ").withStyle(TextFormatting.WHITE);

    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player != null && player.level != null) {
            FoodValues foodValues = FoodValuesDefinition.getFoodValues(event.getItemStack().getItem(), player.level.getRecipeManager());
            if (!foodValues.isEmpty()) {
                IFormattableTextComponent tooltip = null;
                for (Pair<FoodCategory, Float> entry : foodValues.entrySet()) {
                    IFormattableTextComponent foodValuesText = new TranslationTextComponent("tooltip.crockpot.food_values", new TranslationTextComponent("item." + CrockPot.MOD_ID + ".food_category_" + entry.getKey().name().toLowerCase()), entry.getValue()).withStyle(Style.EMPTY.withColor(entry.getKey().color));
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
