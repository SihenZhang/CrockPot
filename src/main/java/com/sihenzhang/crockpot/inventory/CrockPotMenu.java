package com.sihenzhang.crockpot.inventory;

import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.inventory.slot.SlotCrockPotOutput;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;

import java.util.ArrayList;
import java.util.List;

public class CrockPotMenu extends AbstractContainerMenu {
    private final CrockPotBlockEntity blockEntity;

    public CrockPotMenu(int windowId, Inventory playerInventory, CrockPotBlockEntity blockEntity) {
        super(ModMenuTypes.CROCK_POT_MENU_TYPE.get(), windowId);
        this.blockEntity = blockEntity;

        if (this.blockEntity != null) {
            blockEntity.startOpen(playerInventory.player);

            ItemStacksResourceHandler itemHandler = this.blockEntity.getItemHandler();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    addSlot(new ResourceHandlerSlot(itemHandler, itemHandler::set, j + i * 2, 41 + j * 18, 22 + i * 18));
                }
            }

            addSlot(new ResourceHandlerSlot(itemHandler, itemHandler::set, 4, 92, 61));

            addSlot(new SlotCrockPotOutput(itemHandler, itemHandler::set, 5, 123, 31));
        }

        // Player Inventory
        for (var i = 0; i < 3; i++) {
            for (var j = 0; j < 9; j++) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 93 + i * 18));
            }
        }
        for (var i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 151));
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        blockEntity.stopOpen(pPlayer);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(blockEntity, pPlayer);
    }

    public BlockEntity getBlockEntity() {
        return blockEntity;
    }

    public List<ItemStack> getInputStacks() {
        var stacks = new ArrayList<ItemStack>(4);
        for (var i = 0; i < 4; i++) {
            stacks.add(blockEntity.getStackInSlot(i));
        }
        return stacks;
    }

    public boolean hasFuel() {
        return !blockEntity.getStackInSlot(4).isEmpty();
    }

    public boolean isBurning() {
        return blockEntity.isBurning();
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
                } else if (CrockPotBlockEntity.isFuel(slotStack, playerIn.level())) {
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
