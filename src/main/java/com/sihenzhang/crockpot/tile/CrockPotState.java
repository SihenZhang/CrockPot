package com.sihenzhang.crockpot.tile;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.RecipeInput;

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
        state.process.accept(tile, ctx);
        while (ctx.shouldContinueTick) {
            ctx.nextState.process.accept(tile, ctx);
        }

        return ctx.nextState;
    }

    private static void processIdle(CrockPotTileEntity tile, CrockPotContext ctx) {
        if (!tile.itemHandler.getStackInSlot(5).isEmpty()) {
            ctx.endTick(IDLE);
            return;
        }
        if (tile.shouldDoMatch) {
            tile.shouldDoMatch = false;
            RecipeInput input = tile.getRecipeInput();
            if (input != null) {
                tile.pendingRecipe = CrockPot.RECIPE_MANAGER.match(input);
                ctx.endTick(WAITING_MATCHING);
                return;
            }
        }
        ctx.endTick(IDLE);
    }

    private static void processWaitingMatching(CrockPotTileEntity tile, CrockPotContext ctx) {
        // Cancel current matching if input changed
        if (tile.shouldDoMatch) {
            if (tile.getRecipeInput() == null) {
                tile.pendingRecipe.cancel(true);
                ctx.endTick(CrockPotState.IDLE);
                return;
            }
        }

        if (tile.pendingRecipe == null) {
            if (ctx.isBurning) tile.burnTime++;
            tile.shouldDoMatch = true;
            ctx.endTick(IDLE);
            return;
        }

        if (tile.pendingRecipe.isDone()) {
            tile.currentRecipe = tile.pendingRecipe.join();
            if (tile.currentRecipe.isEmpty()) {
                ctx.endTick(IDLE);
            } else {
                tile.shrinkInputs();
                ctx.continueNext(PROCESSING);
                tile.sync();
            }
            tile.markDirty();
        } else {
            if (ctx.isBurning) tile.burnTime++;
            ctx.endTick(WAITING_MATCHING);
        }
    }

    private static void processProcessing(CrockPotTileEntity tile, CrockPotContext ctx) {
        // Consume fuel
        if (!ctx.isBurning) {
            tile.consumeFuel();
            if (tile.burnTime > 0) {
                tile.burnTime--;
                tile.updateBurningState();
                ctx.isBurning = true;
            } else {
                tile.processTime = 0;
                ctx.endTick(PROCESSING);
                return;
            }
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
