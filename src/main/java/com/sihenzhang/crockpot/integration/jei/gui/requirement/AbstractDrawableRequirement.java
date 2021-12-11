package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.jei.gui.DrawableNineSliceResource;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import it.unimi.dsi.fastutil.ints.IntList;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDrawableRequirement<T extends IRequirement> implements IDrawable {
    protected final T requirement;
    protected final Component description;

    public AbstractDrawableRequirement(T requirement, Component description) {
        this.requirement = requirement;
        this.description = description;
    }

    @Override
    public void draw(PoseStack stack, int xOffset, int yOffset) {
        this.drawRequirementBackground(stack, xOffset, yOffset);
    }

    private void drawRequirementBackground(PoseStack stack, int xOffset, int yOffset) {
//        RenderSystem.enableAlphaTest();
        IDrawable drawable = new DrawableNineSliceResource(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/requirement_background.png"), 0, 0, 64, 64, this.getWidth(), this.getHeight(), 8, 8, 8, 8, 64, 64);
        drawable.draw(stack, xOffset, yOffset);
//        RenderSystem.disableAlphaTest();
    }

    public abstract List<GuiItemStacksInfo> getGuiItemStacksInfos(int xOffset, int yOffset);

    public abstract List<List<ItemStack>> getInputLists();

    public static AbstractDrawableRequirement<? extends IRequirement> createDrawable(IRequirement requirement) {
        if (requirement instanceof RequirementCategoryMax) {
            return new DrawableRequirementCategoryMax((RequirementCategoryMax) requirement);
        } else if (requirement instanceof RequirementCategoryMaxExclusive) {
            return new DrawableRequirementCategoryMaxExclusive((RequirementCategoryMaxExclusive) requirement);
        } else if (requirement instanceof RequirementCategoryMin) {
            return new DrawableRequirementCategoryMin((RequirementCategoryMin) requirement);
        } else if (requirement instanceof RequirementCategoryMinExclusive) {
            return new DrawableRequirementCategoryMinExclusive((RequirementCategoryMinExclusive) requirement);
        } else if (requirement instanceof RequirementCombinationAnd) {
            return new DrawableRequirementCombinationAnd((RequirementCombinationAnd) requirement);
        } else if (requirement instanceof RequirementCombinationOr) {
            return new DrawableRequirementCombinationOr((RequirementCombinationOr) requirement);
        } else if (requirement instanceof RequirementMustContainIngredient) {
            return new DrawableRequirementMustContainIngredient((RequirementMustContainIngredient) requirement);
        } else if (requirement instanceof RequirementMustContainIngredientLessThan) {
            return new DrawableRequirementMustContainIngredientLessThan((RequirementMustContainIngredientLessThan) requirement);
        }
        throw new IllegalArgumentException("No valid requirement was found");
    }

    public static List<AbstractDrawableRequirement<? extends IRequirement>> getDrawables(List<IRequirement> requirements) {
        ImmutableList.Builder<AbstractDrawableRequirement<? extends IRequirement>> builder = ImmutableList.builder();
        if (requirements.isEmpty()) {
            builder.add(new AbstractDrawableRequirement<IRequirement>(null, new TranslatableComponent("integration.crockpot.jei.crock_pot_cooking.requirement.no_requirement")) {
                @Override
                public int getWidth() {
                    return 6 + Minecraft.getInstance().font.width(description);
                }

                @Override
                public int getHeight() {
                    return 14;
                }

                @Override
                public void draw(PoseStack stack, int xOffset, int yOffset) {
                    super.draw(stack, xOffset, yOffset);
                    Minecraft.getInstance().font.draw(stack, description, xOffset + 3, yOffset + 3, 0);
                }

                @Override
                public List<List<ItemStack>> getInputLists() {
                    return ImmutableList.of();
                }

                @Override
                public List<GuiItemStacksInfo> getGuiItemStacksInfos(int xOffset, int yOffset) {
                    return ImmutableList.of();
                }
            });
        } else {
            List<IRequirement> tmpRequirements = new ArrayList<>(requirements);
            Iterator<IRequirement> it = tmpRequirements.iterator();
            while (it.hasNext()) {
                IRequirement requirement = it.next();
                if (requirement instanceof RequirementMustContainIngredient || requirement instanceof RequirementMustContainIngredientLessThan) {
                    if (requirement instanceof RequirementMustContainIngredient requirementMustContainIngredient) {
                        Optional<RequirementMustContainIngredientLessThan> requirementMustContainIngredientLessThan = tmpRequirements.stream()
                                .filter(r -> r instanceof RequirementMustContainIngredientLessThan)
                                .map(RequirementMustContainIngredientLessThan.class::cast)
                                .filter(r -> {
                                    if (requirementMustContainIngredient.getQuantity() != r.getQuantity()) {
                                        return false;
                                    }
                                    IntList first = requirementMustContainIngredient.getIngredient().getStackingIds();
                                    IntList second = r.getIngredient().getStackingIds();
                                    return first.size() == second.size() && first.containsAll(second) && second.containsAll(first);
                                }).findFirst();
                        if (requirementMustContainIngredientLessThan.isPresent()) {
                            builder.add(new DrawableRequirementMustContainIngredient(requirementMustContainIngredient, requirementMustContainIngredientLessThan.get()));
                        } else {
                            builder.add(AbstractDrawableRequirement.createDrawable(requirement));
                        }
                    } else {
                        RequirementMustContainIngredientLessThan requirementMustContainIngredientLessThan = (RequirementMustContainIngredientLessThan) requirement;
                        Optional<RequirementMustContainIngredient> requirementMustContainIngredient = tmpRequirements.stream()
                                .filter(r -> r instanceof RequirementMustContainIngredient)
                                .map(RequirementMustContainIngredient.class::cast)
                                .filter(r -> {
                                    if (requirementMustContainIngredientLessThan.getQuantity() != r.getQuantity()) {
                                        return false;
                                    }
                                    IntList first = requirementMustContainIngredientLessThan.getIngredient().getStackingIds();
                                    IntList second = r.getIngredient().getStackingIds();
                                    return first.size() == second.size() && first.containsAll(second) && second.containsAll(first);
                                }).findFirst();
                        if (requirementMustContainIngredient.isPresent()) {
                            it.remove();
                        } else {
                            builder.add(AbstractDrawableRequirement.createDrawable(requirement));
                        }
                    }
                } else {
                    builder.add(AbstractDrawableRequirement.createDrawable(requirement));
                }
            }
        }
        return builder.build();
    }

    public static class GuiItemStacksInfo {
        public List<ItemStack> stacks;
        public int x;
        public int y;

        public GuiItemStacksInfo(List<ItemStack> stacks, int x, int y) {
            this.stacks = stacks;
            this.x = x;
            this.y = y;
        }
    }
}
