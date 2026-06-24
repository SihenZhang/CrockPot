package com.sihenzhang.crockpot.integration.jei.ingredient;

import com.sihenzhang.crockpot.client.FoodCategoryAtlas;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public final class FoodCategoryIngredientRenderer implements IIngredientRenderer<FoodCategoryIngredient> {
    @Override
    public void render(GuiGraphicsExtractor guiGraphics, FoodCategoryIngredient ingredient) {
        var sprite = FoodCategoryAtlas.getSprite(ingredient.identifier());
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, sprite, 0, 0, getWidth(), getHeight());
    }

    @Override
    public List<Component> getTooltip(FoodCategoryIngredient ingredient, TooltipFlag tooltipFlag) {
        return List.of(ingredient.displayName().copy().withStyle(Style.EMPTY.withColor(ingredient.category().value().color())));
    }
}
