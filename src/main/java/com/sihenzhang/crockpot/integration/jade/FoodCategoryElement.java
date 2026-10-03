package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.client.FoodCategoryAtlas;
import com.sihenzhang.crockpot.registry.FoodCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import snownee.jade.api.ui.Element;

final class FoodCategoryElement extends Element {
    private final Holder<FoodCategory> category;

    FoodCategoryElement(Holder<FoodCategory> category) {
        this.category = category;
    }

    @Override
    public Vec2 getSize() {
        return new Vec2(16, 16);
    }

    @Override
    public String getMessage() {
        var id = category.unwrapKey().orElseThrow().location();
        return Component.translatable(id.toLanguageKey("food_category")).getString();
    }

    @Override
    public void render(GuiGraphics graphics, float x, float y, float maxX, float maxY) {
        var sprite = FoodCategoryAtlas.get(category);
        graphics.blit((int) x, (int) y, 0, 16, 16, sprite);
    }
}
