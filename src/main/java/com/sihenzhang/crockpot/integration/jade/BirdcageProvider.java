package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IBlockComponentProvider;
import snownee.jade.api.IServerDataProvider;
import snownee.jade.api.ITooltip;
import snownee.jade.api.config.IPluginConfig;
import snownee.jade.api.ui.BoxStyle;

public enum BirdcageProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor accessor, IPluginConfig config) {
        if (config.get(ModIntegrationJade.BIRDCAGE)) {
            var serverData = accessor.getServerData();
            if (serverData.contains("OutputBuffer", Tag.TAG_LIST)) {
                var elements = tooltip.getElementHelper();
                var outputBuffer = serverData.getList("OutputBuffer", Tag.TAG_COMPOUND);
                for (var i = 0; i < outputBuffer.size(); i++) {
                    var output = outputBuffer.getCompound(i);
                    tooltip.add(elements.item(ItemStack.of(output.getCompound("Item"))));
                    var progress = (float) (BirdcageBlockEntity.OUTPUT_COOLDOWN - output.getLong("Time")) / (float) BirdcageBlockEntity.OUTPUT_COOLDOWN;
                    tooltip.append(elements.progress(progress, Component.literal((int) (progress * 100) + "%"), elements.progressStyle(), BoxStyle.DEFAULT, false).size(new Vec2(60, 14)).translate(new Vec2(0, 4)));
                }
            }
        }
    }

    @Override
    public void appendServerData(CompoundTag data, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof BirdcageBlockEntity birdcageBlockEntity) {
            var list = new ListTag();
            for (var output : birdcageBlockEntity.getOutputBuffer()) {
                var tag = new CompoundTag();
                tag.put("Item", output.getFirst().serializeNBT());
                tag.putLong("Time", output.getSecond() - accessor.getLevel().getGameTime());
                list.add(tag);
            }
            data.put("OutputBuffer", list);
        }
    }

    @Override
    public ResourceLocation getUid() {
        return ModIntegrationJade.BIRDCAGE;
    }
}
