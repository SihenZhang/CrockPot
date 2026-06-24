package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.sihenzhang.crockpot.integration.jei.FoodValuesDefinitionCache;
import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredient;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMax;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.NumberUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrawableRequirementCategoryMax extends AbstractDrawableRequirement<RequirementCategoryMax> {
    public DrawableRequirementCategoryMax(RequirementCategoryMax requirement) {
        super(requirement, NumberUtil.isClose(requirement.getMax(), 0.0F)
                ? I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking.requirement.no")
                : I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking.requirement.le", requirement.getMax()));
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
    public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        super.draw(guiGraphics, xOffset, yOffset);
        var zero = NumberUtil.isClose(requirement.getMax(), 0.0F);
        guiGraphics.text(Minecraft.getInstance().font, description, zero ? xOffset + 3 : xOffset + 20, yOffset + 7, TEXT_COLOR, false);
    }

    @Override
    public List<ItemStack> getInvisibleInputs() {
        return NumberUtil.isClose(requirement.getMax(), 0.0F) ? List.of() : List.copyOf(FoodValuesDefinitionCache.getMatchedItems(requirement.getCategory()));
    }

    @Override
    public List<GuiIngredientInfo> getGuiIngredientInfos(int xOffset, int yOffset) {
        var zero = NumberUtil.isClose(requirement.getMax(), 0.0F);
        return List.of(new GuiIngredientInfo(
                new FoodCategoryIngredient(requirement.getCategory()),
                zero ? xOffset + this.getWidth() - 19 : xOffset + 3,
                yOffset + 3,
                zero
        ));
    }
}
