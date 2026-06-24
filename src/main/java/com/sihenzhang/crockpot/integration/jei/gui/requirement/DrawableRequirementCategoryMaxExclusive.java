package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.sihenzhang.crockpot.integration.jei.FoodValuesDefinitionCache;
import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredient;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMaxExclusive;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrawableRequirementCategoryMaxExclusive extends AbstractDrawableRequirement<RequirementCategoryMaxExclusive> {
    public DrawableRequirementCategoryMaxExclusive(RequirementCategoryMaxExclusive requirement) {
        super(requirement, I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking.requirement.lt", requirement.getMax()));
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
        guiGraphics.text(Minecraft.getInstance().font, description, xOffset + 20, yOffset + 7, TEXT_COLOR, false);
    }

    @Override
    public List<ItemStack> getInvisibleInputs() {
        return List.copyOf(FoodValuesDefinitionCache.getMatchedItems(requirement.getCategory()));
    }

    @Override
    public List<GuiIngredientInfo> getGuiIngredientInfos(int xOffset, int yOffset) {
        return List.of(new GuiIngredientInfo(new FoodCategoryIngredient(requirement.getCategory()), xOffset + 3, yOffset + 3));
    }
}
