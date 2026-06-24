package com.sihenzhang.crockpot.core;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.registry.FoodCategory;
import it.unimi.dsi.fastutil.objects.Object2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMaps;
import net.minecraft.core.Holder;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public final class FoodValues {
    public static final Codec<FoodValues> CODEC = Codec.unboundedMap(FoodCategory.CODEC, Codec.FLOAT).xmap(
            FoodValues::of, FoodValues::asMap
    );

    private final Object2FloatMap<Holder<FoodCategory>> delegate = new Object2FloatLinkedOpenHashMap<>();

    private FoodValues() {
    }

    public static FoodValues create() {
        return new FoodValues();
    }

    public static FoodValues of(Map<Holder<FoodCategory>, Float> map) {
        final FoodValues foodValues = create();
        if (map != null) {
            map.forEach(foodValues::put);
        }
        return foodValues;
    }

    public float get(Holder<FoodCategory> category) {
        if (category == null) {
            return 0.0F;
        }
        return Math.max(delegate.getFloat(category), 0.0F);
    }

    public boolean has(Holder<FoodCategory> category) {
        return delegate.containsKey(category) && delegate.getFloat(category) > 0.0F;
    }

    public void put(Holder<FoodCategory> category, float value) {
        if (category == null) {
            return;
        }
        if (Float.isNaN(value) || value <= 0.0F) {
            this.remove(category);
            return;
        }
        delegate.put(category, value);
    }

    public void remove(Holder<FoodCategory> category) {
        if (category == null) {
            return;
        }
        delegate.removeFloat(category);
    }

    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    public int size() {
        return delegate.size();
    }

    public void clear() {
        delegate.clear();
    }

    public Set<Map.Entry<Holder<FoodCategory>, Float>> entrySet() {
        return asMap().entrySet();
    }

    public Map<Holder<FoodCategory>, Float> asMap() {
        return Object2FloatMaps.unmodifiable(delegate);
    }

    public static FoodValues merge(Collection<FoodValues> values) {
        var merged = create();
        values.forEach(foodValues -> foodValues.entrySet().forEach(entry -> merged.put(entry.getKey(), merged.get(entry.getKey()) + entry.getValue())));
        return merged;
    }
}
