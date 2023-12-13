package com.sihenzhang.crockpot.block.entity;

import com.google.common.base.Preconditions;
import com.sihenzhang.crockpot.CrockPotConfigs;
import com.sihenzhang.crockpot.base.CrockPotSoundEvents;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.util.I18nUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RangedWrapper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class CrockPotBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStackHandler itemHandler = new ItemStackHandler(6) {
        @Override
        public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
            if (slot < 4) {
                return CrockPotBlockEntity.this.isValidIngredient(stack);
            }
            if (slot == 4) {
                return isFuel(stack);
            }
            return super.isItemValid(slot, stack);
        }

        @Override
        protected void onContentsChanged(int slot) {
            super.onContentsChanged(slot);
            CrockPotBlockEntity.this.markUpdated();
        }
    };
    private final RangedWrapper itemHandlerInput = new RangedWrapper(itemHandler, 0, 4);
    private final RangedWrapper itemHandlerFuel = new RangedWrapper(itemHandler, 4, 5);
    private final RangedWrapper itemHandlerOutput = new RangedWrapper(itemHandler, 5, 6);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            CrockPotBlockEntity.this.playSound(pState, CrockPotSoundEvents.CROCK_POT_OPEN.get());
            CrockPotBlockEntity.this.updateBlockState(pState, true);
        }

        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            CrockPotBlockEntity.this.playSound(pState, CrockPotSoundEvents.CROCK_POT_CLOSE.get());
            CrockPotBlockEntity.this.updateBlockState(pState, false);
        }

        @Override
        protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) {
        }

        @Override
        protected boolean isOwnContainer(Player pPlayer) {
            if (pPlayer.containerMenu instanceof CrockPotMenu crockPotMenu) {
                return crockPotMenu.getBlockEntity() == CrockPotBlockEntity.this;
            }
            return false;
        }
    };
    private final int potLevel;

    private int burningTime;
    private int burningTotalTime;
    private int cookingTime;
    private int cookingTotalTime;
    private ItemStack result = ItemStack.EMPTY;


    public CrockPotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrockPotBlockEntities.CROCK_POT_BLOCK_ENTITY.get(), pPos, pBlockState);
        Preconditions.checkArgument(pBlockState.getBlock() instanceof CrockPotBlock, "Block of the `CrockPotEntity` must be an instance of `CrockPotBlock`.");
        this.potLevel = ((CrockPotBlock) pBlockState.getBlock()).getPotLevel();
    }

    @Override
    public Component getDisplayName() {
        return I18nUtils.createComponent("container", "crock_pot");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new CrockPotMenu(pContainerId, pPlayerInventory, this);
    }

    public static void serverTick(Level pLevel, BlockPos pPos, BlockState pState, CrockPotBlockEntity pBlockEntity) {
        var hasChanged = false;
        var isBurning = pBlockEntity.isBurning();

        if (pBlockEntity.isBurning()) {
            pBlockEntity.burningTime--;
            hasChanged = true;
        }

        var fuelStack = pBlockEntity.itemHandlerFuel.getStackInSlot(0);

        // the Crock Pot can only cook when it is burning or has fuel
        if (pBlockEntity.isBurning() || isFuel(fuelStack)) {
            // if the Crock Pot is not cooking and output slot is empty, consume inputs and start cooking
            if (!pBlockEntity.isCooking() && pBlockEntity.itemHandlerOutput.getStackInSlot(0).isEmpty()) {
                var recipeWrapper = pBlockEntity.getRecipeWrapper();
                if (recipeWrapper != null) {
                    var optionalRecipe = CrockPotCookingRecipe.getRecipeFor(recipeWrapper, pLevel);
                    if (optionalRecipe.isPresent()) {
                        var recipe = optionalRecipe.get();
                        pBlockEntity.cookingTotalTime = pBlockEntity.getActualCookingTotalTime(recipe);
                        pBlockEntity.result = recipe.assemble(recipeWrapper, pLevel.registryAccess());
                        pBlockEntity.shrinkInputs();
                        Containers.dropContents(pLevel, pPos, recipe.getRemainingItems(recipeWrapper));
                        hasChanged = true;
                    }
                }
            }

            if (pBlockEntity.isCooking()) {
                // if the Crock Pot is cooking and not burning, consume fuel and start burning
                if (!pBlockEntity.isBurning() && isFuel(fuelStack)) {
                    pBlockEntity.burningTime = pBlockEntity.burningTotalTime = ForgeHooks.getBurnTime(fuelStack, null);
                    var remainingItem = fuelStack.getCraftingRemainingItem();
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        pBlockEntity.itemHandlerFuel.setStackInSlot(0, remainingItem);
                    }
                    hasChanged = true;
                }
                // if the Crock Pot is cooking and burning, add cooking time
                if (pBlockEntity.isBurning() && pBlockEntity.itemHandlerOutput.getStackInSlot(0).isEmpty()) {
                    pBlockEntity.cookingTime++;
                    // finish cooking and output result
                    if (pBlockEntity.cookingTime >= pBlockEntity.cookingTotalTime) {
                        pBlockEntity.cookingTime = 0;
                        pBlockEntity.itemHandlerOutput.setStackInSlot(0, pBlockEntity.result);
                        pBlockEntity.result = ItemStack.EMPTY;
                    }
                    hasChanged = true;
                }
            }
        }

        // if the burning status has changed, update the block state
        if (isBurning != pBlockEntity.isBurning()) {
            pState = pState.setValue(CrockPotBlock.LIT, pBlockEntity.isBurning());
            pLevel.setBlock(pPos, pState, Block.UPDATE_ALL);
            hasChanged = true;
        }

        if (hasChanged) {
            pBlockEntity.markUpdated();
        }
    }

    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    public int getPotLevel() {
        return potLevel;
    }

    @Nullable
    public CrockPotCookingRecipe.Wrapper getRecipeWrapper() {
        var size = itemHandlerInput.getSlots();
        var stacks = new ArrayList<ItemStack>(size);
        for (var i = 0; i < size; i++) {
            var stackInSlot = itemHandlerInput.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                return null;
            }
            stacks.add(stackInSlot.copyWithCount(1));
        }
        var mergedFoodValues = FoodValues.merge(stacks.stream().map(stack -> FoodValuesDefinition.getFoodValues(stack, level)).toList());
        return new CrockPotCookingRecipe.Wrapper(stacks, mergedFoodValues, this.getPotLevel());
    }

    public boolean isValidIngredient(ItemStack stack) {
        return !FoodValuesDefinition.getFoodValues(stack, level).isEmpty();
    }

    public static boolean isFuel(ItemStack pStack) {
        return ForgeHooks.getBurnTime(pStack, null) > 0;
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

    private void shrinkInputs() {
        for (int i = 0; i < itemHandlerInput.getSlots(); i++) {
            itemHandlerInput.getStackInSlot(i).shrink(1);
        }
    }

    private int getActualCookingTotalTime(CrockPotCookingRecipe recipe) {
        return Math.max((int) (recipe.getCookingTime() * (1.0 - CrockPotConfigs.CROCK_POT_SPEED_MODIFIER.get() * this.getPotLevel())), 1);
    }

    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("ItemHandler"));
        burningTime = pTag.getInt("BurningTime");
        burningTotalTime = pTag.getInt("BurningTotalTime");
        cookingTime = pTag.getInt("CookingTime");
        cookingTotalTime = pTag.getInt("CookingTotalTime");
        if (pTag.contains("Result", Tag.TAG_COMPOUND)) {
            result.deserializeNBT(pTag.getCompound("Result"));
        }
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("ItemHandler", itemHandler.serializeNBT());
        pTag.putInt("BurningTime", burningTime);
        pTag.putInt("BurningTotalTime", burningTotalTime);
        pTag.putInt("CookingTime", cookingTime);
        pTag.putInt("CookingTotalTime", cookingTotalTime);
        pTag.put("Result", result.serializeNBT());
    }

    @Override
    public CompoundTag getUpdateTag() {
        var tag = new CompoundTag();
        tag.put("ItemHandler", itemHandler.serializeNBT());
        tag.putInt("BurningTime", burningTime);
        tag.putInt("BurningTotalTime", burningTotalTime);
        tag.putInt("CookingTime", cookingTime);
        tag.putInt("CookingTotalTime", cookingTotalTime);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void markUpdated() {
        this.setChanged();
        level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }

    public void startOpen(Player pPlayer) {
        if (!remove && !pPlayer.isSpectator()) {
            openersCounter.incrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState());
        }
    }

    public void stopOpen(Player pPlayer) {
        if (!remove && !pPlayer.isSpectator()) {
            openersCounter.decrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState());
        }
    }

    public void recheckOpen() {
        if (!remove) {
            openersCounter.recheckOpeners(level, this.getBlockPos(), this.getBlockState());
        }
    }

    void updateBlockState(BlockState pState, boolean pOpen) {
        level.setBlock(this.getBlockPos(), pState.setValue(CrockPotBlock.OPEN, pOpen), Block.UPDATE_ALL);
    }

    void playSound(BlockState pState, SoundEvent pSound) {
        var vec3i = pState.getValue(CrockPotBlock.FACING).getNormal();
        var d0 = (double) worldPosition.getX() + 0.5D + (double) vec3i.getX() / 2.0D;
        var d1 = (double) worldPosition.getY() + 0.5D + (double) vec3i.getY() / 2.0D;
        var d2 = (double) worldPosition.getZ() + 0.5D + (double) vec3i.getZ() / 2.0D;
        level.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
    }

    private final LazyOptional<IItemHandler> itemHandlerCap = LazyOptional.of(() -> itemHandler);
    private final LazyOptional<IItemHandler> itemHandlerInputCap = LazyOptional.of(() -> itemHandlerInput);
    private final LazyOptional<IItemHandler> itemHandlerFuelCap = LazyOptional.of(() -> itemHandlerFuel);
    private final LazyOptional<IItemHandler> itemHandlerOutputCap = LazyOptional.of(() -> itemHandlerOutput);

    @Override
    public @Nonnull <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
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
