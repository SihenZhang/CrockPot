package com.sihenzhang.crockpot.inventory.slot;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.IndexModifier;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import javax.annotation.Nonnull;

public class SlotCrockPotOutput extends ResourceHandlerSlot {
    public SlotCrockPotOutput(ResourceHandler<ItemResource> itemHandler, IndexModifier<ItemResource> slotModifier, int index, int xPosition, int yPosition) {
        super(itemHandler, slotModifier, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }
}
