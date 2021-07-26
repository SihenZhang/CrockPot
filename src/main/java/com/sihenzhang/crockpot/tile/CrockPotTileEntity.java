package com.sihenzhang.crockpot.tile;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.base.FoodValueSum;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipe;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot < 4) {
                if (!isValidIngredient(stack)) {
                    return stack;
                }
            } else if (slot == 4) {
                if (!isItemFuel(stack)) {
                    return stack;
                }
            }
            shouldDoMatch = !simulate;
            return super.insertItem(slot, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            ItemStack result = super.extractItem(slot, amount, simulate);
            if (!result.isEmpty()) {
                shouldDoMatch = !simulate;
            }
            return result;
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            shouldDoMatch = true;
            super.setStackInSlot(slot, stack);
        }
    };

    private final RangedWrapper itemHandlerInput = new RangedWrapper(itemHandler, 0, 4);
    private final RangedWrapper itemHandlerFuel = new RangedWrapper(itemHandler, 4, 5);
    private final RangedWrapper itemHandlerOutput = new RangedWrapper(itemHandler, 5, 6);

    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> itemHandlerInputCap = LazyOptional.of(() -> itemHandlerInput);
    private final LazyOptional<IItemHandler> itemHandlerFuelCap = LazyOptional.of(() -> itemHandlerFuel);
    private final LazyOptional<IItemHandler> itemHandlerOutputCap = LazyOptional.of(() -> itemHandlerOutput);

    int burnTime;
    int currentItemBurnTime;
    int processTime;

    // do recipe match after restarting the server
    boolean shouldDoMatch = true;

    public CrockPotTileEntity() {
        super(CrockPotRegistry.crockPotTileEntity);
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

    CrockPotRecipe currentRecipe = CrockPotRecipe.EMPTY;
    CompletableFuture<CrockPotRecipe> pendingRecipe;

    CrockPotState currentState = CrockPotState.IDLE;

    @Override
    public void tick() {
        currentState = CrockPotState.doPotTick(currentState, this);
    }

    int getPotLevel() {
        assert level != null;
        return ((CrockPotBlock) level.getBlockState(worldPosition).getBlock()).getPotLevel();
    }

    public CrockPotRecipe getCurrentRecipe() {
        return currentRecipe;
    }

    @Nullable
    CrockPotRecipeInput getRecipeInput() {
        List<ItemStack> stacks = new ArrayList<>(4);
        for (int i = 0; i < 4; ++i) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                return null;
            }
            ItemStack stack = itemHandler.getStackInSlot(i).copy();
            stack.setCount(1);
            stacks.add(stack);
        }
        FoodValueSum sum = new FoodValueSum(
                stacks.stream().map(ItemStack::getItem)
                        .map(CrockPot.FOOD_CATEGORY_MANAGER::valuesOf).collect(Collectors.toList())
        );
        return new CrockPotRecipeInput(sum, stacks, getPotLevel());
    }

    void consumeFuel() {
        ItemStack fuelStack = itemHandler.getStackInSlot(4);
        if (!fuelStack.isEmpty()) {
            ItemStack copy = fuelStack.copy();
            copy.setCount(1);
            currentItemBurnTime = ForgeHooks.getBurnTime(copy);
            if (currentItemBurnTime > 0) {
                fuelStack.shrink(1);
                this.burnTime += currentItemBurnTime;
            }
            if (copy.getItem().getContainerItem(copy) != null && itemHandler.getStackInSlot(4).isEmpty()) {
                itemHandler.setStackInSlot(4, copy.getContainerItem());
            }
        }
    }

    void updateBurningState() {
        assert level != null;
        if (!level.isClientSide) {
            this.level.setBlock(this.worldPosition, this.level.getBlockState(this.worldPosition).setValue(CrockPotBlock.LIT, this.isBurning()), 3);
        }
    }

    void shrinkInputs() {
        for (int i = 0; i < 4; ++i) {
            itemHandler.getStackInSlot(i).shrink(1);
        }
    }

    void sync() {
        assert level != null;
        if (level.isClientSide) {
            return;
        }
        SUpdateTileEntityPacket pkt = getUpdatePacket();
        assert pkt != null;
        ((ServerWorld) level).getChunkSource().chunkMap.getPlayers(new ChunkPos(worldPosition), false)
                .forEach(p -> p.connection.send(pkt));
        setChanged();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, 1, this.serializeNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        deserializeNBT(pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.save(super.getUpdateTag());
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        return ForgeHooks.getBurnTime(itemStack) > 0;
    }

    public static boolean isValidIngredient(ItemStack itemStack) {
        return !CrockPot.FOOD_CATEGORY_MANAGER.valuesOf(itemStack.getItem()).isEmpty();
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        itemHandler.deserializeNBT(compound.getCompound("ItemHandler"));
        burnTime = compound.getShort("BurnTime");
        currentItemBurnTime = compound.getShort("CurrentItemBurnTime");
        processTime = compound.getShort("ProcessTime");
        currentState = CrockPotState.valueOf(compound.getString("CurrentState"));
        if (compound.contains("CurrentRecipe")) {
            currentRecipe = new CrockPotRecipe((CompoundNBT) Objects.requireNonNull(compound.get("CurrentRecipe")));
        }
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        super.save(compound);
        compound.put("ItemHandler", itemHandler.serializeNBT());
        compound.putShort("BurnTime", (short) burnTime);
        compound.putShort("CurrentItemBurnTime", (short) currentItemBurnTime);
        compound.putShort("ProcessTime", (short) processTime);
        compound.putString("CurrentState", currentState.name());
        if (currentRecipe != null) {
            compound.put("CurrentRecipe", currentRecipe.serializeNBT());
        }
        return compound;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    public float getBurnTimeProgress() {
        return (float) burnTime / (float) currentItemBurnTime;
    }

    public boolean isProcessing() {
        return currentState == CrockPotState.PROCESSING;
    }

    public float getProcessTimeProgress() {
        if (currentRecipe == null) {
            return 0F;
        }
        return (float) processTime / (currentRecipe.getCookTime() * (1.0F - 0.15F * this.getPotLevel()));
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
