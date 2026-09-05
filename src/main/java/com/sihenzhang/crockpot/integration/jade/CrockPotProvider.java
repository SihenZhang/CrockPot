package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemStack;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IServerDataProvider;

public enum CrockPotProvider implements IServerDataProvider<BlockAccessor> {
    INSTANCE;

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        if (accessor.getBlockEntity() instanceof CrockPotBlockEntity crockPot) {
            var data = new CompoundTag();
            var inputs = new ListTag();
            for (var slot = 0; slot < 4; slot++) {
                inputs.add(accessor.encodeAsNbt(ItemStack.OPTIONAL_STREAM_CODEC, crockPot.getStackInSlot(slot)));
            }
            data.put("Inputs", inputs);
            if (crockPot.isCooking()) {
                data.put("Result", accessor.encodeAsNbt(ItemStack.STREAM_CODEC, crockPot.getResult()));
                data.putFloat("CookingProgress", crockPot.getCookingProgress());
            }
            tag.put(getUid().toString(), data);
        }
    }

    @Override
    public Identifier getUid() {
        return ModIntegrationJade.CROCK_POT;
    }
}
