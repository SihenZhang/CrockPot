package com.sihenzhang.crockpot.tile;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.RecipeInput;

import java.util.Objects;
import java.util.function.BiConsumer;

public enum CrockPotState {
    IDLE(CrockPotState::processIdle),
    WAITING_MATCHING(CrockPotState::processWaitingMatching),
    PROCESSING(CrockPotState::processProcessing);

    private final BiConsumer<CrockPotTileEntity, CrockPotContext> process;

    CrockPotState(BiConsumer<CrockPotTileEntity, CrockPotContext> process) {
        this.process = process;
    }

    /**
     * Update CrockPot
     *
     * @param state Current CrockPot state
     * @param tile  CrockPot TileEntity to be ticked
     * @return Next CrockPot state
     */
    static CrockPotState doPotTick(CrockPotState state, CrockPotTileEntity tile) {
        CrockPotContext ctx = new CrockPotContext();
        if (tile.burnTime <= 1 && state == PROCESSING) {
            tile.consumeFuel();
            tile.updateBurningState();
            tile.sync();
        }
        // Pre processing
        if (tile.burnTime > 0) {
            tile.burnTime--;
            ctx.isBurning = true;
            // Well no one will notice the state is changed one tick before
            if (tile.burnTime == 0) {
                tile.updateBurningState();
            }
        }

        // State processing
        CrockPotState lastState = ctx.nextState;

        state.process.accept(tile, ctx);
        while (ctx.shouldContinueTick) {
            if (ctx.nextState == null) {
                throw new IllegalStateException("Next state should not be null, last state is " + lastState.name());
            }
            lastState = tile.currentState = ctx.nextState;
            ctx.nextState.process.accept(tile, ctx);
        }

        return ctx.nextState;
    }

    private static void processIdle(CrockPotTileEntity tile, CrockPotContext ctx) {
        if (!tile.itemHandler.getStackInSlot(5).isEmpty()) {
            ctx.endTick(IDLE);
            return;
        }
        // Do not match recipe if fuel is empty
        if (!ctx.isBurning && tile.itemHandler.getStackInSlot(4).isEmpty()) {
            ctx.endTick(IDLE);
            return;
        }
        if (tile.shouldDoMatch) {
            tile.shouldDoMatch = false;
            RecipeInput input = tile.getRecipeInput();
            if (input != null) {
                if (!Objects.requireNonNull(tile.getWorld()).isRemote) {
                    tile.pendingRecipe = CrockPot.RECIPE_MANAGER.match(input);
                }
                ctx.continueNext(WAITING_MATCHING);
                return;
            }
        }
        ctx.endTick(IDLE);
    }

    private static void processWaitingMatching(CrockPotTileEntity tile, CrockPotContext ctx) {
        // If the game stops when the pot is waiting for a match result
        if (tile.pendingRecipe == null) {
            if (Objects.requireNonNull(tile.getWorld()).isRemote) {
                tile.shouldDoMatch = false;
                ctx.endTick(WAITING_MATCHING);
                return;
            }
            if (ctx.isBurning) {
                tile.burnTime++;
            }
            tile.shouldDoMatch = true;
            ctx.endTick(IDLE);
            return;
        }

        // Cancel current matching if input changed
        if (tile.shouldDoMatch) {
            tile.pendingRecipe.cancel(true);
            ctx.endTick(CrockPotState.IDLE);
            return;
        }

        if (tile.pendingRecipe.isDone()) {
            tile.currentRecipe = tile.pendingRecipe.join();
            if (tile.currentRecipe.isEmpty()) {
                ctx.endTick(IDLE);
            } else {
                tile.shrinkInputs();
                ctx.continueNext(PROCESSING);
                ctx.needSync = true;
            }
            tile.markDirty();
        } else {
            if (ctx.isBurning) {
                tile.burnTime++;
            }
            ctx.endTick(WAITING_MATCHING);
        }
    }

    private static void processProcessing(CrockPotTileEntity tile, CrockPotContext ctx) {
        if (ctx.needSync) {
            tile.sync();
        }
        // Process
        tile.processTime++;
        tile.markDirty();
        if (tile.processTime >= tile.currentRecipe.getCookTime()) {
            tile.itemHandler.setStackInSlot(5, tile.currentRecipe.getResult().copy());
            tile.processTime = 0;
            ctx.endTick(IDLE);
        } else {
            ctx.endTick(PROCESSING);
        }
    }
}
