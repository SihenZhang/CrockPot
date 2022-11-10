package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.AbstractCrockPotBlock;
import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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

public class CrockPotBlockEntity extends BlockEntity implements MenuProvider {
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

    public CrockPotBlockEntity(BlockPos pos, BlockState state) {
        super(CrockPotRegistry.CROCK_POT_BLOCK_ENTITY.get(), pos, state);
    }

    @Override
    public Component getDisplayName() {
        return new TranslatableComponent("container.crockpot.crock_pot");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inventory, Player player) {
        return new CrockPotMenu(containerId, inventory, this);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, CrockPotBlockEntity blockEntity) {
        boolean isBurning = blockEntity.isBurning();
        if (blockEntity.isBurning()) {
            blockEntity.burningTime--;
            blockEntity.hasChanged = true;
        }

        if ((blockEntity.isBurning() || blockEntity.canConsumeFuel()) && !blockEntity.isCooking() && blockEntity.itemHandlerOutput.getStackInSlot(0).isEmpty()) {
            CrockPotCookingRecipeInput recipeInput = blockEntity.getRecipeInput();
            if (recipeInput != null) {
                CrockPotCookingRecipe recipe = CrockPotCookingRecipe.getRecipeFor(recipeInput, level.random, level.getRecipeManager());
                if (recipe != null) {
                    blockEntity.cookingTotalTime = blockEntity.getActualCookingTotalTime(recipe);
                    blockEntity.result = recipe.assemble();
                    blockEntity.shrinkInputs();
                    blockEntity.hasChanged = true;
                }
            }
        }

        if (blockEntity.canConsumeFuel() && blockEntity.isCooking()) {
            ItemStack fuelStack = blockEntity.itemHandlerFuel.getStackInSlot(0);
            ItemStack tmpFuelStack = fuelStack.copy();
            tmpFuelStack.setCount(1);
            blockEntity.burningTime = blockEntity.burningTotalTime = ForgeHooks.getBurnTime(tmpFuelStack, null);
            fuelStack.shrink(1);
            if (fuelStack.isEmpty()) {
                blockEntity.itemHandlerFuel.setStackInSlot(0, tmpFuelStack.getContainerItem());
            }
            blockEntity.hasChanged = true;
        }

        if (blockEntity.isBurning() && blockEntity.isCooking() && blockEntity.itemHandlerOutput.getStackInSlot(0).isEmpty()) {
            blockEntity.cookingTime++;
            if (blockEntity.cookingTime >= blockEntity.cookingTotalTime) {
                blockEntity.cookingTime = 0;
                blockEntity.itemHandlerOutput.setStackInSlot(0, blockEntity.result);
                blockEntity.result = ItemStack.EMPTY;
            }
            blockEntity.hasChanged = true;
        }

        if (isBurning != blockEntity.isBurning()) {
            state = state.setValue(AbstractCrockPotBlock.LIT, blockEntity.isBurning());
            level.setBlock(pos, state, Block.UPDATE_ALL);
            blockEntity.hasChanged = true;
        }

        if (blockEntity.hasChanged) {
            blockEntity.markUpdated(level, pos, state);
            blockEntity.hasChanged = false;
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public int getPotLevel() {
        return ((AbstractCrockPotBlock) this.getBlockState().getBlock()).getPotLevel();
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
        return Math.max((int) (recipe.getCookingTime() * (1.0 - CrockPotConfigs.CROCK_POT_SPEED_MODIFIER.get() * this.getPotLevel())), 1);
    }

    private void sendTileEntityUpdatePacket() {
        if (!level.isClientSide) {
            Packet<ClientGamePacketListener> pkt = this.getUpdatePacket();
            if (pkt != null) {
                ((ServerLevel) level).getChunkSource().chunkMap.getPlayers(new ChunkPos(worldPosition), false).forEach(p -> p.connection.send(pkt));
            }
        }
    }

    private void markUpdated(Level level, BlockPos pos, BlockState state) {
        setChanged(level, pos, state);
        this.sendTileEntityUpdatePacket();
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        itemHandler.deserializeNBT(tag.getCompound("ItemHandler"));
        burningTime = tag.getInt("BurningTime");
        burningTotalTime = tag.getInt("BurningTotalTime");
        cookingTime = tag.getInt("CookingTime");
        cookingTotalTime = tag.getInt("CookingTotalTime");
        result.deserializeNBT(tag.getCompound("Result"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("ItemHandler", itemHandler.serializeNBT());
        tag.putInt("BurningTime", burningTime);
        tag.putInt("BurningTotalTime", burningTotalTime);
        tag.putInt("CookingTime", cookingTime);
        tag.putInt("CookingTotalTime", cookingTotalTime);
        tag.put("Result", result.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        this.saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
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
            return switch (side) {
                case UP -> itemHandlerInputCap.cast();
                case DOWN -> itemHandlerOutputCap.cast();
                default -> itemHandlerFuelCap.cast();
            };
        }
        return super.getCapability(cap, side);
    }
}
