package com.sihenzhang.crockpot.integration.jei;

import java.util.ArrayList;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CrockPot.MOD_ID, "main");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new FoodValuesCategory(registration.getJeiHelpers().getGuiHelper()),
        new PiglinBarteringRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
        new ExplosionCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
        new CookingCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
	public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
		registration.addRecipeCatalyst(new ItemStack(CrockPotRegistry.crockPotBasicBlock),CookingCategory.UID, FoodValuesCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(CrockPotRegistry.crockPotAdvancedBlock),CookingCategory.UID, FoodValuesCategory.UID);
		registration.addRecipeCatalyst(new ItemStack(CrockPotRegistry.crockPotUltimateBlock),CookingCategory.UID, FoodValuesCategory.UID);
		
	}

	@Override
	public void registerGuiHandlers(IGuiHandlerRegistration registration) {
		registration.addRecipeClickArea(CrockPotScreen.class,80,44,24,17,CookingCategory.UID, FoodValuesCategory.UID);
	}

	@Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(new ArrayList<>(CrockPot.FOOD_VALUES_MANAGER.getFoodCategoryMatchedItemsList()), FoodValuesCategory.UID);
        registration.addRecipes(new ArrayList<>(CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.getRecipes()), PiglinBarteringRecipeCategory.UID);
        registration.addRecipes(new ArrayList<>(CrockPot.EXPLOSION_CRAFTING_RECIPE_MANAGER.getRecipes()), ExplosionCraftingRecipeCategory.UID);
        registration.addRecipes(new ArrayList<>(CrockPot.CROCK_POT_RECIPE_MANAGER.getRecipes()), CookingCategory.UID);
    }
}
