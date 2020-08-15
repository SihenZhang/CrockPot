package com.sihenzhang.crockpot.recipe;

public class FutureRecipe {
    private boolean done = false;
    private Recipe result;

    void setResult(Recipe r) {
        this.result = r;
        done = true;
    }

    public boolean isDone() {
        return done;
    }

    public Recipe get() {
        return result;
    }
}
