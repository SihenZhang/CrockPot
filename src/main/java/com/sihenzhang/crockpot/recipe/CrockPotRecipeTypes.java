package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class CrockPotRecipeTypes {
    public static RecipeType<CrockPotCookingRecipe> CROCK_POT_COOKING_RECIPE_TYPE = register("crock_pot_cooking");
    public static RecipeType<FoodValuesDefinition> FOOD_VALUES_RECIPE_TYPE = register("food_values");
    public static RecipeType<ExplosionCraftingRecipe> EXPLOSION_CRAFT_RECIPE_TYPE = register("explosion_crafting");
    public static RecipeType<PiglinBarteringRecipe> PIGLIN_BARTERING_RECIPE_TYPE = register("piglin_bartering");

    private static <T extends Recipe<?>> RecipeType<T> register(String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(CrockPot.MOD_ID, key), new RecipeType<T>() {
            @Override
            public String toString() {
                return CrockPot.MOD_ID + ":" + key;
            }
        });
    }
}
