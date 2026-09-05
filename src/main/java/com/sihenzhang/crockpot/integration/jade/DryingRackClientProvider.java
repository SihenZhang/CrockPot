package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
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

public enum DryingRackClientProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!config.get(getUid())) {
            return;
        }
        var data = accessor.getServerData().getCompoundOrEmpty(getUid().toString());
        var hasDryingItems = false;
        for (var itemTag : data.getListOrEmpty("Items")) {
            if (!(itemTag instanceof CompoundTag entry) || !entry.contains("Item")) {
                continue;
            }
            var item = accessor.decodeFromNbt(ItemStack.STREAM_CODEC, entry.get("Item")).orElse(ItemStack.EMPTY);
            if (item.isEmpty()) {
                continue;
            }
            tooltip.add(JadeUI.item(item));
            if (entry.getBooleanOr("Ready", false)) {
                tooltip.append(I18nUtil.integration("jade", "drying_rack.ready").withStyle(ChatFormatting.GREEN));
            } else {
                hasDryingItems = true;
                var progress = Mth.clamp(entry.getFloatOr("Progress", 0.0F), 0.0F, 1.0F);
                tooltip.append(JadeUI.progress(new ProgressView(
                        ProgressView.Part.of(progress), Component.literal((int) (progress * 100) + "%"),
                        JadeUI.progressStyle(), BoxStyle.nestedBox()), 60, 14).alignSelfCenter());
            }
        }
        if (hasDryingItems && !data.getBooleanOr("CanDry", true)) {
            tooltip.add(I18nUtil.integration("jade", "drying_rack.paused").withStyle(ChatFormatting.YELLOW));
        }
    }

    @Override
    public Identifier getUid() {
        return ModIntegrationJade.DRYING_RACK;
    }
}
