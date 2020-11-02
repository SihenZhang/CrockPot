package com.sihenzhang.crockpot.base;

import java.util.EnumMap;
import java.util.List;

public class FoodValueSum {
    protected EnumMap<FoodCategory, Float> foodValue = new EnumMap<>(FoodCategory.class);

    public FoodValueSum(List<EnumMap<FoodCategory, Float>> values) {
        values.forEach(i -> i.forEach((k, v) -> foodValue.put(k, v + foodValue.getOrDefault(k, 0F))));
    }

    public float getFoodValue(FoodCategory category) {
        return foodValue.getOrDefault(category, 0F);
    }
}
