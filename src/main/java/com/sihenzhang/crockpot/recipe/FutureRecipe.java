package com.sihenzhang.crockpot.recipe;

public final class FutureRecipe {
    private volatile boolean done = false;
    private volatile Recipe result;

    synchronized void setResult(Recipe r) {
        if (done) throw new IllegalStateException();
        this.result = r;
        done = true;
    }

    public synchronized boolean isDone() {
        return done;
    }

    public synchronized Recipe get() {
        return result;
    }
}
