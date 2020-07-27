package com.sihenzhang.crockpot.container;

import com.sihenzhang.crockpot.container.slot.SlotCrockPotFuel;
import com.sihenzhang.crockpot.container.slot.SlotCrockPotOutput;
import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotContainer extends Container {
    private final CrockPotTileEntity tileEntity;

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
                addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
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

    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack slotStack = slot.getStack();
            itemStack = slotStack.copy();

            if (index == 5) {
                if (!mergeItemStack(slotStack, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(slotStack, itemStack);
            } else if (index >= 6) {
                if (CrockPotTileEntity.isValidIngredient(slotStack)) {
                    if (!mergeItemStack(slotStack, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (CrockPotTileEntity.isItemFuel(slotStack)) {
                    if (!mergeItemStack(slotStack, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 33) {
                    if (!this.mergeItemStack(slotStack, 33, 42, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 42 && !this.mergeItemStack(slotStack, 6, 33, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.mergeItemStack(slotStack, 6, 42, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.putStack(ItemStack.EMPTY);
            } else {
                slot.onSlotChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }
        return itemStack;
    }
}
