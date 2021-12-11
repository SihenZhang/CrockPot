package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.recipe.CrockPotRecipeTypes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeManager;

import java.util.stream.Collectors;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CrockPot.MOD_ID, "main");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new CrockPotCookingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new FoodValuesCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ExplosionCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new PiglinBarteringRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Minecraft.getInstance().level.getRecipeManager();
        registration.addRecipes(recipeManager.getAllRecipesFor(CrockPotRecipeTypes.CROCK_POT_COOKING_RECIPE_TYPE).stream().filter(r -> r.getResult().getItem() != CrockPotRegistry.avaj).collect(Collectors.toList()), CrockPotCookingRecipeCategory.UID);
        registration.addRecipes(FoodValuesDefinition.getFoodCategoryMatchedItemsList(recipeManager), FoodValuesCategory.UID);
        registration.addRecipes(recipeManager.getAllRecipesFor(CrockPotRecipeTypes.EXPLOSION_CRAFT_RECIPE_TYPE), ExplosionCraftingRecipeCategory.UID);
        registration.addRecipes(recipeManager.getAllRecipesFor(CrockPotRecipeTypes.PIGLIN_BARTERING_RECIPE_TYPE), PiglinBarteringRecipeCategory.UID);
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        CrockPotCookingRecipeCategory.POTS.forEach(pot -> registration.addRecipeCatalyst(pot, CrockPotCookingRecipeCategory.UID));
    }
}
