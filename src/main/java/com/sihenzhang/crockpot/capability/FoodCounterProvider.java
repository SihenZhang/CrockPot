package com.sihenzhang.crockpot.capability;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.attachment.IAttachmentHolder;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;

public class FoodCounterProvider implements IAttachmentSerializer<CompoundTag, IFoodCounter> {
    @Override
    public IFoodCounter read(IAttachmentHolder holder, CompoundTag tag, HolderLookup.Provider registries) {
        var counter = new FoodCounter();
        counter.deserializeNBT(tag);
        return counter;
    }

    @Override
    public CompoundTag write(IFoodCounter counter, HolderLookup.Provider registries) {
        return counter.serializeNBT();
    }
}
