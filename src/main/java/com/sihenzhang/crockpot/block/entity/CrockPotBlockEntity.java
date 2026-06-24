package com.sihenzhang.crockpot.block.entity;

import com.google.common.base.Preconditions;
import com.sihenzhang.crockpot.Config;
import com.sihenzhang.crockpot.core.ModSoundEvents;
import com.sihenzhang.crockpot.core.FoodValues;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.RangedResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;

public class CrockPotBlockEntity extends BlockEntity implements MenuProvider {
    private final ItemStacksResourceHandler itemHandler = new ItemStacksResourceHandler(6) {
        @Override
        public boolean isValid(int slot, ItemResource resource) {
            if (slot < 4) {
                return CrockPotBlockEntity.this.isValidIngredient(resource.toStack());
            }
            if (slot == 4) {
                return CrockPotBlockEntity.this.level != null && isFuel(resource.toStack(), CrockPotBlockEntity.this.level);
            }
            return false;
        }

        @Override
        protected void onContentsChanged(int slot, ItemStack previousContents) {
            super.onContentsChanged(slot, previousContents);
            CrockPotBlockEntity.this.markUpdated();
        }
    };
    private final ResourceHandler<ItemResource> itemHandlerInput = RangedResourceHandler.of(itemHandler, 0, 4);
    private final ResourceHandler<ItemResource> itemHandlerFuel = RangedResourceHandler.of(itemHandler, 4, 5);
    private final ResourceHandler<ItemResource> itemHandlerOutput = RangedResourceHandler.of(itemHandler, 5, 6);
    private final ContainerOpenersCounter openersCounter = new ContainerOpenersCounter() {
        @Override
        protected void onOpen(Level pLevel, BlockPos pPos, BlockState pState) {
            CrockPotBlockEntity.this.playSound(pState, ModSoundEvents.CROCK_POT_OPEN.get());
            CrockPotBlockEntity.this.updateBlockState(pState, true);
        }

        @Override
        protected void onClose(Level pLevel, BlockPos pPos, BlockState pState) {
            CrockPotBlockEntity.this.playSound(pState, ModSoundEvents.CROCK_POT_CLOSE.get());
            CrockPotBlockEntity.this.updateBlockState(pState, false);
        }

        @Override
        protected void openerCountChanged(Level pLevel, BlockPos pPos, BlockState pState, int pCount, int pOpenCount) {
        }

        @Override
        public boolean isOwnContainer(Player pPlayer) {
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

    private int cookingSoundPlayingTime;

    public CrockPotBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(CrockPotBlockEntities.CROCK_POT_BLOCK_ENTITY.get(), pPos, pBlockState);
        Preconditions.checkArgument(pBlockState.getBlock() instanceof CrockPotBlock, "Block of the `CrockPotEntity` must be an instance of `CrockPotBlock`.");
        this.potLevel = ((CrockPotBlock) pBlockState.getBlock()).getPotLevel();
    }

    @Override
    public Component getDisplayName() {
        return I18nUtil.of("container", "crock_pot");
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

        var fuelStack = pBlockEntity.getStackInSlot(4);

        // the Crock Pot can only cook when it is burning or has fuel
        if (pBlockEntity.isBurning() || isFuel(fuelStack, pLevel)) {
            // if the Crock Pot is not cooking and output slot is empty, consume inputs and start cooking
            if (!pBlockEntity.isCooking() && pBlockEntity.getStackInSlot(5).isEmpty()) {
                var recipeWrapper = pBlockEntity.getRecipeWrapper();
                if (recipeWrapper != null) {
                    var optionalRecipe = CrockPotCookingRecipe.getRecipeFor(recipeWrapper, pLevel);
                    if (optionalRecipe.isPresent()) {
                        var recipe = optionalRecipe.get();
                        pBlockEntity.cookingTotalTime = pBlockEntity.getActualCookingTotalTime(recipe);
                        pBlockEntity.result = recipe.assemble(recipeWrapper);
                        pBlockEntity.shrinkInputs();
                        Containers.dropContents(pLevel, pPos, recipe.getRemainingItems(recipeWrapper));
                        hasChanged = true;
                    }
                }
            }

            if (pBlockEntity.isCooking()) {
                // if the Crock Pot is cooking and not burning, consume fuel and start burning
                if (!pBlockEntity.isBurning() && isFuel(fuelStack, pLevel)) {
                    pBlockEntity.burningTime = pBlockEntity.burningTotalTime = fuelStack.getBurnTime(null, pLevel.fuelValues());
                    var remainingTemplate = fuelStack.getItem().getCraftingRemainder(fuelStack);
                    var remainingItem = remainingTemplate == null ? ItemStack.EMPTY : remainingTemplate.create();
                    fuelStack.shrink(1);
                    if (fuelStack.isEmpty()) {
                        pBlockEntity.setStackInSlot(4, remainingItem);
                    } else {
                        pBlockEntity.setStackInSlot(4, fuelStack);
                    }
                    hasChanged = true;
                }
                // if the Crock Pot is cooking and burning, add cooking time
                if (pBlockEntity.isBurning() && pBlockEntity.getStackInSlot(5).isEmpty()) {
                    pBlockEntity.cookingTime++;
                    // play cooking sound
                    if (pBlockEntity.cookingSoundPlayingTime % 5 == 0) {
                        pBlockEntity.playSound(pState, ModSoundEvents.CROCK_POT_RATTLE.get());
                        pBlockEntity.cookingSoundPlayingTime = 0;
                    }
                    pBlockEntity.cookingSoundPlayingTime++;
                    // finish cooking and output result
                    if (pBlockEntity.cookingTime >= pBlockEntity.cookingTotalTime) {
                        pBlockEntity.cookingTime = 0;
                        pBlockEntity.setStackInSlot(5, pBlockEntity.result);
                        pBlockEntity.result = ItemStack.EMPTY;
                    }
                    hasChanged = true;
                }
            }
        }

        // if the Crock Pot is not burning, reset cooking sound playing time
        if (!pBlockEntity.isBurning()) {
            pBlockEntity.cookingSoundPlayingTime = 0;
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

    public ItemStacksResourceHandler getItemHandler() {
        return itemHandler;
    }

    public ItemStack getStackInSlot(int slot) {
        return itemHandler.getResource(slot).toStack(itemHandler.getAmountAsInt(slot));
    }

    public int getPotLevel() {
        return potLevel;
    }

    @Nullable
    public CrockPotCookingRecipe.Wrapper getRecipeWrapper() {
        var size = itemHandlerInput.size();
        var stacks = new ArrayList<ItemStack>(size);
        for (var i = 0; i < size; i++) {
            var stackInSlot = getStackInSlot(i);
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

    public static boolean isFuel(ItemStack pStack, Level pLevel) {
        return pStack.getBurnTime(null, pLevel.fuelValues()) > 0;
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
        for (int i = 0; i < itemHandlerInput.size(); i++) {
            var stack = getStackInSlot(i);
            stack.shrink(1);
            setStackInSlot(i, stack);
        }
    }

    private void setStackInSlot(int slot, ItemStack stack) {
        if (stack.isEmpty()) {
            itemHandler.set(slot, ItemResource.EMPTY, 0);
        } else {
            itemHandler.set(slot, ItemResource.of(stack), stack.getCount());
        }
    }

    private int getActualCookingTotalTime(CrockPotCookingRecipe recipe) {
        return Math.max((int) (recipe.getCookingTime() * (1.0 - Config.CROCK_POT_SPEED_MODIFIER.get() * this.getPotLevel())), 1);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        itemHandler.deserialize(input.childOrEmpty("ItemHandler"));
        burningTime = input.getIntOr("BurningTime", 0);
        burningTotalTime = input.getIntOr("BurningTotalTime", 0);
        cookingTime = input.getIntOr("CookingTime", 0);
        cookingTotalTime = input.getIntOr("CookingTotalTime", 0);
        result = input.read("Result", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        itemHandler.serialize(output.child("ItemHandler"));
        output.putInt("BurningTime", burningTime);
        output.putInt("BurningTotalTime", burningTotalTime);
        output.putInt("CookingTime", cookingTime);
        output.putInt("CookingTotalTime", cookingTotalTime);
        output.store("Result", ItemStack.OPTIONAL_CODEC, result);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    private void markUpdated() {
        this.setChanged();
        if (level != null) {
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
        }
    }

    public void startOpen(Player pPlayer) {
        if (!remove && !pPlayer.isSpectator()) {
            openersCounter.incrementOpeners(pPlayer, level, this.getBlockPos(), this.getBlockState(), pPlayer.blockInteractionRange());
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
        var direction = pState.getValue(CrockPotBlock.FACING);
        var d0 = (double) worldPosition.getX() + 0.5D + (double) direction.getStepX() / 2.0D;
        var d1 = (double) worldPosition.getY() + 0.5D + (double) direction.getStepY() / 2.0D;
        var d2 = (double) worldPosition.getZ() + 0.5D + (double) direction.getStepZ() / 2.0D;
        level.playSound(null, d0, d1, d2, pSound, SoundSource.BLOCKS, 0.5F, level.getRandom().nextFloat() * 0.1F + 0.9F);
    }

    public ResourceHandler<ItemResource> getItemHandlerForSide(@Nullable Direction side) {
        if (side == null) {
            return itemHandler;
        }
        return switch (side) {
            case UP -> itemHandlerInput;
            case DOWN -> itemHandlerOutput;
            default -> itemHandlerFuel;
        };
    }
}
