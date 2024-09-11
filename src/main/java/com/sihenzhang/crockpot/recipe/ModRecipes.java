package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModRecipes {
    private ModRecipes() {
    }

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, CrockPot.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, CrockPot.MOD_ID);

    public static final String CROCK_POT_COOKING = "crock_pot_cooking";
    public static final String EXPLOSION_CRAFTING = "explosion_crafting";
    public static final String FOOD_VALUES = "food_values";
    public static final String PARROT_FEEDING = "parrot_feeding";
    public static final String PIGLIN_BARTERING = "piglin_bartering";

    public static final Supplier<RecipeType<CrockPotCookingRecipe>> CROCK_POT_COOKING_RECIPE_TYPE = RECIPE_TYPES.register(CROCK_POT_COOKING, () -> new CrockPotRecipeType<>(CROCK_POT_COOKING));
    public static final Supplier<RecipeType<ExplosionCraftingRecipe>> EXPLOSION_CRAFTING_RECIPE_TYPE = RECIPE_TYPES.register(EXPLOSION_CRAFTING, () -> new CrockPotRecipeType<>(EXPLOSION_CRAFTING));
    public static final Supplier<RecipeType<FoodValuesDefinition>> FOOD_VALUES_RECIPE_TYPE = RECIPE_TYPES.register(FOOD_VALUES, () -> new CrockPotRecipeType<>(FOOD_VALUES));
    public static final Supplier<RecipeType<ParrotFeedingRecipe>> PARROT_FEEDING_RECIPE_TYPE = RECIPE_TYPES.register(PARROT_FEEDING, () -> new CrockPotRecipeType<>(PARROT_FEEDING));
    public static final Supplier<RecipeType<PiglinBarteringRecipe>> PIGLIN_BARTERING_RECIPE_TYPE = RECIPE_TYPES.register(PIGLIN_BARTERING, () -> new CrockPotRecipeType<>(PIGLIN_BARTERING));

    public static final Supplier<RecipeSerializer<CrockPotCookingRecipe>> CROCK_POT_COOKING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(CROCK_POT_COOKING, CrockPotCookingRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<ExplosionCraftingRecipe>> EXPLOSION_CRAFTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(EXPLOSION_CRAFTING, ExplosionCraftingRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<FoodValuesDefinition>> FOOD_VALUES_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(FOOD_VALUES, FoodValuesDefinition.Serializer::new);
    public static final Supplier<RecipeSerializer<ParrotFeedingRecipe>> PARROT_FEEDING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(PARROT_FEEDING, ParrotFeedingRecipe.Serializer::new);
    public static final Supplier<RecipeSerializer<PiglinBarteringRecipe>> PIGLIN_BARTERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register(PIGLIN_BARTERING, PiglinBarteringRecipe.Serializer::new);
}
