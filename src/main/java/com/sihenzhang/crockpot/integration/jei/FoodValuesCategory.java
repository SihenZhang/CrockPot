package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.util.I18nUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.widgets.IScrollGridWidgetFactory;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

import java.util.Locale;
import java.util.Set;

public class FoodValuesCategory implements IRecipeCategory<FoodValuesCategory.FoodCategoryMatchedItems> {
    public static final RecipeType<FoodValuesCategory.FoodCategoryMatchedItems> RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "food_values", FoodValuesCategory.FoodCategoryMatchedItems.class);
    private final int WIDTH = 142;
    private final int HEIGHT = 74;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic slotDrawable;
    private final IScrollGridWidgetFactory<?> scrollGridFactory;

    public FoodValuesCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 16, 0, 16, 16);
        this.slotDrawable = guiHelper.getSlotDrawable();
        var scrollGridFactory = guiHelper.createScrollGridFactory(7, 3);
        scrollGridFactory.setPosition((WIDTH - scrollGridFactory.getArea().width()) / 2, 20);
        this.scrollGridFactory = scrollGridFactory;
    }

    @Override
    public RecipeType<FoodValuesCategory.FoodCategoryMatchedItems> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, FoodValuesCategory.FoodCategoryMatchedItems recipe, IFocusGroup focuses) {
        recipe.items().stream().map(Item::getDefaultInstance).forEach(stack -> builder.addSlotToWidget(RecipeIngredientRole.INPUT, scrollGridFactory).addItemStack(stack));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 1, 1)
                .addItemStack(FoodCategory.getItemStack(recipe.category()))
                .setBackground(slotDrawable, -1, -1);
    }

    @Override
    public void draw(FoodCategoryMatchedItems recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        guiGraphics.drawString(Minecraft.getInstance().font, I18nUtils.createTooltipComponent("food_values", I18nUtils.createComponent("item", "food_category_" + recipe.category().name().toLowerCase(Locale.ROOT)), recipe.value()), 22, 5, 0xFF505050, false);
    }

    public record FoodCategoryMatchedItems(FoodCategory category, float value, Set<Item> items) {
    }
}
