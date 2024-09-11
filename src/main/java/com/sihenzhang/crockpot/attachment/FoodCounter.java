package com.sihenzhang.crockpot.attachment;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

import java.util.Map;

public class FoodCounter {
    public static final Codec<FoodCounter> CODEC = Codec.unboundedMap(BuiltInRegistries.ITEM.byNameCodec(), Codec.INT).xmap(map -> Util.make(new FoodCounter(), foodCounter -> map.forEach(foodCounter::setCount)), FoodCounter::asMap);

    private final Multiset<Item> counter = HashMultiset.create();

    public boolean hasEaten(Item food) {
        return counter.contains(food);
    }

    public void addFood(Item food) {
        counter.add(food);
    }

    public int getCount(Item food) {
        return counter.count(food);
    }

    public void setCount(Item food, int count) {
        counter.setCount(food, count);
    }

    public void clear() {
        counter.clear();
    }

    public Map<Item, Integer> asMap() {
        return Maps.asMap(counter.elementSet(), counter::count);
    }
}
