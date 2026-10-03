package com.sihenzhang.crockpot.integration.jei.ingredient;

import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.UidContext;
import net.minecraft.resources.ResourceLocation;

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
    public String getUniqueId(FoodCategoryIngredient ingredient, UidContext context) {
        return ingredient.id().toString();
    }

    @Override
    public ResourceLocation getResourceLocation(FoodCategoryIngredient ingredient) {
        return ingredient.id();
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
    public String getErrorInfo(FoodCategoryIngredient ingredient) {
        return ingredient == null ? "null" : ingredient.category().toString();
    }
}
