package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.sihenzhang.crockpot.integration.jei.JeiUtils;
import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredientLessThan;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrawableRequirementMustContainIngredientLessThan extends AbstractDrawableRequirement<RequirementMustContainIngredientLessThan> {
    public DrawableRequirementMustContainIngredientLessThan(RequirementMustContainIngredientLessThan requirement) {
        super(requirement, I18nUtil.integration(
                ModIntegrationJei.MOD_ID,
                requirement.getQuantity() >= 4 ? "crock_pot_cooking.requirement.eq" : "crock_pot_cooking.requirement.le",
                requirement.getQuantity()
        ));
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
        return List.of();
    }

    @Override
    public List<GuiIngredientInfo> getGuiIngredientInfos(int xOffset, int yOffset) {
        return List.of(new GuiIngredientInfo(JeiUtils.getItemsFromIngredientWithoutEmptyTag(requirement.getIngredient()), xOffset + 3, yOffset + 3));
    }
}
