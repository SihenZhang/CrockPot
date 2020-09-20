package com.sihenzhang.crockpot.base;

import java.util.EnumMap;
import java.util.List;

public class FoodValueSum {
    protected EnumMap<FoodCategory, Float> ingredientValue = new EnumMap<>(FoodCategory.class);

    public FoodValueSum(List<EnumMap<FoodCategory, Float>> values) {
        values.forEach(
                i -> i.keySet().forEach(
                        p -> ingredientValue.put(p, i.get(p) + ingredientValue.getOrDefault(p, 0F))
                )
        );
    }

    public float getIngredient(FoodCategory type) {
        return ingredientValue.getOrDefault(type, 0F);
    }
}
