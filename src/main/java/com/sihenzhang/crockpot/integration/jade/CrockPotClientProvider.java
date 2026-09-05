package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.core.FoodValues;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;
import snownee.jade.api.ui.JadeUI;
import snownee.jade.api.view.ProgressView;

import java.util.ArrayList;

public enum CrockPotClientProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!config.get(getUid())) {
            return;
        }
        var data = accessor.getServerData().getCompoundOrEmpty(getUid().toString());
        var inputs = new ArrayList<ItemStack>(4);
        for (var input : data.getListOrEmpty("Inputs")) {
            inputs.add(accessor.decodeFromNbt(ItemStack.OPTIONAL_STREAM_CODEC, input).orElse(ItemStack.EMPTY));
        }
        if (inputs.stream().anyMatch(stack -> !stack.isEmpty())) {
            tooltip.add(inputs.stream().map(JadeUI::item).toList());
            if (accessor.getPlayer().isShiftKeyDown()) {
                var foodValues = FoodValues.merge(inputs.stream()
                        .filter(stack -> !stack.isEmpty())
                        .map(stack -> FoodValuesDefinition.getFoodValues(stack, accessor.getLevel()))
                        .toList());
                var categoryCount = 0;
                for (var entry : foodValues.entrySet()) {
                    var icon = new FoodCategoryElement(entry.getKey());
                    if (categoryCount % 3 == 0) {
                        tooltip.add(icon);
                    } else {
                        tooltip.append(JadeUI.spacer(2, 0));
                        tooltip.append(icon);
                    }
                    tooltip.append(Component.literal("×" + entry.getValue()));
                    categoryCount++;
                }
            }
        }
        if (data.contains("Result")) {
            var result = accessor.decodeFromNbt(ItemStack.STREAM_CODEC, data.get("Result")).orElse(ItemStack.EMPTY);
            if (!result.isEmpty()) {
                tooltip.add(I18nUtil.integration("top", "recipe"));
                tooltip.append(JadeUI.item(result));
                tooltip.append(result.getHoverName());
            }
        }
        if (data.contains("CookingProgress")) {
            var progress = Mth.clamp(data.getFloatOr("CookingProgress", 0.0F), 0.0F, 1.0F);
            tooltip.add(JadeUI.progress(new ProgressView(
                    ProgressView.Part.of(progress), null, JadeUI.progressStyle(), BoxStyle.nestedBox())));
        }
    }

    @Override
    public Identifier getUid() {
        return ModIntegrationJade.CROCK_POT;
    }
}
