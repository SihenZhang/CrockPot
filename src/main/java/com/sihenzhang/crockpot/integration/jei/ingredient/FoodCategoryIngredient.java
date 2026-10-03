package com.sihenzhang.crockpot.integration.jei.ingredient;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.registry.FoodCategory;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public record FoodCategoryIngredient(Holder<FoodCategory> category) {
    public static final Codec<FoodCategoryIngredient> CODEC = FoodCategory.CODEC.xmap(FoodCategoryIngredient::new, FoodCategoryIngredient::category);
    public static final IIngredientType<FoodCategoryIngredient> TYPE = () -> FoodCategoryIngredient.class;

    public ResourceLocation id() {
        return category.unwrapKey().orElseThrow().location();
    }

    public Component displayName() {
        var id = id();
        return Component.translatable(id.toLanguageKey("food_category"));
    }
}
