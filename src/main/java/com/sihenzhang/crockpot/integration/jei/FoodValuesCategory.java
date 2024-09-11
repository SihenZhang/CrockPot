package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.util.I18nUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.widgets.IScrollGridWidgetFactory;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.Set;

public class FoodValuesCategory implements IRecipeCategory<FoodValuesCategory.FoodCategoryMatchedItems> {
    public static final RecipeType<FoodCategoryMatchedItems> RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "food_values", FoodCategoryMatchedItems.class);
    private final int WIDTH = 178;
    private final int HEIGHT = 110;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic slotDrawable;
    private final IScrollGridWidgetFactory<?> scrollGridFactory;

    public FoodValuesCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createBlankDrawable(WIDTH, HEIGHT);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 16, 0, 16, 16);
        this.slotDrawable = guiHelper.getSlotDrawable();
        var scrollGridFactory = guiHelper.createScrollGridFactory(9, 5);
        scrollGridFactory.setPosition((WIDTH - scrollGridFactory.getArea().width()) / 2, 20);
        this.scrollGridFactory = scrollGridFactory;
    }

    @Override
    public RecipeType<FoodCategoryMatchedItems> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, FoodCategoryMatchedItems recipe, IFocusGroup focuses) {
        recipe.items().forEach(stack -> builder.addSlotToWidget(RecipeIngredientRole.INPUT, scrollGridFactory).addItemStack(stack));
        builder.addSlot(RecipeIngredientRole.OUTPUT, (WIDTH - slotDrawable.getWidth()) / 2 + 1, 1)
                .addItemStack(FoodCategory.getItemStack(recipe.category()))
                .setBackground(slotDrawable, -1, -1);
    }

    public record FoodCategoryMatchedItems(FoodCategory category, Set<ItemStack> items) {
    }
}
