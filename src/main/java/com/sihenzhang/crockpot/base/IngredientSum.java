package com.sihenzhang.crockpot.base;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class IngredientSum {
    protected Map<CrockPotIngredientType, Float> ingredientValue = new HashMap<>();

    public IngredientSum(CrockPotIngredient... ingredients) {
        Arrays.stream(ingredients).forEach(
                i -> i.ingredientValue.keySet().forEach(
                        p -> ingredientValue.put(p, i.ingredientValue.getOrDefault(p, 0F) + ingredientValue.getOrDefault(p, 0F))
                )
        );
    }

    public void add(CrockPotIngredient ingredient) {
        ingredient.ingredientValue.keySet().forEach(
                p -> ingredientValue.put(p, ingredient.ingredientValue.getOrDefault(p, 0F) + ingredientValue.getOrDefault(p, 0F))
        );
    }

    public float getIngredient(CrockPotIngredientType type) {
        return ingredientValue.getOrDefault(type, 0F);
    }
}
