package com.sihenzhang.crockpot.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.WeightedRandom;

public class WeightedItem extends WeightedRandom.Item {
    public final Item item;
    public final int min;
    public final int max;

    public WeightedItem(Item item, int min, int max, int itemWeight) {
        super(itemWeight);
        this.item = item;
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }

    public WeightedItem(Item item, int count, int itemWeight) {
        this(item, count, count, itemWeight);
    }

    public WeightedItem(Item item, int itemWeight) {
        this(item, 1, itemWeight);
    }

    public boolean isRanged() {
        return this.min != this.max;
    }

    public boolean isEmpty() {
        return this.item == null || this.item == Items.AIR || (this.min <= 0 && this.max <= 0) || this.weight <= 0;
    }
}
