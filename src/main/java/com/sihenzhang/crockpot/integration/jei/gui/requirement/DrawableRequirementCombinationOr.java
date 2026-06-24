package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationOr;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrawableRequirementCombinationOr extends AbstractDrawableRequirement<RequirementCombinationOr> {
    private final AbstractDrawableRequirement<? extends IRequirement> first;
    private final AbstractDrawableRequirement<? extends IRequirement> second;

    public DrawableRequirementCombinationOr(RequirementCombinationOr requirement) {
        super(requirement, I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking.requirement.or"));
        this.first = AbstractDrawableRequirement.createDrawable(requirement.getFirst());
        this.second = AbstractDrawableRequirement.createDrawable(requirement.getSecond());
    }

    @Override
    public int getWidth() {
        return 8 + first.getWidth() + second.getWidth() + Minecraft.getInstance().font.width(description);
    }

    @Override
    public int getHeight() {
        return 6 + Math.max(first.getHeight(), second.getHeight());
    }

    @Override
    public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        super.draw(guiGraphics, xOffset, yOffset);
        first.draw(guiGraphics, xOffset + 3, yOffset + (this.getHeight() / 2) - (first.getHeight() / 2));
        guiGraphics.text(Minecraft.getInstance().font, description, xOffset + first.getWidth() + 4, yOffset + (this.getHeight() / 2) - 4, TEXT_COLOR, false);
        second.draw(guiGraphics, xOffset + this.getWidth() - second.getWidth() - 3, yOffset + (this.getHeight() / 2) - (second.getHeight() / 2));
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
        builder.addAll(first.getGuiIngredientInfos(xOffset + 3, yOffset + (this.getHeight() / 2) - (first.getHeight() / 2)));
        builder.addAll(second.getGuiIngredientInfos(xOffset + this.getWidth() - second.getWidth() - 3, yOffset + (this.getHeight() / 2) - (second.getHeight() / 2)));
        return builder.build();
    }
}
