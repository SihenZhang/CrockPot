package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class CrockPotRecipeTypes {
    public static RecipeType<CrockPotCookingRecipe> CROCK_POT_COOKING_RECIPE_TYPE;
    public static RecipeType<FoodValuesDefinition> FOOD_VALUES_RECIPE_TYPE;
    public static RecipeType<ExplosionCraftingRecipe> EXPLOSION_CRAFT_RECIPE_TYPE;
    public static RecipeType<PiglinBarteringRecipe> PIGLIN_BARTERING_RECIPE_TYPE;

    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event) {
        CROCK_POT_COOKING_RECIPE_TYPE = register("crock_pot_cooking");
        FOOD_VALUES_RECIPE_TYPE = register("food_values");
        EXPLOSION_CRAFT_RECIPE_TYPE = register("explosion_crafting");
        PIGLIN_BARTERING_RECIPE_TYPE = register("piglin_bartering");
    }

    private static <T extends Recipe<?>> RecipeType<T> register(String key) {
        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(CrockPot.MOD_ID, key), new RecipeType<T>() {
            @Override
            public String toString() {
                return CrockPot.MOD_ID + ":" + key;
            }
        });
    }
}
