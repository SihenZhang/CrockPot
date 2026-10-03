package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.client.FoodCategoryAtlas;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.util.RLUtils;
import mcjty.theoneprobe.api.IElement;
import mcjty.theoneprobe.api.IElementFactory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

final class FoodCategoryElement implements IElement {
    private static final ResourceLocation ID = RLUtils.createRL("food_category");
    static final IElementFactory FACTORY = new IElementFactory() {
        @Override
        public IElement createElement(RegistryFriendlyByteBuf buffer) {
            return new FoodCategoryElement(FoodCategory.STREAM_CODEC.decode(buffer));
        }

        @Override
        public ResourceLocation getId() {
            return ID;
        }
    };

    private final Holder<FoodCategory> category;

    FoodCategoryElement(Holder<FoodCategory> category) {
        this.category = category;
    }

    @Override
    public void render(GuiGraphics graphics, int x, int y) {
        var sprite = FoodCategoryAtlas.get(category);
        graphics.blit(x, y, 0, 16, 16, sprite);
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    @Override
    public void toBytes(RegistryFriendlyByteBuf buffer) {
        FoodCategory.STREAM_CODEC.encode(buffer, category);
    }

    @Override
    public ResourceLocation getID() {
        return ID;
    }
}
