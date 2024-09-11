package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.integration.jei.FoodValuesDefinitionCache;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMax;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrawableRequirementCategoryMax extends AbstractDrawableRequirement<RequirementCategoryMax> {
    public DrawableRequirementCategoryMax(RequirementCategoryMax requirement) {
        super(requirement, MathUtils.fuzzyIsZero(requirement.max()) ? Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.no") : Component.translatable("integration.crockpot.jei.crock_pot_cooking.requirement.le", requirement.max()));
    }

    @Override
    public int getWidth() {
        return 23 + Minecraft.getInstance().font.width(description);
    }

    @Override
    public int getHeight() {
        return 22;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        super.draw(guiGraphics, xOffset, yOffset);
        guiGraphics.drawString(Minecraft.getInstance().font, description, MathUtils.fuzzyIsZero(requirement.max()) ? xOffset + 3 : xOffset + 20, yOffset + 7, 0, false);
    }

    @Override
    public List<ItemStack> getInvisibleInputs() {
        return MathUtils.fuzzyIsZero(requirement.max()) ? List.of() : List.copyOf(FoodValuesDefinitionCache.getMatchedItems(requirement.category()));
    }

    @Override
    public List<GuiItemStacksInfo> getGuiItemStacksInfos(int xOffset, int yOffset) {
        return List.of(new GuiItemStacksInfo(List.of(FoodCategory.getItemStack(requirement.category())), MathUtils.fuzzyIsZero(requirement.max()) ? xOffset + this.getWidth() - 19 : xOffset + 3, yOffset + 3, MathUtils.fuzzyIsZero(requirement.max())));
    }
}
