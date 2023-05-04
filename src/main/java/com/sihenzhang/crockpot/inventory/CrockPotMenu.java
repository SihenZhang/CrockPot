package com.sihenzhang.crockpot.inventory;

import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.inventory.slot.SlotCrockPotOutput;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrockPotMenu extends AbstractContainerMenu {
    private final CrockPotBlockEntity blockEntity;

    public CrockPotMenu(int windowId, Inventory playerInventory, CrockPotBlockEntity blockEntity) {
        super(CrockPotMenuTypes.CROCK_POT_MENU_TYPE.get(), windowId);
        this.blockEntity = blockEntity;

        if (this.blockEntity != null) {
            ItemStackHandler itemHandler = this.blockEntity.getItemHandler();
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
    public boolean stillValid(Player playerIn) {
        BlockPos pos = blockEntity.getBlockPos();
        return playerIn.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) <= 64.0;
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public int getBurningProgress() {
        return (int) (13 * blockEntity.getBurningProgress());
    }

    public int getCookingProgress() {
        return (int) (24 * blockEntity.getCookingProgress());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();

            if (index == 5) {
                if (!this.moveItemStackTo(slotStack, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }

                slot.onQuickCraft(slotStack, itemStack);
            } else if (index >= 6) {
                if (blockEntity.isValidIngredient(slotStack)) {
                    if (!this.moveItemStackTo(slotStack, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (CrockPotBlockEntity.isFuel(slotStack)) {
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
