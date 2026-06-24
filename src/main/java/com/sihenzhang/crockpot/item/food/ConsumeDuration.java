package com.sihenzhang.crockpot.item.food;

import net.minecraft.world.item.component.Consumable;

public enum ConsumeDuration {
    SUPER_FAST(Consumable.DEFAULT_CONSUME_SECONDS - 0.8F),
    FAST(Consumable.DEFAULT_CONSUME_SECONDS - 0.4F),
    NORMAL(Consumable.DEFAULT_CONSUME_SECONDS),
    SLOW(Consumable.DEFAULT_CONSUME_SECONDS + 0.4F),
    SUPER_SLOW(Consumable.DEFAULT_CONSUME_SECONDS + 0.8F);

    public final float consumeSeconds;

    ConsumeDuration(float consumeSeconds) {
        this.consumeSeconds = consumeSeconds;
    }
}
