package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(CrockPot.MOD_ID, "main");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new PiglinBarteringRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
        registration.addRecipeCategories(new ExplosionCraftingRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.getRecipes(), PiglinBarteringRecipeCategory.UID);
        registration.addRecipes(CrockPot.EXPLOSION_CRAFTING_RECIPE_MANAGER.getRecipes(), ExplosionCraftingRecipeCategory.UID);
    }
}
