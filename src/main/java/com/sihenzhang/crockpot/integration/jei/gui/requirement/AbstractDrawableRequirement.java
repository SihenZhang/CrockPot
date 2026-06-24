package com.sihenzhang.crockpot.integration.jei.gui.requirement;

import com.google.common.collect.ImmutableList;
import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredient;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMax;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMaxExclusive;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMin;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMinExclusive;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationAnd;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationOr;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredient;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredientLessThan;
import com.sihenzhang.crockpot.util.I18nUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDrawableRequirement<T extends IRequirement> implements IDrawable {
    protected static final int TEXT_COLOR = 0xFF000000;

    protected final T requirement;
    protected final Component description;

    protected AbstractDrawableRequirement(T requirement, Component description) {
        this.requirement = requirement;
        this.description = description;
    }

    @Override
    public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        this.drawRequirementBackground(guiGraphics, xOffset, yOffset);
    }

    private void drawRequirementBackground(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
        guiGraphics.fill(xOffset, yOffset, xOffset + this.getWidth(), yOffset + this.getHeight(), 0xFFE8E1C8);
        guiGraphics.outline(xOffset, yOffset, this.getWidth(), this.getHeight(), 0xFF8F7A4A);
    }

    public abstract List<ItemStack> getInvisibleInputs();

    public abstract List<GuiIngredientInfo> getGuiIngredientInfos(int xOffset, int yOffset);

    public static AbstractDrawableRequirement<? extends IRequirement> createDrawable(IRequirement requirement) {
        if (requirement instanceof RequirementCategoryMax requirementCategoryMax) {
            return new DrawableRequirementCategoryMax(requirementCategoryMax);
        } else if (requirement instanceof RequirementCategoryMaxExclusive requirementCategoryMaxExclusive) {
            return new DrawableRequirementCategoryMaxExclusive(requirementCategoryMaxExclusive);
        } else if (requirement instanceof RequirementCategoryMin requirementCategoryMin) {
            return new DrawableRequirementCategoryMin(requirementCategoryMin);
        } else if (requirement instanceof RequirementCategoryMinExclusive requirementCategoryMinExclusive) {
            return new DrawableRequirementCategoryMinExclusive(requirementCategoryMinExclusive);
        } else if (requirement instanceof RequirementCombinationAnd requirementCombinationAnd) {
            return new DrawableRequirementCombinationAnd(requirementCombinationAnd);
        } else if (requirement instanceof RequirementCombinationOr requirementCombinationOr) {
            return new DrawableRequirementCombinationOr(requirementCombinationOr);
        } else if (requirement instanceof RequirementMustContainIngredient requirementMustContainIngredient) {
            return new DrawableRequirementMustContainIngredient(requirementMustContainIngredient);
        } else if (requirement instanceof RequirementMustContainIngredientLessThan requirementMustContainIngredientLessThan) {
            return new DrawableRequirementMustContainIngredientLessThan(requirementMustContainIngredientLessThan);
        }
        throw new IllegalArgumentException("No valid requirement was found");
    }

    public static List<AbstractDrawableRequirement<? extends IRequirement>> getDrawables(List<IRequirement> requirements) {
        ImmutableList.Builder<AbstractDrawableRequirement<? extends IRequirement>> builder = ImmutableList.builder();
        if (requirements.isEmpty()) {
            builder.add(new AbstractDrawableRequirement<>(null, I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking.requirement.no_requirement")) {
                @Override
                public int getWidth() {
                    return 6 + Minecraft.getInstance().font.width(description);
                }

                @Override
                public int getHeight() {
                    return 14;
                }

                @Override
                public void draw(GuiGraphicsExtractor guiGraphics, int xOffset, int yOffset) {
                    super.draw(guiGraphics, xOffset, yOffset);
                    guiGraphics.text(Minecraft.getInstance().font, description, xOffset + 3, yOffset + 3, TEXT_COLOR, false);
                }

                @Override
                public List<ItemStack> getInvisibleInputs() {
                    return List.of();
                }

                @Override
                public List<GuiIngredientInfo> getGuiIngredientInfos(int xOffset, int yOffset) {
                    return List.of();
                }
            });
        } else {
            List<IRequirement> tmpRequirements = new ArrayList<>(requirements);
            Iterator<IRequirement> it = tmpRequirements.iterator();
            while (it.hasNext()) {
                IRequirement requirement = it.next();
                if (requirement instanceof RequirementMustContainIngredient mustContain) {
                    Optional<RequirementMustContainIngredientLessThan> lessThan = tmpRequirements.stream()
                            .filter(RequirementMustContainIngredientLessThan.class::isInstance)
                            .map(RequirementMustContainIngredientLessThan.class::cast)
                            .filter(r -> mustContain.getQuantity() == r.getQuantity())
                            .filter(r -> isSameIngredient(mustContain.getIngredient(), r.getIngredient()))
                            .findFirst();
                    builder.add(lessThan
                            .<AbstractDrawableRequirement<? extends IRequirement>>map(r -> new DrawableRequirementMustContainIngredient(mustContain, r))
                            .orElseGet(() -> AbstractDrawableRequirement.createDrawable(requirement)));
                } else if (requirement instanceof RequirementMustContainIngredientLessThan lessThan) {
                    Optional<RequirementMustContainIngredient> mustContain = tmpRequirements.stream()
                            .filter(RequirementMustContainIngredient.class::isInstance)
                            .map(RequirementMustContainIngredient.class::cast)
                            .filter(r -> lessThan.getQuantity() == r.getQuantity())
                            .filter(r -> isSameIngredient(lessThan.getIngredient(), r.getIngredient()))
                            .findFirst();
                    if (mustContain.isPresent()) {
                        it.remove();
                    } else {
                        builder.add(AbstractDrawableRequirement.createDrawable(requirement));
                    }
                } else {
                    builder.add(AbstractDrawableRequirement.createDrawable(requirement));
                }
            }
        }
        return builder.build();
    }

    private static boolean isSameIngredient(Ingredient first, Ingredient second) {
        if (first.equals(second)) {
            return true;
        }
        var firstItems = new HashSet<>(first.items().toList());
        var secondItems = new HashSet<>(second.items().toList());
        return firstItems.equals(secondItems);
    }

    public record GuiIngredientInfo(
            RecipeIngredientRole role,
            List<ItemStack> stacks,
            FoodCategoryIngredient foodCategory,
            int x,
            int y
    ) {
        public GuiIngredientInfo(List<ItemStack> stacks, int x, int y, boolean isRenderOnly) {
            this(isRenderOnly ? RecipeIngredientRole.RENDER_ONLY : RecipeIngredientRole.INPUT, List.copyOf(stacks), null, x, y);
        }

        public GuiIngredientInfo(List<ItemStack> stacks, int x, int y) {
            this(stacks, x, y, false);
        }

        public GuiIngredientInfo(FoodCategoryIngredient foodCategory, int x, int y, boolean isRenderOnly) {
            this(isRenderOnly ? RecipeIngredientRole.RENDER_ONLY : RecipeIngredientRole.INPUT, List.of(), foodCategory, x, y);
        }

        public GuiIngredientInfo(FoodCategoryIngredient foodCategory, int x, int y) {
            this(foodCategory, x, y, false);
        }

        public void addTo(IRecipeLayoutBuilder builder) {
            var slot = builder.addSlot(role, x, y);
            if (foodCategory != null) {
                slot.add(FoodCategoryIngredient.TYPE, foodCategory);
            } else {
                slot.addItemStacks(stacks);
            }
        }
    }
}
