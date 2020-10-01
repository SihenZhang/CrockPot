package com.sihenzhang.crockpot.item.food;

public enum FoodUseDuration {
    SUPER_FAST(16),
    FAST(24),
    NORMAL(32),
    SLOW(40),
    SUPER_SLOW(48);

    public final int val;

    FoodUseDuration(int val) {
        this.val = val;
    }
}
