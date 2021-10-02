package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValuesManager;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FoodValuesCategory implements IRecipeCategory<FoodValuesManager.FoodCategoryMatchedItems> {
    public static final ResourceLocation UID = new ResourceLocation(CrockPot.MOD_ID, "food_values");
    private final IDrawable background;
    private final IDrawable icon;

    public FoodValuesCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/food_values.png"), 0, 0, 166, 117);
        this.icon = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/icons.png"), 16, 0, 16, 16);
    }

    @Override
    public ResourceLocation getUid() {
        return FoodValuesCategory.UID;
    }

    @Override
    public Class<? extends FoodValuesManager.FoodCategoryMatchedItems> getRecipeClass() {
        return FoodValuesManager.FoodCategoryMatchedItems.class;
    }

    @Override
    public String getTitle() {
        return getTitleAsTextComponent().toString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("integration.crockpot.jei.food_values");
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
    public void setIngredients(FoodValuesManager.FoodCategoryMatchedItems recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(Ingredient.of(FoodCategory.getItemStack(recipe.getCategory()))));
        ingredients.setOutputs(VanillaTypes.ITEM, recipe.getItems().stream().map(Item::getDefaultInstance).collect(Collectors.toList()));
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, FoodValuesManager.FoodCategoryMatchedItems recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        int slot = 0;
        guiItemStacks.init(slot++, true, 74, 2);
        for (int row = 0; row < 5; row++) {
            for (int col = 0; col < 9; col++) {
                guiItemStacks.init(slot++, false, 2 + col * 18, 25 + row * 18);
            }
        }
        List<List<ItemStack>> pagedIngredientsOutputs = JeiUtils.getPagedIngredientsOutputs(recipeLayout, ingredients, 45);
        guiItemStacks.set(0, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        for (int i = 0; i < pagedIngredientsOutputs.size(); i++) {
            guiItemStacks.set(i + 1, pagedIngredientsOutputs.get(i));
        }
    }
}
