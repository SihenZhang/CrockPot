package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.stream.Collectors;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    public static final RecipeType<CrockPotCookingRecipe> CROCK_POT_COOKING_RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "crock_pot_cooking", CrockPotCookingRecipe.class);
    public static final RecipeType<FoodValuesDefinition.FoodCategoryMatchedItems> FOOD_VALUES_RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "food_values", FoodValuesDefinition.FoodCategoryMatchedItems.class);
    public static final RecipeType<ExplosionCraftingRecipe> EXPLOSION_CRAFTING_RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "explosion_crafting", ExplosionCraftingRecipe.class);
    public static final RecipeType<PiglinBarteringRecipe> PIGLIN_BARTERING_RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "piglin_bartering", PiglinBarteringRecipe.class);

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CrockPot.MOD_ID, "main");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CrockPotCookingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new FoodValuesCategory(guiHelper));
        registration.addRecipeCategories(new ExplosionCraftingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new PiglinBarteringRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(CROCK_POT_COOKING_RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRegistry.crockPotCookingRecipeType.get()).stream().filter(r -> r.getResult().getItem() != CrockPotRegistry.avaj.get()).collect(Collectors.toList()));
        registration.addRecipes(FOOD_VALUES_RECIPE_TYPE, FoodValuesDefinition.getFoodCategoryMatchedItemsList(recipeManager));
        registration.addRecipes(EXPLOSION_CRAFTING_RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRegistry.explosionCraftingRecipeType.get()));
        registration.addRecipes(PIGLIN_BARTERING_RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRegistry.piglinBarteringRecipeType.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        CrockPotCookingRecipeCategory.POTS.forEach(pot -> registration.addRecipeCatalyst(pot, CROCK_POT_COOKING_RECIPE_TYPE));
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CrockPotScreen.class, 80, 43, 24, 18, CROCK_POT_COOKING_RECIPE_TYPE);
    }
}
