package com.sihenzhang.crockpot.container;

import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.container.slot.SlotCrockPotOutput;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrockPotContainer extends Container {
    private final CrockPotTileEntity tileEntity;

    public CrockPotContainer(int windowId, PlayerInventory playerInventory, CrockPotTileEntity tileEntity) {
        super(CrockPotRegistry.crockPotContainer, windowId);
        this.tileEntity = tileEntity;

        if (this.tileEntity != null) {
            ItemStackHandler itemHandler = this.tileEntity.getItemHandler();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    addSlot(new SlotItemHandler(itemHandler, j + i * 2, 39 + j * 18, 17 + i * 18));
                }
            }

            addSlot(new SlotItemHandler(itemHandler, 4, 48, 71));

            addSlot(new SlotCrockPotOutput(itemHandler, 5, 117, 44));
        }

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
    public boolean stillValid(PlayerEntity playerIn) {
        BlockPos pos = tileEntity.getBlockPos();
        return playerIn.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    public TileEntity getTileEntity() {
        return tileEntity;
    }

    public int getBurningProgress() {
        return (int) (13 * tileEntity.getBurningProgress());
    }

    public int getCookingProgress() {
        return (int) (24 * tileEntity.getCookingProgress());
    }

    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (index == 5) {
                if (!this.moveItemStackTo(slotStack, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemStack);
            } else if (index >= 6) {
                if (tileEntity.isValidIngredient(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (CrockPotTileEntity.isFuel(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 33) {
                    if (!this.moveItemStackTo(slotStack, 33, 42, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 42 && !this.moveItemStackTo(slotStack, 6, 33, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(slotStack, 6, 42, false)) {
                return ItemStack.EMPTY;
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, slotStack);
        }
        return itemStack;
    }
}
