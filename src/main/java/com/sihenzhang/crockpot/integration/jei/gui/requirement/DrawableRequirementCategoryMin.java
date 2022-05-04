package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMin;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;

public class DrawableRequirementCategoryMin extends AbstractDrawableRequirement<RequirementCategoryMin> {
    public DrawableRequirementCategoryMin(RequirementCategoryMin requirement) {
        super(requirement, new TranslatableComponent("integration.crockpot.jei.crock_pot_cooking.requirement.ge", requirement.getMin()));
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
    public void draw(PoseStack stack, int xOffset, int yOffset) {
        super.draw(stack, xOffset, yOffset);
        Minecraft.getInstance().font.draw(stack, description, xOffset + 20, yOffset + 7, 0);
    }

    @Override
    public List<ItemStack> getInvisibleInputs() {
        return ImmutableList.copyOf(FoodValuesDefinition.getMatchedItems(requirement.getCategory(), Minecraft.getInstance().level.getRecipeManager()).stream().map(Item::getDefaultInstance).iterator());
    }

    @Override
    public List<GuiItemStacksInfo> getGuiItemStacksInfos(int xOffset, int yOffset) {
        return ImmutableList.of(new GuiItemStacksInfo(ImmutableList.of(FoodCategory.getItemStack(requirement.getCategory())), xOffset + 3, yOffset + 3));
    }
}
