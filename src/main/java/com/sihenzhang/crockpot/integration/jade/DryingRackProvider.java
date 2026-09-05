package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.DryingRackBlock;
import com.sihenzhang.crockpot.block.entity.DryingRackBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum DryingRackProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof DryingRackBlockEntity dryingRack) {
            var data = new CompoundTag();
            var items = new ListTag();
            for (var slot = 0; slot < dryingRack.getActiveSlotCount(); slot++) {
                var item = dryingRack.getItem(slot);
                if (item.isEmpty()) {
                    continue;
                }
                var entry = new CompoundTag();
                entry.put("Item", accessor.encodeAsNbt(ItemStack.STREAM_CODEC, item));
                entry.putFloat("Progress", dryingRack.getDryingProgress(slot));
                entry.putBoolean("Ready", dryingRack.isReady(slot));
                items.add(entry);
            }
            data.put("Items", items);
            data.putBoolean("CanDry", DryingRackBlock.canDry(accessor.getLevel(), accessor.getPosition(), accessor.getBlockState()));
            tag.put(getUid().toString(), data);
        }
    }

    @Override
    public Identifier getUid() {
        return ModIntegrationJade.DRYING_RACK;
    }
}
