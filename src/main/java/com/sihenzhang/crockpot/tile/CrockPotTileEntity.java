package com.sihenzhang.crockpot.tile;

import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            // TODO: Valid Ingredients
            if (slot == 4) {
                if (!isItemFuel(stack)) {
                    return stack;
                }
            }
            return super.insertItem(slot, stack, simulate);
        }
    };

    private final RangedWrapper itemHandlerInput = new RangedWrapper(itemHandler, 0, 4);
    private final RangedWrapper itemHandlerFuel = new RangedWrapper(itemHandler, 4, 5);
    private final RangedWrapper itemHandlerOutput = new RangedWrapper(itemHandler, 5, 6);

    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> itemHandlerInputCap = LazyOptional.of(() -> itemHandlerInput);
    private final LazyOptional<IItemHandler> itemHandlerFuelCap = LazyOptional.of(() -> itemHandlerFuel);
    private final LazyOptional<IItemHandler> itemHandlerOutputCap = LazyOptional.of(() -> itemHandlerOutput);

    private int burnTime;
    private int currentItemBurnTime;
    private int processTime;

    public CrockPotTileEntity() {
        super(CrockPotRegistry.crockPotTileEntity.get());
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.crockpot.crock_pot");
    }

    @Nullable
    @Override
    public Container createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new CrockPotContainer(i, playerInventory, this);
    }

    @Override
    public void tick() {
        // TODO: Recipe System
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        return getBurnTime(itemStack) > 0;
    }

    protected static int getBurnTime(ItemStack itemStack) {
        if (itemStack.isEmpty()) {
            return 0;
        } else {
            int ret = itemStack.getBurnTime();
            return ForgeEventFactory.getItemBurnTime(itemStack, ret == -1 ? ForgeHooks.getBurnTime(itemStack) : ret);
        }
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        itemHandler.deserializeNBT(compound.getCompound("ItemHandler"));
        burnTime = compound.getShort("BurnTime");
        currentItemBurnTime = compound.getShort("CurrentItemBurnTime");
        processTime = compound.getShort("ProcessTime");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("ItemHandler", itemHandler.serializeNBT());
        compound.putShort("BurnTime", (short) burnTime);
        compound.putShort("CurrentItemBurnTime", (short) currentItemBurnTime);
        compound.putShort("ProcessTime", (short) processTime);
        return compound;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    public float getBurnTimeProgress() {
        if (processTime == 0 && burnTime > 0) {
            return 1f;
        }
        return (float) burnTime / (float) currentItemBurnTime;
    }

    public boolean isProcessing() {
        return processTime > 0;
    }

    public float getProcessTimeProgress() {
        if (processTime == 0) {
            return 0f;
        }
        return (float) processTime / 100;
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == null) {
                return itemHandlerCap.cast();
            }
            switch (side) {
                case UP:
                    return itemHandlerInputCap.cast();
                case DOWN:
                    return itemHandlerOutputCap.cast();
                default:
                    return itemHandlerFuelCap.cast();
            }
        }
        return super.getCapability(cap, side);
    }
}
