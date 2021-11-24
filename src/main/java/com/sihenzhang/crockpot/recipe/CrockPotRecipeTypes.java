package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.pot.CrockPotCookingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class CrockPotRecipeTypes {
    public static IRecipeType<CrockPotCookingRecipe> CROCK_POT_COOKING_RECIPE_TYPE = register("crock_pot_cooking");
    public static IRecipeType<FoodValuesDefinition> FOOD_VALUES_RECIPE_TYPE = register("food_values");
    public static IRecipeType<ExplosionCraftingRecipe> EXPLOSION_CRAFT_RECIPE_TYPE = register("explosion_crafting");
    public static IRecipeType<PiglinBarteringRecipe> PIGLIN_BARTERING_RECIPE_TYPE = register("piglin_bartering");

    private static <T extends IRecipe<?>> IRecipeType<T> register(String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(CrockPot.MOD_ID, key), new IRecipeType<T>() {
            @Override
            public String toString() {
                return CrockPot.MOD_ID + ":" + key;
            }
        });
    }
}
