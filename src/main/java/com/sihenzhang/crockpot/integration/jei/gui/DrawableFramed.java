package com.sihenzhang.crockpot.integration.jei.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import mezz.jei.api.gui.ITickTimer;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;

public class DrawableFramed implements IDrawableAnimated {
    private final IDrawableStatic drawable;
    private final int width;
    private final int height;
    private final ITickTimer tickTimer;
    private final StartDirection startDirection;

    public DrawableFramed(IDrawableStatic drawable, int ticksPerCycle, int frames, StartDirection startDirection) {
        boolean inverted = startDirection == StartDirection.BOTTOM || startDirection == StartDirection.RIGHT;

        int tickTimerMaxValue, width, height;
        switch (startDirection) {
            case TOP:
            case BOTTOM:
                tickTimerMaxValue = drawable.getHeight();
                width = drawable.getWidth();
                height = drawable.getHeight() / frames;
                break;
            case LEFT:
            case RIGHT:
                tickTimerMaxValue = drawable.getWidth();
                width = drawable.getWidth() / frames;
                height = drawable.getHeight();
                break;
            default:
                throw new IllegalStateException("Unknown startDirection " + startDirection);
        }

        this.drawable = drawable;
        this.width = width;
        this.height = height;
        this.tickTimer = new StepTickTimer(ticksPerCycle, frames, tickTimerMaxValue, !inverted);
        this.startDirection = startDirection;
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
        int maskLeft = 0;
        int maskRight = 0;
        int maskTop = 0;
        int maskBottom = 0;

        int animationValue = tickTimer.getValue();
        int tickerTimerMaxValue = tickTimer.getMaxValue();
        int actualWidth = drawable.getWidth();
        int actualHeight = drawable.getHeight();
        switch (startDirection) {
            case TOP:
                maskTop = tickerTimerMaxValue - animationValue - height;
                maskBottom = animationValue + actualHeight - tickerTimerMaxValue;
                break;
            case BOTTOM:
                maskTop = actualHeight - animationValue - height;
                maskBottom = animationValue;
                break;
            case LEFT:
                maskLeft = tickerTimerMaxValue - animationValue - width;
                maskRight = animationValue + actualWidth - tickerTimerMaxValue;
                break;
            case RIGHT:
                maskLeft = actualWidth - animationValue - width;
                maskRight = animationValue;
                break;
            default:
                throw new IllegalStateException("Unknown startDirection " + startDirection);
        }

        drawable.draw(matrixStack, xOffset - maskLeft, yOffset - maskTop, maskTop, maskBottom, maskLeft, maskRight);
    }
}
