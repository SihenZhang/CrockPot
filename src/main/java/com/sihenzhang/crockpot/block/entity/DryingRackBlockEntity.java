package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.block.DryingRackBlock;
import com.sihenzhang.crockpot.recipe.DryingRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Clearable;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.Arrays;

public class DryingRackBlockEntity extends BlockEntity implements Clearable {
    public static final int SLOTS_PER_STACK = 2;
    public static final int MAX_SLOTS = 4;

    private final NonNullList<ItemStack> items = NonNullList.withSize(MAX_SLOTS, ItemStack.EMPTY);
    private final int[] dryingTime = new int[MAX_SLOTS];
    private final int[] dryingTotalTime = new int[MAX_SLOTS];

    public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DRYING_RACK_BLOCK_ENTITY.get(), pos, blockState);
    }

    public static void serverTick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity blockEntity) {
        if (!DryingRackBlock.canDry(level, pos, state)) {
            return;
        }

        var changed = false;
        for (var i = 0; i < blockEntity.getActiveSlotCount(); i++) {
            var stack = blockEntity.items.get(i);
            if (stack.isEmpty() || blockEntity.dryingTotalTime[i] <= 0) {
                continue;
            }
            blockEntity.dryingTime[i]++;
            if (blockEntity.dryingTime[i] >= blockEntity.dryingTotalTime[i]) {
                var input = new SingleRecipeInput(stack);
                var result = DryingRecipe.getRecipeFor(stack, level)
                        .map(recipe -> recipe.value().assemble(input))
                        .filter(resultStack -> !resultStack.isEmpty())
                        .orElseGet(() -> stack.copyWithCount(1));
                blockEntity.items.set(i, result.copy());
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

    public float getDryingProgress(int slot) {
        if (items.get(slot).isEmpty()) {
            return 0.0F;
        }
        return dryingTotalTime[slot] > 0 ? (float) dryingTime[slot] / dryingTotalTime[slot] : 1.0F;
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
                dryingTime[i] = 0;
                dryingTotalTime[i] = recipe.getDryingTime();
                markUpdated();
                return true;
            }
        }
        return false;
    }

    public boolean collectReadyItems(Player player) {
        var collected = false;
        for (var i = 0; i < getActiveSlotCount(); i++) {
            if (!isReady(i)) {
                continue;
            }
            var stack = items.get(i);
            player.getInventory().placeItemBackInInventory(stack.copy());
            items.set(i, ItemStack.EMPTY);
            dryingTime[i] = 0;
            dryingTotalTime[i] = 0;
            collected = true;
        }
        if (collected) {
            markUpdated();
        }
        return collected;
    }

    public boolean isReady(int slot) {
        return !items.get(slot).isEmpty() && dryingTotalTime[slot] == 0;
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.items.clear();
        ContainerHelper.loadAllItems(input, items);
        input.getIntArray("DryingTimes")
                .ifPresentOrElse(
                        times -> System.arraycopy(times, 0, dryingTime, 0, Math.min(dryingTime.length, times.length)),
                        () -> Arrays.fill(dryingTime, 0)
                );
        input.getIntArray("DryingTotalTimes")
                .ifPresentOrElse(
                        times -> System.arraycopy(times, 0, dryingTotalTime, 0, Math.min(dryingTotalTime.length, times.length)),
                        () -> Arrays.fill(dryingTotalTime, 0)
                );
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        ContainerHelper.saveAllItems(output, items, true);
        output.putIntArray("DryingTimes", dryingTime);
        output.putIntArray("DryingTotalTimes", dryingTotalTime);
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

    @Override
    public void clearContent() {
        items.clear();
    }

    @Override
    public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level != null) {
            Containers.dropContents(level, pos, items);
        }
    }

    @Override
    protected void applyImplicitComponents(DataComponentGetter components) {
        super.applyImplicitComponents(components);
        components.getOrDefault(DataComponents.CONTAINER, ItemContainerContents.EMPTY).copyInto(this.items);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(this.items));
    }

    @Override
    public void removeComponentsFromTag(ValueOutput output) {
        output.discard(ContainerHelper.TAG_ITEMS);
    }
}
