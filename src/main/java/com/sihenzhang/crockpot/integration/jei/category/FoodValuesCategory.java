package com.sihenzhang.crockpot.integration.jei.category;

import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.RecipeTypes;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredient;
import com.sihenzhang.crockpot.util.I18nUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.Holder;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class FoodValuesCategory extends AbstractRecipeCategory<FoodValuesCategory.FoodCategoryMatchedItems> {
    private static final int WIDTH = 142;
    private static final int HEIGHT = 74;

    private final IDrawable slotDrawable;

    public FoodValuesCategory(IGuiHelper guiHelper) {
        super(
                RecipeTypes.FOOD_VALUES,
                I18nUtil.integration(ModIntegrationJei.MOD_ID, "food_values"),
                guiHelper.createDrawable(ModIntegrationJei.ICONS, 16, 0, 16, 16),
                WIDTH,
                HEIGHT
        );
        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, FoodCategoryMatchedItems recipe, IFocusGroup focuses) {
        recipe.items().forEach(stack -> builder.addSlot(RecipeIngredientRole.INPUT).setStandardSlotBackground().add(stack));
        builder.addOutputSlot(1, 1)
                .add(FoodCategoryIngredient.TYPE, new FoodCategoryIngredient(recipe.category()))
                .setBackground(slotDrawable, -1, -1);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, FoodCategoryMatchedItems recipe, IFocusGroup focuses) {
        var inputSlots = builder.getRecipeSlots().getSlots(RecipeIngredientRole.INPUT);
        var scrollGrid = builder.addScrollGridWidget(inputSlots, 7, 3);
        scrollGrid.setPosition((WIDTH - scrollGrid.getWidth()) / 2, 20);
    }

    @Override
    public void draw(FoodCategoryMatchedItems recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        var ingredient = new FoodCategoryIngredient(recipe.category());
        guiGraphics.text(
                Minecraft.getInstance().font,
                I18nUtil.tooltip("food_values", ingredient.displayName(), recipe.value()),
                22,
                5,
                0xFF505050,
                false
        );
    }

    public record FoodCategoryMatchedItems(Holder<FoodCategory> category, float value, Set<ItemStack> items) {
    }
}
