package com.sihenzhang.crockpot.recipe;

public final class FutureRecipe {
    private volatile boolean done = false;
    private volatile Recipe result;

    synchronized void setResult(Recipe r) {
        if (done) throw new IllegalStateException();
        this.result = r;
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    public Recipe get() {
        if (!done) throw new IllegalStateException();
        return result;
    }
}
