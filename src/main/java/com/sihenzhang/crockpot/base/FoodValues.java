package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.JSONUtils;
import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class FoodValues {
    private static final FoodCategory[] CATEGORIES = FoodCategory.values();
    private final float[] values = new float[CATEGORIES.length];
    private int size;

    private FoodValues() {
    }

    public static FoodValues create() {
        return new FoodValues();
    }

    @SuppressWarnings("unchecked")
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
        foodValues.forEach(foodValue -> {
            for (int i = 0; i < mergedFoodValues.values.length; i++) {
                mergedFoodValues.values[i] += foodValue.values[i];
            }
        });
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
        return values[category.ordinal()];
    }

    public boolean has(FoodCategory category) {
        if (category == null) {
            return false;
        }
        return values[category.ordinal()] > 0.0F;
    }

    public void put(FoodCategory category, float value) {
        if (category == null || Float.isNaN(value) || value <= 0.0F) {
            return;
        }
        boolean hasOldValue = this.has(category);
        values[category.ordinal()] = value;
        if (!hasOldValue) {
            size++;
        }
    }

    public void remove(FoodCategory category) {
        if (category == null) {
            return;
        }
        boolean hasOldValue = this.has(category);
        values[category.ordinal()] = 0.0F;
        if (hasOldValue) {
            size--;
        }
    }

    public void clear() {
        Arrays.fill(values, 0.0F);
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public Set<Pair<FoodCategory, Float>> entrySet() {
        ImmutableSet.Builder<Pair<FoodCategory, Float>> builder = ImmutableSet.builder();
        for (int i = 0; i < values.length; i++) {
            if (this.has(CATEGORIES[i])) {
                builder.add(Pair.of(CATEGORIES[i], values[i]));
            }
        }
        return builder.build();
    }

    public static FoodValues fromJson(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            throw new JsonSyntaxException("Json cannot be null");
        }
        if (!json.isJsonObject()) {
            throw new JsonSyntaxException("Expected food value to be an object, was " + JSONUtils.getType(json));
        }
        final FoodValues foodValues = create();
        JsonObject obj = json.getAsJsonObject();
        obj.entrySet().forEach(entry -> {
            String category = entry.getKey();
            if (!EnumUtils.isValidEnum(FoodCategory.class, category.toUpperCase())) {
                throw new JsonSyntaxException("Expected the key of food value to be a food category, was unknown food category name: '" + category + "'");
            }
            if (!JSONUtils.isNumberValue(entry.getValue())) {
                throw new JsonSyntaxException("Expected the value of food value to be a number, was " + JSONUtils.getType(entry.getValue()));
            }
            foodValues.put(FoodCategory.valueOf(category.toUpperCase()), entry.getValue().getAsFloat());
        });
        return foodValues;
    }

    public JsonElement toJson() {
        final JsonObject obj = new JsonObject();
        for (int i = 0; i < values.length; i++) {
            if (this.has(CATEGORIES[i])) {
                obj.addProperty(CATEGORIES[i].name(), values[i]);
            }
        }
        return obj;
    }
}
