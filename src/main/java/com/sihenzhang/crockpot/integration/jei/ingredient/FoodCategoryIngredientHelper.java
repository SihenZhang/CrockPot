package com.sihenzhang.crockpot.integration.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;

import java.util.List;

public final class FoodCategoryIngredientHelper implements IIngredientHelper<FoodCategoryIngredient> {
    @Override
    public IIngredientType<FoodCategoryIngredient> getIngredientType() {
        return FoodCategoryIngredient.TYPE;
    }

    @Override
    public String getDisplayName(FoodCategoryIngredient ingredient) {
        return ingredient.displayName().getString();
    }

    @Override
    public Object getUid(FoodCategoryIngredient ingredient, UidContext context) {
        return ingredient.identifier();
    }

    @Override
    public Identifier getIdentifier(FoodCategoryIngredient ingredient) {
        return ingredient.identifier();
    }

    @Override
    public FoodCategoryIngredient copyIngredient(FoodCategoryIngredient ingredient) {
        return ingredient;
    }

    @Override
    public Iterable<Integer> getColors(FoodCategoryIngredient ingredient) {
        return List.of(ingredient.category().value().color());
    }

    @Override
    public boolean isValidIngredient(FoodCategoryIngredient ingredient) {
        return ingredient.category().isBound() && ingredient.category().unwrapKey().isPresent();
    }

    @Override
    public String getErrorInfo(@Nullable FoodCategoryIngredient ingredient) {
        if (ingredient == null) {
            return "null";
        }
        return ingredient.category().unwrapKey()
                .map(key -> key.identifier().toString())
                .orElseGet(() -> "Unregistered food category: " + ingredient.category());
    }
}
