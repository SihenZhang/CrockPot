package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public class FoodValuesCategory implements IRecipeCategory<FoodValuesDefinition.FoodCategoryMatchedItems> {
    public static final RecipeType<FoodValuesDefinition.FoodCategoryMatchedItems> RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "food_values", FoodValuesDefinition.FoodCategoryMatchedItems.class);
    private final IDrawable background;
    private final IDrawable icon;

    public FoodValuesCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/food_values.png"), 0, 0, 166, 117);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 16, 0, 16, 16);
    }

    @Override
    public RecipeType<FoodValuesDefinition.FoodCategoryMatchedItems> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "food_values");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FoodValuesDefinition.FoodCategoryMatchedItems recipe, IFocusGroup focuses) {
        var pagedItemStacks = JeiUtils.getPagedItemStacks(recipe.items().stream().map(Item::getDefaultInstance).toList(), focuses, RecipeIngredientRole.INPUT, 45);
        for (var i = 0; i < pagedItemStacks.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 3 + i % 9 * 18, 26 + i / 9 * 18).addItemStacks(pagedItemStacks.get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 75, 3).addItemStack(FoodCategory.getItemStack(recipe.category()));
    }
}
