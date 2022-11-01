package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ParrotFeedingRecipeCategory implements IRecipeCategory<ParrotFeedingRecipe> {
    private final IDrawable icon;

    public static final RecipeType<ParrotFeedingRecipe> PARROT_FEEDING_RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "parrot_feeding", ParrotFeedingRecipe.class);

    public ParrotFeedingRecipeCategory(IGuiHelper guiHelper) {
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, CrockPotRegistry.BIRDCAGE_BLOCK_ITEM.get().getDefaultInstance());
    }

    @Override
    @SuppressWarnings("removal")
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @Override
    @SuppressWarnings("removal")
    public Class<? extends ParrotFeedingRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<ParrotFeedingRecipe> getRecipeType() {
        return PARROT_FEEDING_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return null;
    }

    @Override
    public IDrawable getBackground() {
        return null;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }
}
