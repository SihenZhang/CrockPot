package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
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

public enum BirdcageClientProvider implements IBlockComponentProvider {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (!config.get(getUid())) {
            return;
        }
        var data = accessor.getServerData().getCompoundOrEmpty(getUid().toString());
        data.getString("OwnerName").or(() -> BirdcageProvider.getOwnerName(accessor))
                .ifPresent(name -> tooltip.add(Component.translatable("jade.owner", name)));
        for (var outputTag : data.getListOrEmpty("OutputBuffer")) {
            if (!(outputTag instanceof CompoundTag output) || !output.contains("Item")) {
                continue;
            }
            var item = accessor.decodeFromNbt(ItemStack.STREAM_CODEC, output.get("Item")).orElse(ItemStack.EMPTY);
            if (item.isEmpty()) {
                continue;
            }
            tooltip.add(JadeUI.item(item));
            var remainingTime = output.getLongOr("Time", BirdcageBlockEntity.OUTPUT_COOLDOWN);
            var progress = Mth.clamp(1.0F - (float) remainingTime / BirdcageBlockEntity.OUTPUT_COOLDOWN, 0.0F, 1.0F);
            tooltip.append(JadeUI.progress(new ProgressView(
                    ProgressView.Part.of(progress), Component.literal((int) (progress * 100) + "%"),
                    JadeUI.progressStyle(), BoxStyle.nestedBox()), 60, 14).alignSelfCenter());
        }
    }

    @Override
    public Identifier getUid() {
        return ModIntegrationJade.BIRDCAGE;
    }
}
