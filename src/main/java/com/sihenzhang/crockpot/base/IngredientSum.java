package com.sihenzhang.crockpot.base;

import java.util.EnumMap;
import java.util.List;

public class IngredientSum {
    protected EnumMap<CrockPotIngredientType, Float> ingredientValue = new EnumMap<>(CrockPotIngredientType.class);

    public IngredientSum(List<CrockPotIngredient> ingredients) {
        ingredients.forEach(
                i -> i.ingredientValue.keySet().forEach(
                        p -> ingredientValue.put(p, i.getIngredient(p) + ingredientValue.getOrDefault(p, 0F))
                )
        );
    }

    public float getIngredient(CrockPotIngredientType type) {
        return ingredientValue.getOrDefault(type, 0F);
    }
}
