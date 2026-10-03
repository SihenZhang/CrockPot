package com.sihenzhang.crockpot.integration.jei.ingredient;

import com.sihenzhang.crockpot.client.FoodCategoryAtlas;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public final class FoodCategoryIngredientRenderer implements IIngredientRenderer<FoodCategoryIngredient> {
    @Override
    public void render(GuiGraphics graphics, FoodCategoryIngredient ingredient) {
        var sprite = FoodCategoryAtlas.get(ingredient.category());
        graphics.blit(0, 0, 0, 16, 16, sprite);
    }

    @Override
    public List<Component> getTooltip(FoodCategoryIngredient ingredient, TooltipFlag tooltipFlag) {
        return List.of(ingredient.displayName().copy().withStyle(Style.EMPTY.withColor(ingredient.category().value().color())));
    }
}
