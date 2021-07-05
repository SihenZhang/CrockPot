package com.sihenzhang.crockpot.recipe;

import net.minecraft.item.Item;
import net.minecraft.util.WeightedRandom;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        WeightedItem that = (WeightedItem) o;
        return min == that.min && max == that.max && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, min, max);
    }
}
