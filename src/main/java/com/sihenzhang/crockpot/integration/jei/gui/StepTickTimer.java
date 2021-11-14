package com.sihenzhang.crockpot.integration.jei.gui;

import com.google.common.base.Preconditions;
import mezz.jei.api.gui.ITickTimer;

public class StepTickTimer implements ITickTimer {
    private final int msPerCycle;
    private final int steps;
    private final int maxValue;
    private final boolean countDown;
    private final long startTime;

    public StepTickTimer(int ticksPerCycle, int steps, int maxValue, boolean countDown) {
        Preconditions.checkArgument(ticksPerCycle > 0, "Must have at least 1 tick per cycle.");
        Preconditions.checkArgument(steps > 0, "Must have at least 1 step per cycle.");
        Preconditions.checkArgument(maxValue > 0, "max value must be greater than 0");
        this.msPerCycle = ticksPerCycle * 50;
        this.steps = steps;
        this.maxValue = maxValue / steps * steps;
        this.countDown = countDown;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public int getValue() {
        long currentTime = System.currentTimeMillis();
        long msPassed = (currentTime - startTime) % msPerCycle;
        int stepSize = maxValue / steps;
        int value = ((int) ((double) msPassed / msPerCycle * steps)) * stepSize;
        if (countDown) {
            return maxValue - value - stepSize;
        } else {
            return value;
        }
    }

    @Override
    public int getMaxValue() {
        return maxValue;
    }
}
