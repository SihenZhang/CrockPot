package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.client.FoodCategoryAtlas;
import com.sihenzhang.crockpot.registry.FoodCategory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import snownee.jade.api.ui.Element;

final class FoodCategoryElement extends Element {
    private final Identifier identifier;

    FoodCategoryElement(Holder<FoodCategory> category) {
        identifier = category.unwrapKey().orElseThrow().identifier();
        width = 16;
        height = 16;
    }

    @Override
    public Component getNarration() {
        return Component.translatable("item." + identifier.getNamespace() + ".food_category_" + identifier.getPath().replace('/', '.'));
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        graphics.blitSprite(RenderPipelines.GUI_TEXTURED, FoodCategoryAtlas.getSprite(identifier), getX(), getY(), getWidth(), getHeight());
    }
}
