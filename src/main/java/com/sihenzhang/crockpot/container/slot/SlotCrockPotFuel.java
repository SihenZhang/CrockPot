package com.sihenzhang.crockpot.container.slot;

import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class SlotCrockPotFuel extends SlotItemHandler {
    public SlotCrockPotFuel(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return CrockPotTileEntity.isItemFuel(stack);
    }
}
