package com.sihenzhang.crockpot.tile;

public final class CrockPotContext {
    boolean isBurning, shouldContinueTick;
    CrockPotState nextState;

    void continueNext(CrockPotState nextState) {
        this.nextState = nextState;
        shouldContinueTick = true;
    }

    void endTick(CrockPotState nextState) {
        this.nextState = nextState;
        shouldContinueTick = false;
    }
}
