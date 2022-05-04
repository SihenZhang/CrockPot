package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredient;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredientLessThan;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;

import java.util.*;

public class DrawableRequirementMustContainIngredient extends AbstractDrawableRequirement<RequirementMustContainIngredient> {
    public DrawableRequirementMustContainIngredient(RequirementMustContainIngredient requirement) {
        super(requirement, new TranslatableComponent(requirement.getQuantity() >= 4 ? "integration.crockpot.jei.crock_pot_cooking.requirement.eq" : "integration.crockpot.jei.crock_pot_cooking.requirement.ge", requirement.getQuantity()));
    }

    public DrawableRequirementMustContainIngredient(RequirementMustContainIngredient requirement, RequirementMustContainIngredientLessThan dummy) {
        super(requirement, new TranslatableComponent("integration.crockpot.jei.crock_pot_cooking.requirement.eq", requirement.getQuantity()));
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
        return ImmutableList.of();
    }

    @Override
    public List<GuiItemStacksInfo> getGuiItemStacksInfos(int xOffset, int yOffset) {
        Set<ItemStack> stacks = new TreeSet<>(Comparator.comparing(o -> o.getItem().getRegistryName()));
        Arrays.stream(requirement.getIngredient().getItems()).filter(stack -> !(stack.is(Blocks.BARRIER.asItem()) && stack.getHoverName().getContents().contains("Empty Tag: "))).forEach(stacks::add);
        return ImmutableList.of(new GuiItemStacksInfo(ImmutableList.copyOf(stacks), xOffset + 3, yOffset + 3));
    }
}
