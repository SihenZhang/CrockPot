package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredientLessThan;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.*;

public class DrawableRequirementMustContainIngredientLessThan extends AbstractDrawableRequirement<RequirementMustContainIngredientLessThan> {
    public DrawableRequirementMustContainIngredientLessThan(RequirementMustContainIngredientLessThan requirement) {
        super(requirement, new TranslationTextComponent(requirement.getQuantity() >= 4 ? "integration.crockpot.jei.crock_pot_cooking.requirement.eq" : "integration.crockpot.jei.crock_pot_cooking.requirement.le", requirement.getQuantity()));
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
    public void draw(MatrixStack matrixStack, int xOffset, int yOffset) {
        super.draw(matrixStack, xOffset, yOffset);
        Minecraft.getInstance().font.draw(matrixStack, description, xOffset + 20, yOffset + 7, 0);
    }

    @Override
    public List<List<ItemStack>> getInputLists() {
        Set<ItemStack> stacks = new TreeSet<>(Comparator.comparing(o -> o.getItem().getRegistryName()));
        Collections.addAll(stacks, requirement.getIngredient().getItems());
        return ImmutableList.of(ImmutableList.copyOf(stacks));
    }

    @Override
    public List<GuiItemStacksInfo> getGuiItemStacksInfos(int xOffset, int yOffset) {
        Set<ItemStack> stacks = new TreeSet<>(Comparator.comparing(o -> o.getItem().getRegistryName()));
        Collections.addAll(stacks, requirement.getIngredient().getItems());
        return ImmutableList.of(new GuiItemStacksInfo(ImmutableList.copyOf(stacks), xOffset + 3, yOffset + 3));
    }
}
