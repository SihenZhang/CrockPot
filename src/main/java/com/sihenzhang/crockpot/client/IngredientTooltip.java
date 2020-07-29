package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.CrockPotIngredient;
import com.sihenzhang.crockpot.base.CrockPotIngredientType;
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
public class IngredientTooltip {
    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onTooltip(ItemTooltipEvent event) {
        Item item = event.getItemStack().getItem();
        CrockPotIngredient crockPotIngredient = CrockPot.INGREDIENT_MANAGER.getIngredientFromItem(item);
        if (crockPotIngredient != null) {
            StringBuilder result = new StringBuilder();
            List<ITextComponent> toolTip = event.getToolTip();
            boolean isFirstIngredientValue = true;
            for (Map.Entry<CrockPotIngredientType, Float> ingredient : crockPotIngredient.getIngredientValue().entrySet()) {
                if (!isFirstIngredientValue) {
                    result.append(", ");
                }
                result.append(new TranslationTextComponent("item." + CrockPot.MOD_ID + ".ingredient_" + ingredient.getKey().name().toLowerCase()).getFormattedText())
                        .append(": ").append(ingredient.getValue());
                isFirstIngredientValue = false;
            }
            toolTip.add(new StringTextComponent(result.toString()));
        }
    }
}
