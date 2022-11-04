package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.util.RLUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class FoodValuesCategory implements IRecipeCategory<FoodValuesDefinition.FoodCategoryMatchedItems> {
    public static final RecipeType<FoodValuesDefinition.FoodCategoryMatchedItems> RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "food_values", FoodValuesDefinition.FoodCategoryMatchedItems.class);
    private final IDrawable background;
    private final IDrawable icon;

    public FoodValuesCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/food_values.png"), 0, 0, 166, 117);
        this.icon = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/icons.png"), 16, 0, 16, 16);
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends FoodValuesDefinition.FoodCategoryMatchedItems> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<FoodValuesDefinition.FoodCategoryMatchedItems> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("integration.crockpot.jei.food_values");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FoodValuesDefinition.FoodCategoryMatchedItems recipe, IFocusGroup focuses) {
        List<List<ItemStack>> pagedItemStacks = JeiUtils.getPagedItemStacks(recipe.items().stream().map(Item::getDefaultInstance).toList(), focuses, RecipeIngredientRole.INPUT, 45);
        for (int i = 0; i < pagedItemStacks.size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 3 + i % 9 * 18, 26 + i / 9 * 18).addItemStacks(pagedItemStacks.get(i));
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 75, 3).addItemStack(FoodCategory.getItemStack(recipe.category()));
    }
}
