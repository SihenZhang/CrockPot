package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModRecipes {
    private ModRecipes() {
    }

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CrockPot.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CrockPot.MOD_ID);

    public static final String CROCK_POT_COOKING = "crock_pot_cooking";
    public static final String EXPLOSION_CRAFTING = "explosion_crafting";
    public static final String DRYING = "drying";
    public static final String FOOD_VALUES = "food_values";
    public static final String PARROT_FEEDING = "parrot_feeding";
    public static final String PIGLIN_BARTERING = "piglin_bartering";

    public static final DeferredHolder<RecipeType<?>, RecipeType<CrockPotCookingRecipe>> CROCK_POT_COOKING_RECIPE_TYPE = RECIPE_TYPES.register(CROCK_POT_COOKING, () -> new BaseRecipeType<>(CROCK_POT_COOKING));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ExplosionCraftingRecipe>> EXPLOSION_CRAFTING_RECIPE_TYPE = RECIPE_TYPES.register(EXPLOSION_CRAFTING, () -> new BaseRecipeType<>(EXPLOSION_CRAFTING));
    public static final DeferredHolder<RecipeType<?>, RecipeType<DryingRecipe>> DRYING_RECIPE_TYPE = RECIPE_TYPES.register(DRYING, () -> new BaseRecipeType<>(DRYING));
    public static final DeferredHolder<RecipeType<?>, RecipeType<FoodValuesDefinition>> FOOD_VALUES_RECIPE_TYPE = RECIPE_TYPES.register(FOOD_VALUES, () -> new BaseRecipeType<>(FOOD_VALUES));
    public static final DeferredHolder<RecipeType<?>, RecipeType<ParrotFeedingRecipe>> PARROT_FEEDING_RECIPE_TYPE = RECIPE_TYPES.register(PARROT_FEEDING, () -> new BaseRecipeType<>(PARROT_FEEDING));
    public static final DeferredHolder<RecipeType<?>, RecipeType<PiglinBarteringRecipe>> PIGLIN_BARTERING_RECIPE_TYPE = RECIPE_TYPES.register(PIGLIN_BARTERING, () -> new BaseRecipeType<>(PIGLIN_BARTERING));

    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<CrockPotCookingRecipe>> CROCK_POT_COOKING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(
            CROCK_POT_COOKING, () -> new RecipeSerializer<>(CrockPotCookingRecipe.CODEC, CrockPotCookingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ExplosionCraftingRecipe>> EXPLOSION_CRAFTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(
            EXPLOSION_CRAFTING, () -> new RecipeSerializer<>(ExplosionCraftingRecipe.CODEC, ExplosionCraftingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<DryingRecipe>> DRYING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(
            DRYING, () -> new RecipeSerializer<>(DryingRecipe.CODEC, DryingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<FoodValuesDefinition>> FOOD_VALUES_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(
            FOOD_VALUES, () -> new RecipeSerializer<>(FoodValuesDefinition.CODEC, FoodValuesDefinition.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<ParrotFeedingRecipe>> PARROT_FEEDING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(
            PARROT_FEEDING, () -> new RecipeSerializer<>(ParrotFeedingRecipe.CODEC, ParrotFeedingRecipe.STREAM_CODEC)
    );
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PiglinBarteringRecipe>> PIGLIN_BARTERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(
            PIGLIN_BARTERING, () -> new RecipeSerializer<>(PiglinBarteringRecipe.CODEC, PiglinBarteringRecipe.STREAM_CODEC)
    );
}
