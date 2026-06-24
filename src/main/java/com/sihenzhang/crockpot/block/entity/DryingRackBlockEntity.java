package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.block.DryingRackBlock;
import com.sihenzhang.crockpot.recipe.DryingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;

public class DryingRackBlockEntity extends BlockEntity {
    public static final int SLOTS_PER_STACK = 2;
    public static final int MAX_SLOTS = 4;

    private final NonNullList<ItemStack> items = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);
    private final NonNullList<ItemStack> results = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);
    private final int[] dryingTime = new int[MAX_SLOTS];
    private final int[] dryingTotalTime = new int[MAX_SLOTS];

    public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(CrockPotBlockEntities.DRYING_RACK_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity blockEntity) {
        if (!DryingRackBlock.canDry(level, pos, state)) {
            return;
        }

        var changed = false;
        for (var i = 0; i < blockEntity.getActiveSlotCount(); i++) {
            if (blockEntity.items.get(i).isEmpty() || blockEntity.results.get(i).isEmpty()) {
                continue;
            }
            blockEntity.dryingTime[i]++;
            if (blockEntity.dryingTime[i] >= blockEntity.dryingTotalTime[i]) {
                blockEntity.items.set(i, blockEntity.results.get(i));
                blockEntity.results.set(i, ItemStack.EMPTY);
                blockEntity.dryingTime[i] = 0;
                blockEntity.dryingTotalTime[i] = 0;
            }
            changed = true;
        }

        if (changed) {
            blockEntity.markUpdated();
        }
    }

    public int getActiveSlotCount() {
        return DryingRackBlock.getSlotCount(this.getBlockState());
    }

    public ItemStack getItem(int slot) {
        return items.get(slot);
    }

    public boolean hasEmptySlot() {
        for (var i = 0; i < getActiveSlotCount(); i++) {
            if (items.get(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public boolean hasReadyItems() {
        for (var i = 0; i < getActiveSlotCount(); i++) {
            if (isReady(i)) {
                return true;
            }
        }
        return false;
    }

    public boolean addItem(ItemStack input, DryingRecipe recipe) {
        if (input.isEmpty()) {
            return false;
        }
        var result = recipe.assemble(new SingleRecipeInput(input));
        if (result.isEmpty()) {
            return false;
        }
        for (var i = 0; i < getActiveSlotCount(); i++) {
            if (items.get(i).isEmpty()) {
                items.set(i, input.copyWithCount(1));
                results.set(i, result.copyWithCount(1));
                dryingTime[i] = 0;
                dryingTotalTime[i] = recipe.getDryingTime();
                markUpdated();
                return true;
            }
        }
        return false;
    }

    public boolean collectReadyItems(Player player, InteractionHand hand) {
        var collected = false;
        for (var i = 0; i < getActiveSlotCount(); i++) {
            if (!isReady(i)) {
                continue;
            }
            var stack = items.get(i);
            giveToPlayer(player, hand, stack.copy());
            items.set(i, ItemStack.EMPTY);
            collected = true;
        }
        if (collected) {
            markUpdated();
        }
        return collected;
    }

    public void dropContents(Level level, BlockPos pos) {
        for (var stack : items) {
            if (!stack.isEmpty()) {
                Containers.dropContents(level, pos, new SimpleContainer(stack));
            }
        }
    }

    private boolean isReady(int slot) {
        return !items.get(slot).isEmpty() && results.get(slot).isEmpty() && dryingTotalTime[slot] == 0;
    }

    private static void giveToPlayer(Player player, InteractionHand hand, ItemStack stack) {
        var handStack = player.getItemInHand(hand);
        if (handStack.isEmpty()) {
            player.setItemInHand(hand, stack);
            return;
        }
        if (ItemStack.isSameItemSameComponents(handStack, stack) && handStack.getCount() < handStack.getMaxStackSize()) {
            var transfer = Math.min(stack.getCount(), handStack.getMaxStackSize() - handStack.getCount());
            handStack.grow(transfer);
            stack.shrink(transfer);
        }
        if (!stack.isEmpty() && !player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        items.replaceAll(ignored -> ItemStack.EMPTY);
        results.replaceAll(ignored -> ItemStack.EMPTY);
        for (var i = 0; i < MAX_SLOTS; i++) {
            dryingTime[i] = 0;
            dryingTotalTime[i] = 0;
        }
        input.childrenListOrEmpty("Items").forEach(child -> {
            var slot = child.getIntOr("Slot", -1);
            if (slot < 0 || slot >= MAX_SLOTS) {
                return;
            }
            items.set(slot, child.read("Item", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY));
            results.set(slot, child.read("Result", ItemStack.OPTIONAL_CODEC).orElse(ItemStack.EMPTY));
            dryingTime[slot] = child.getIntOr("DryingTime", 0);
            dryingTotalTime[slot] = child.getIntOr("DryingTotalTime", 0);
        });
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        var list = output.childrenList("Items");
        for (var i = 0; i < MAX_SLOTS; i++) {
            if (items.get(i).isEmpty()) {
                continue;
            }
            var child = list.addChild();
            child.putInt("Slot", i);
            child.store("Item", ItemStack.OPTIONAL_CODEC, items.get(i));
            child.store("Result", ItemStack.OPTIONAL_CODEC, results.get(i));
            child.putInt("DryingTime", dryingTime[i]);
            child.putInt("DryingTotalTime", dryingTotalTime[i]);
        }
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
}
