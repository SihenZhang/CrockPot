package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationAnd;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrawableRequirementCombinationAnd extends AbstractDrawableRequirement<RequirementCombinationAnd> {
    private final AbstractDrawableRequirement<? extends IRequirement> first;
    private final AbstractDrawableRequirement<? extends IRequirement> second;

    public DrawableRequirementCombinationAnd(RequirementCombinationAnd requirement) {
        super(requirement, null);
        this.first = AbstractDrawableRequirement.createDrawable(requirement.getFirst());
        this.second = AbstractDrawableRequirement.createDrawable(requirement.getSecond());
    }

    @Override
    public int getWidth() {
        return 6 + Math.max(first.getWidth(), second.getWidth());
    }

    @Override
    public int getHeight() {
        return 7 + first.getHeight() + second.getHeight();
    }

    @Override
    public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        super.draw(guiGraphics, xOffset, yOffset);
        first.draw(guiGraphics, xOffset + 3, yOffset + 3);
        second.draw(guiGraphics, xOffset + 3, yOffset + first.getHeight() + 4);
    }

    @Override
    public List<ItemStack> getInvisibleInputs() {
        ImmutableList.Builder<ItemStack> builder = ImmutableList.builder();
        builder.addAll(first.getInvisibleInputs());
        builder.addAll(second.getInvisibleInputs());
        return builder.build();
    }

    @Override
    public List<GuiIngredientInfo> getGuiIngredientInfos(int xOffset, int yOffset) {
        ImmutableList.Builder<GuiIngredientInfo> builder = ImmutableList.builder();
        builder.addAll(first.getGuiIngredientInfos(xOffset + 3, yOffset + 3));
        builder.addAll(second.getGuiIngredientInfos(xOffset + 3, yOffset + first.getHeight() + 4));
        return builder.build();
    }
}
