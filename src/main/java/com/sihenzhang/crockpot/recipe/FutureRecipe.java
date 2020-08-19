package com.sihenzhang.crockpot.recipe;

public class FutureRecipe {
    private volatile boolean done = false;
    private volatile Recipe result;

    void setResult(Recipe r) {
        synchronized (this) {
            this.result = r;
            done = true;
        }
    }

    public boolean isDone() {
        synchronized (this) {
            return done;
        }
    }

    public Recipe get() {
        synchronized (this) {
            return result;
        }
    }
}
