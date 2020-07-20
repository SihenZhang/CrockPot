package com.sihenzhang.crockpot.base;

import java.util.Arrays;
import java.util.EnumMap;

public class IngredientSum {
    protected EnumMap<CrockPotIngredientType, Float> ingredientValue = new EnumMap<>(CrockPotIngredientType.class);

    public IngredientSum(CrockPotIngredient... ingredients) {
        Arrays.stream(ingredients).forEach(
                i -> i.ingredientValue.keySet().forEach(
                        p -> ingredientValue.put(p, i.getIngredient(p) + ingredientValue.getOrDefault(p, 0F))
                )
        );
    }

    public void add(CrockPotIngredient ingredient) {
        ingredient.ingredientValue.keySet().forEach(
                p -> ingredientValue.put(p, ingredient.getIngredient(p) + ingredientValue.getOrDefault(p, 0F))
        );
    }

    public float getIngredient(CrockPotIngredientType type) {
        return ingredientValue.getOrDefault(type, 0F);
    }
}
