package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.integration.jei.category.FoodValuesCategory;
import com.sihenzhang.crockpot.integration.jei.category.ParrotLayingEggsRecipeCategory;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.recipe.DryingRecipe;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.util.IdUtil;
import mezz.jei.api.recipe.types.IRecipeHolderType;
import mezz.jei.api.recipe.types.IRecipeType;

public final class RecipeTypes {
    private RecipeTypes() {}

    public static final IRecipeHolderType<CrockPotCookingRecipe> CROCK_POT_COOKING = IRecipeHolderType.create(ModRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get());
    public static final IRecipeHolderType<DryingRecipe> DRYING = IRecipeHolderType.create(ModRecipes.DRYING_RECIPE_TYPE.get());
    public static final IRecipeHolderType<ExplosionCraftingRecipe> EXPLOSION_CRAFTING = IRecipeHolderType.create(ModRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get());
    public static final IRecipeType<FoodValuesCategory.FoodCategoryMatchedItems> FOOD_VALUES = IRecipeType.create(IdUtil.mod(ModRecipes.FOOD_VALUES), FoodValuesCategory.FoodCategoryMatchedItems.class);
    public static final IRecipeHolderType<ParrotFeedingRecipe> PARROT_FEEDING = IRecipeHolderType.create(ModRecipes.PARROT_FEEDING_RECIPE_TYPE.get());
    public static final IRecipeType<ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper> PARROT_LAYING_EGGS = IRecipeType.create(IdUtil.mod("parrot_laying_eggs"), ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper.class);
    public static final IRecipeHolderType<PiglinBarteringRecipe> PIGLIN_BARTERING = IRecipeHolderType.create(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get());
}
