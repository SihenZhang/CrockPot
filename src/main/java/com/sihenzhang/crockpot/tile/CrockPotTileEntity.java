package com.sihenzhang.crockpot.tile;

import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipeInput;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CrockPotTileEntity extends TileEntity implements ITickableTileEntity, INamedContainerProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Nonnull
        @Override
        public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
            if (slot < 4) {
                if (!isValidIngredient(stack)) {
                    return stack;
                }
            } else if (slot == 4) {
                if (!isFuel(stack)) {
                    return stack;
                }
            }
            return super.insertItem(slot, stack, simulate);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            hasChanged = true;
        }
    };

    private final RangedWrapper itemHandlerInput = new RangedWrapper(itemHandler, 0, 4);
    private final RangedWrapper itemHandlerFuel = new RangedWrapper(itemHandler, 4, 5);
    private final RangedWrapper itemHandlerOutput = new RangedWrapper(itemHandler, 5, 6);

    private int burningTime;
    private int burningTotalTime;
    private int cookingTime;
    private int cookingTotalTime;
    private ItemStack result = ItemStack.EMPTY;

    private boolean hasChanged;

    public CrockPotTileEntity() {
        super(CrockPotRegistry.crockPotTileEntity);
    }

    @Override
    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("container.crockpot.crock_pot");
    }

    @Nullable
    @Override
    public Container createMenu(int containerId, PlayerInventory inventory, PlayerEntity player) {
        return new CrockPotContainer(containerId, inventory, this);
    }

    @Override
    public void tick() {
        boolean isBurning = this.isBurning();
        if (this.isBurning()) {
            burningTime--;
        }
        if (!level.isClientSide) {
            if ((this.isBurning() || this.canConsumeFuel()) && !this.isCooking() && itemHandlerOutput.getStackInSlot(0).isEmpty()) {
                CrockPotCookingRecipeInput recipeInput = this.getRecipeInput();
                if (recipeInput != null) {
                    CrockPotCookingRecipe recipe = CrockPotCookingRecipe.getRecipeFor(recipeInput, level.random, level.getRecipeManager());
                    if (recipe != null) {
                        cookingTotalTime = this.getActualCookingTotalTime(recipe);
                        result = recipe.assemble();
                        this.shrinkInputs();
                        hasChanged = true;
                    }
                }
            }
            if (this.canConsumeFuel() && this.isCooking()) {
                ItemStack fuelStack = itemHandlerFuel.getStackInSlot(0);
                ItemStack tmpFuelStack = fuelStack.copy();
                tmpFuelStack.setCount(1);
                burningTime = burningTotalTime = ForgeHooks.getBurnTime(tmpFuelStack, null);
                fuelStack.shrink(1);
                if (fuelStack.isEmpty()) {
                    itemHandlerFuel.setStackInSlot(0, tmpFuelStack.getContainerItem());
                }
                hasChanged = true;
            }
            if (this.isBurning() && this.isCooking() && itemHandlerOutput.getStackInSlot(0).isEmpty()) {
                cookingTime++;
                if (cookingTime == cookingTotalTime) {
                    cookingTime = 0;
                    itemHandlerOutput.setStackInSlot(0, result);
                    result = ItemStack.EMPTY;
                }
                // TODO: Is there a way to mark hasChanged only when cooking is done just like Furnace in vanilla?
                hasChanged = true;
            }
            if (isBurning != this.isBurning()) {
                level.setBlock(worldPosition, level.getBlockState(worldPosition).setValue(CrockPotBlock.LIT, this.isBurning()), 3);
                hasChanged = true;
            }
        }
        if (hasChanged) {
            this.markUpdated();
            hasChanged = false;
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public int getPotLevel() {
        return ((CrockPotBlock) this.getBlockState().getBlock()).getPotLevel();
    }

    @Nullable
    public CrockPotCookingRecipeInput getRecipeInput() {
        int size = itemHandlerInput.getSlots();
        List<ItemStack> stacks = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            ItemStack stackInSlot = itemHandlerInput.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                return null;
            }
            ItemStack stack = stackInSlot.copy();
            stack.setCount(1);
            stacks.add(stack);
        }
        FoodValues mergedFoodValues = FoodValues.merge(stacks.stream()
                .map(stack -> FoodValuesDefinition.getFoodValues(stack.getItem(), level.getRecipeManager())).collect(Collectors.toList()));
        return new CrockPotCookingRecipeInput(mergedFoodValues, stacks, this.getPotLevel());
    }

    public boolean isValidIngredient(ItemStack stack) {
        return !FoodValuesDefinition.getFoodValues(stack.getItem(), level.getRecipeManager()).isEmpty();
    }

    public static boolean isFuel(ItemStack stack) {
        return ForgeHooks.getBurnTime(stack, null) > 0;
    }

    public boolean isBurning() {
        return burningTime > 0;
    }

    public float getBurningProgress() {
        return burningTotalTime != 0 ? (float) burningTime / (float) burningTotalTime : 0.0F;
    }

    public boolean isCooking() {
        return result != null && !result.isEmpty();
    }

    public float getCookingProgress() {
        return cookingTotalTime != 0 ? (float) cookingTime / (float) cookingTotalTime : 0.0F;
    }

    public ItemStack getResult() {
        return result;
    }

    private boolean canConsumeFuel() {
        if (!this.isBurning()) {
            ItemStack fuelStack = itemHandlerFuel.getStackInSlot(0);
            if (!fuelStack.isEmpty()) {
                ItemStack tmpFuelStack = fuelStack.copy();
                tmpFuelStack.setCount(1);
                return ForgeHooks.getBurnTime(tmpFuelStack, null) > 0;
            }
        }
        return false;
    }

    private void shrinkInputs() {
        for (int i = 0; i < itemHandlerInput.getSlots(); i++) {
            itemHandlerInput.getStackInSlot(i).shrink(1);
        }
    }

    private int getActualCookingTotalTime(CrockPotCookingRecipe recipe) {
        return Math.max((int) (recipe.getCookingTime() * (1.0 - CrockPotConfig.CROCK_POT_SPEED_MODIFIER.get() * this.getPotLevel())), 1);
    }

    private void sendTileEntityUpdatePacket() {
        if (!level.isClientSide) {
            SUpdateTileEntityPacket pkt = this.getUpdatePacket();
            if (pkt != null) {
                ((ServerWorld) level).getChunkSource().chunkMap.getPlayers(new ChunkPos(worldPosition), false).forEach(p -> p.connection.send(pkt));
            }
        }
    }

    private void markUpdated() {
        this.setChanged();
        this.sendTileEntityUpdatePacket();
    }

    @Override
    public void load(BlockState state, CompoundNBT tag) {
        super.load(state, tag);
        itemHandler.deserializeNBT(tag.getCompound("ItemHandler"));
        burningTime = tag.getInt("BurningTime");
        burningTotalTime = tag.getInt("BurningTotalTime");
        cookingTime = tag.getInt("CookingTime");
        cookingTotalTime = tag.getInt("CookingTotalTime");
        result.deserializeNBT(tag.getCompound("Result"));
    }

    @Override
    public CompoundNBT save(CompoundNBT tag) {
        super.save(tag);
        tag.put("ItemHandler", itemHandler.serializeNBT());
        tag.putInt("BurningTime", burningTime);
        tag.putInt("BurningTotalTime", burningTotalTime);
        tag.putInt("CookingTime", cookingTime);
        tag.putInt("CookingTotalTime", cookingTotalTime);
        tag.put("Result", result.serializeNBT());
        return tag;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(worldPosition, -1, this.serializeNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.deserializeNBT(pkt.getTag());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        return this.serializeNBT();
    }

    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> itemHandlerInputCap = LazyOptional.of(() -> itemHandlerInput);
    private final LazyOptional<IItemHandler> itemHandlerFuelCap = LazyOptional.of(() -> itemHandlerFuel);
    private final LazyOptional<IItemHandler> itemHandlerOutputCap = LazyOptional.of(() -> itemHandlerOutput);

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
