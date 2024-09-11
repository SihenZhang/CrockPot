package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class FoodValues {
    private static final FoodCategory[] CATEGORIES = FoodCategory.values();
    public static final Codec<FoodValues> CODEC = Codec.unboundedMap(FoodCategory.CODEC, Codec.FLOAT).xmap(
            FoodValues::of, foodValues -> foodValues.entrySet().stream().collect(Collectors.toUnmodifiableMap(Pair::getKey, Pair::getValue))
    );
    public static final StreamCodec<FriendlyByteBuf, FoodValues> STREAM_CODEC = StreamCodec.of(
            FoodValues::toNetwork, FoodValues::fromNetwork
    );
    private final float[] values = new float[CATEGORIES.length];
    private int size;

    private FoodValues() {
    }

    public static FoodValues create() {
        return new FoodValues();
    }

    @SafeVarargs
    public static FoodValues of(Pair<FoodCategory, Float>... pairs) {
        final FoodValues foodValues = create();
        for (Pair<FoodCategory, Float> pair : pairs) {
            foodValues.put(pair.getKey(), pair.getValue());
        }
        return foodValues;
    }

    public static FoodValues of(Object... categoriesAndValues) {
        final FoodValues foodValues = create();
        FoodCategory category = null;
        for (int i = 0; i < categoriesAndValues.length; i++) {
            if (i % 2 == 0) {
                category = (FoodCategory) categoriesAndValues[i];
            } else {
                foodValues.put(category, (Float) categoriesAndValues[i]);
            }
        }
        return foodValues;
    }

    public static FoodValues of(Map<FoodCategory, Float> map) {
        final FoodValues foodValues = create();
        if (map != null) {
            map.forEach(foodValues::put);
        }
        return foodValues;
    }

    public static FoodValues merge(Collection<FoodValues> foodValues) {
        final FoodValues mergedFoodValues = create();
        foodValues.forEach(foodValue -> foodValue.entrySet().forEach(entry -> mergedFoodValues.put(entry.getKey(), entry.getValue() + mergedFoodValues.get(entry.getKey()))));
        return mergedFoodValues;
    }

    public FoodValues set(FoodCategory category, float value) {
        this.put(category, value);
        return this;
    }

    public float get(FoodCategory category) {
        if (category == null) {
            return 0.0F;
        }
        return Math.max(values[category.ordinal()], 0.0F);
    }

    public boolean has(FoodCategory category) {
        if (category == null) {
            return false;
        }
        return values[category.ordinal()] > 0.0F;
    }

    public void put(FoodCategory category, float value) {
        if (category == null) {
            return;
        }
        if (Float.isNaN(value) || value <= 0.0F) {
            this.remove(category);
            return;
        }
        boolean hasOldValue = this.has(category);
        values[category.ordinal()] = value;
        if (!hasOldValue) {
            size++;
        }
    }

    public void remove(FoodCategory category) {
        if (category == null || !this.has(category)) {
            return;
        }
        values[category.ordinal()] = 0.0F;
        size--;
    }

    public void clear() {
        Arrays.fill(values, 0.0F);
        size = 0;
    }

    public boolean isEmpty() {
        return size <= 0;
    }

    public int size() {
        return size;
    }

    public Set<Pair<FoodCategory, Float>> entrySet() {
        ImmutableSet.Builder<Pair<FoodCategory, Float>> builder = ImmutableSet.builder();
        for (int i = 0; i < values.length; i++) {
            if (values[i] > 0.0F) {
                builder.add(Pair.of(CATEGORIES[i], values[i]));
            }
        }
        return builder.build();
    }

    private static FoodValues fromNetwork(FriendlyByteBuf buffer) {
        final FoodValues foodValues = create();
        int length = buffer.readByte();
        for (int i = 0; i < length; i++) {
            FoodCategory category = buffer.readEnum(FoodCategory.class);
            float value = buffer.readFloat();
            foodValues.put(category, value);
        }
        return foodValues;
    }

    private static void toNetwork(FriendlyByteBuf buffer, FoodValues value) {
        Set<Pair<FoodCategory, Float>> entrySet = value.entrySet();
        buffer.writeByte(entrySet.size());
        entrySet.forEach(entry -> {
            buffer.writeEnum(entry.getKey());
            buffer.writeFloat(entry.getValue());
        });
    }
}
