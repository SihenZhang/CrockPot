package com.sihenzhang.crockpot.base;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.registry.FoodCategory;
import it.unimi.dsi.fastutil.objects.Object2FloatLinkedOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatMaps;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class FoodValues {
    public static final Codec<FoodValues> CODEC = Codec.unboundedMap(FoodCategory.CODEC, Codec.FLOAT).xmap(FoodValues::of, FoodValues::asMap);
    public static final StreamCodec<RegistryFriendlyByteBuf, FoodValues> STREAM_CODEC = ByteBufCodecs.<RegistryFriendlyByteBuf, Holder<FoodCategory>, Float, Map<Holder<FoodCategory>, Float>>map(
            Object2FloatLinkedOpenHashMap::new, FoodCategory.STREAM_CODEC, ByteBufCodecs.FLOAT
    ).map(FoodValues::of, FoodValues::asMap);

    private final Object2FloatMap<Holder<FoodCategory>> values = new Object2FloatLinkedOpenHashMap<>();

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

    public static FoodValues merge(Collection<FoodValues> foodValues) {
        final FoodValues mergedFoodValues = create();
        foodValues.forEach(foodValue -> foodValue.entrySet().forEach(entry ->
                mergedFoodValues.put(entry.getKey(), entry.getFloatValue() + mergedFoodValues.get(entry.getKey()))));
        return mergedFoodValues;
    }

    public FoodValues set(Holder<FoodCategory> category, float value) {
        this.put(category, value);
        return this;
    }

    public float get(Holder<FoodCategory> category) {
        if (category == null) {
            return 0.0F;
        }
        return Math.max(values.getFloat(category), 0.0F);
    }

    public float get(ResourceKey<FoodCategory> category) {
        if (category == null) {
            return 0.0F;
        }
        for (var entry : values.object2FloatEntrySet()) {
            if (entry.getKey().is(category)) {
                return Math.max(entry.getFloatValue(), 0.0F);
            }
        }
        return 0.0F;
    }

    public boolean has(Holder<FoodCategory> category) {
        return this.get(category) > 0.0F;
    }

    public boolean has(ResourceKey<FoodCategory> category) {
        return this.get(category) > 0.0F;
    }

    public void put(Holder<FoodCategory> category, float value) {
        if (category == null) {
            return;
        }
        if (Float.isNaN(value) || value <= 0.0F) {
            this.remove(category);
            return;
        }
        values.put(category, value);
    }

    public void remove(Holder<FoodCategory> category) {
        if (category == null) {
            return;
        }
        values.removeFloat(category);
    }

    public void clear() {
        values.clear();
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }

    public int size() {
        return values.size();
    }

    public Set<Object2FloatMap.Entry<Holder<FoodCategory>>> entrySet() {
        return this.asMap().object2FloatEntrySet();
    }

    public Object2FloatMap<Holder<FoodCategory>> asMap() {
        return Object2FloatMaps.unmodifiable(values);
    }

    public static FoodValues fromNetwork(RegistryFriendlyByteBuf buffer) {
        return STREAM_CODEC.decode(buffer);
    }

    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }
}
