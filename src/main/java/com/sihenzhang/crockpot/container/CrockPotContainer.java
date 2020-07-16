package com.sihenzhang.crockpot.container;

import com.sihenzhang.crockpot.container.slot.SlotCrockPotFuel;
import com.sihenzhang.crockpot.container.slot.SlotCrockPotOutput;
import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraftforge.items.SlotItemHandler;

public class CrockPotContainer extends Container {
    private final CrockPotTileEntity tileEntity;
    private int lastCookTime;
    private int lastBurnTime;

    public CrockPotContainer(int windowId, PlayerInventory playerInventory, CrockPotTileEntity tileEntity) {
        super(CrockPotRegistry.crockPotContainer.get(), windowId);
        this.tileEntity = tileEntity;

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                addSlot(new SlotItemHandler(tileEntity.getItemHandler(), j + i * 2, 39 + j * 18, 17 + i * 18));
            }
        }

        addSlot(new SlotCrockPotFuel(tileEntity.getItemHandler(), 4, 48, 71));

        addSlot(new SlotCrockPotOutput(tileEntity.getItemHandler(), 5, 117, 44));

        // Player Inventory
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(playerInventory, j + i * 9 + 6, 8 + j * 18, 102 + i * 18));
            }
        }

        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(playerInventory, i, 8 + i * 18, 160));
        }
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    public CrockPotTileEntity getTileEntity() {
        return tileEntity;
    }
}
