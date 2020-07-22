package com.sihenzhang.crockpot.tile;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.CrockPotIngredient;
import com.sihenzhang.crockpot.base.IngredientSum;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.recipe.Recipe;
import com.sihenzhang.crockpot.recipe.RecipeInput;
import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import mcp.MethodsReturnNonnullByDefault;
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

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
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
                if (!isItemFuel(stack)) {
                    return stack;
                }
            }
            inputChanged = true;
            return super.insertItem(slot, stack, simulate);
        }

        @Nonnull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            if (slot == 5) inputChanged = true;
            return super.extractItem(slot, amount, simulate);
        }

        @Override
        public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
            if (slot < 4)
                inputChanged = true;
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

    private int burnTime;
    private int currentItemBurnTime;
    private int processTime;

    private boolean inputChanged = false;

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

    private Recipe currentRecipe;

    @Override
    public void tick() {
        assert world != null;
        if (world.isRemote) return;
        boolean burning = false;
        if (burnTime > 0) {
            burning = true;
            --burnTime;
            sync();
        } else if (processTime > 0) {
            if (this.itemHandler.getStackInSlot(4).isEmpty())
                processTime = 0;
            sync();
        }
        if (!itemHandler.getStackInSlot(5).isEmpty()) return;
        if (currentRecipe == null) {
            if (inputChanged) {
                inputChanged = false;
                List<ItemStack> stacks = new ArrayList<>(4);
                List<CrockPotIngredient> ingredients = new ArrayList<>(4);
                for (int i = 0; i < 4; ++i) {
                    ItemStack stack = itemHandler.getStackInSlot(i);
                    if (!stack.isEmpty()) {
                        ingredients.add(CrockPot.INGREDIENT_MANAGER.getIngredientFromItem(stack.getItem()));
                        ItemStack copy = stack.copy();
                        copy.setCount(1);
                        stacks.add(copy);
                    } else {
                        return;
                    }
                }
                CrockPotBlock block = (CrockPotBlock) getBlockState().getBlock();
                RecipeInput input = new RecipeInput(new IngredientSum(ingredients), stacks, block.getPotLevel());
                this.currentRecipe = CrockPot.RECIPE_MANAGER.match(input);
                if (this.currentRecipe != null) {
                    if (this.burnTime <= 0 && itemHandler.getStackInSlot(4).isEmpty()) return;
                    for (int i = 0; i < 4; ++i) {
                        itemHandlerInput.getStackInSlot(i).shrink(1);
                    }
                }
            }
        } else {
            if (burning) {
                ++this.processTime;
            } else {
                ItemStack fuelStack = itemHandler.getStackInSlot(4);
                if (!fuelStack.isEmpty()) {
                    ItemStack copy = fuelStack.copy();
                    copy.setCount(1);
                    currentItemBurnTime = ForgeHooks.getBurnTime(copy);
                    if (currentItemBurnTime > 0) {
                        fuelStack.shrink(1);
                        this.burnTime += currentItemBurnTime;
                        --this.burnTime;
                        ++this.processTime;
                    }
                }
            }
            if (processTime >= currentRecipe.getCookTime()) {
                processTime = 0;
                this.itemHandler.setStackInSlot(5, this.currentRecipe.getResult().copy());
                this.currentRecipe = null;
                sync();
            }

        }
    }

    private void sync() {
        SUpdateTileEntityPacket pkt = getUpdatePacket();
        assert pkt != null;
        assert world != null;
        ((ServerWorld) world).getChunkProvider().chunkManager.getTrackingPlayers(new ChunkPos(pos), false)
                .forEach(p -> p.connection.sendPacket(pkt));
        markDirty();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, this.serializeNBT());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        deserializeNBT(pkt.getNbtCompound());
    }

    public static boolean isItemFuel(ItemStack itemStack) {
        return getBurnTime(itemStack) > 0;
    }

    protected static int getBurnTime(ItemStack itemStack) {
        return ForgeHooks.getBurnTime(itemStack);
    }

    public static boolean isValidIngredient(ItemStack itemStack) {
        return CrockPot.INGREDIENT_MANAGER.getIngredientFromItem(itemStack.getItem()) != null;
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        itemHandler.deserializeNBT(compound.getCompound("ItemHandler"));
        burnTime = compound.getShort("BurnTime");
        currentItemBurnTime = compound.getShort("CurrentItemBurnTime");
        processTime = compound.getShort("ProcessTime");
        if (compound.contains("currentRecipe"))
            currentRecipe = new Recipe((CompoundNBT) Objects.requireNonNull(compound.get("currentRecipe")));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        super.write(compound);
        compound.put("ItemHandler", itemHandler.serializeNBT());
        compound.putShort("BurnTime", (short) burnTime);
        compound.putShort("CurrentItemBurnTime", (short) currentItemBurnTime);
        compound.putShort("ProcessTime", (short) processTime);
        if (currentRecipe != null)
            compound.put("currentRecipe", currentRecipe.serializeNBT());
        return compound;
    }

    public boolean isBurning() {
        return burnTime > 0;
    }

    public float getBurnTimeProgress() {
        return (float) burnTime / (float) currentItemBurnTime;
    }

    public boolean isProcessing() {
        return processTime > 0;
    }

    public float getProcessTimeProgress() {
        if (currentRecipe == null) return 0F;
        return (float) processTime / currentRecipe.getCookTime();
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
