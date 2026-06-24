package com.sihenzhang.crockpot.integration.jei.ingredient;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.util.IdUtil;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.util.Objects;

public record FoodCategoryIngredient(Holder<FoodCategory> category) {
    public static final Codec<FoodCategoryIngredient> CODEC = FoodCategory.CODEC.xmap(
            FoodCategoryIngredient::new,
            FoodCategoryIngredient::category
    );
    public static final IIngredientType<FoodCategoryIngredient> TYPE = new IIngredientType<>() {
        @Override
        public Class<? extends FoodCategoryIngredient> getIngredientClass() {
            return FoodCategoryIngredient.class;
        }

        @Override
        public String getUid() {
            return IdUtil.mod("food_category").toString();
        }
    };

    public FoodCategoryIngredient {
        Objects.requireNonNull(category);
    }

    public ResourceKey<FoodCategory> key() {
        return category.unwrapKey().orElseThrow(() -> new IllegalStateException("Food category is not registered: " + category));
    }

    public Identifier identifier() {
        return key().identifier();
    }

    public Component displayName() {
        Identifier id = identifier();
        String path = id.getPath().replace('/', '.');
        return Component.translatable("item." + id.getNamespace() + ".food_category_" + path);
    }
}
