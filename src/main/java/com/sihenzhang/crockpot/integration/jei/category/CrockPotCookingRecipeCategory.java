package com.sihenzhang.crockpot.integration.jei.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.RecipeTypes;
import com.sihenzhang.crockpot.integration.jei.gui.requirement.AbstractDrawableRequirement;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.tag.ModBlockTags;
import com.sihenzhang.crockpot.util.I18nUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;

public class CrockPotCookingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<CrockPotCookingRecipe>> {
    private final IDrawable background;
    private final IDrawable priority;
    private final IDrawable time;
    private final LoadingCache<CrockPotCookingRecipe, List<AbstractDrawableRequirement<? extends IRequirement>>> cachedDrawables;

    public CrockPotCookingRecipeCategory(IGuiHelper guiHelper) {
        super(
                RecipeTypes.CROCK_POT_COOKING,
                I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking"),
                guiHelper.createDrawable(ModIntegrationJei.ICONS, 80, 0, 16, 16),
                176,
                133
        );
        var recipeGui = com.sihenzhang.crockpot.util.IdUtil.mod("textures/gui/jei/crock_pot_cooking.png");
        this.background = guiHelper.createDrawable(recipeGui, 0, 0, 176, 133);
        this.priority = guiHelper.createDrawable(recipeGui, 176, 0, 16, 16);
        this.time = guiHelper.createDrawable(recipeGui, 176, 16, 16, 16);
        this.cachedDrawables = CacheBuilder.newBuilder().maximumSize(32).build(new CacheLoader<>() {
            @Override
            public List<AbstractDrawableRequirement<? extends IRequirement>> load(CrockPotCookingRecipe key) {
                return AbstractDrawableRequirement.getDrawables(key.getRequirements());
            }
        });
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<CrockPotCookingRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
        var xOffset = 2;
        var yOffset = 2;
        var maxWidth = 0;
        var drawables = cachedDrawables.getUnchecked(recipe);
        for (var drawable : drawables) {
            var invisibleInputs = drawable.getInvisibleInputs();
            if (!invisibleInputs.isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(invisibleInputs);
            }
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            drawable.getGuiIngredientInfos(xOffset, yOffset).forEach(info -> info.addTo(builder));
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
        var pots = BuiltInRegistries.BLOCK.get(ModBlockTags.CROCK_POTS).stream()
                .flatMap(holders -> holders.stream())
                .map(holder -> holder.value())
                .filter(CrockPotBlock.class::isInstance)
                .map(CrockPotBlock.class::cast)
                .filter(pot -> pot.getPotLevel() >= recipe.getPotLevel())
                .map(block -> block.asItem().getDefaultInstance())
                .toList();
        builder.addSlot(RecipeIngredientRole.CRAFTING_STATION, 62, 104).setStandardSlotBackground().addItemStacks(pots);
        builder.addOutputSlot(104, 110).setOutputSlotBackground().add(recipe.getResult());
    }

    @Override
    public void draw(RecipeHolder<CrockPotCookingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        var recipe = recipeHolder.value();
        this.background.draw(guiGraphics);
        var font = Minecraft.getInstance().font;
        var cookingTime = recipe.getCookingTime();
        if (cookingTime > 0) {
            time.draw(guiGraphics, 0, 117);
            guiGraphics.text(font, I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking.cooking_time.second", cookingTime / 20), 17, 121, 0xFF808080, false);
        }
        var priorityString = String.valueOf(recipe.getPriority());
        var priorityWidth = font.width(priorityString);
        priority.draw(guiGraphics, 159 - priorityWidth, 117);
        guiGraphics.text(font, priorityString, 175 - priorityWidth, 121, 0xFF808080, false);
        var xOffset = 2;
        var yOffset = 2;
        var maxWidth = 0;
        var drawables = cachedDrawables.getUnchecked(recipe);
        for (var drawable : drawables) {
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            drawable.draw(guiGraphics, xOffset, yOffset);
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<CrockPotCookingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        var recipe = recipeHolder.value();
        if (mouseX >= 0.0 && mouseX <= 16.0 && mouseY >= 117.0 && mouseY <= 133.0) {
            tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking.cooking_time"));
        }
        var priorityString = String.valueOf(recipe.getPriority());
        var priorityWidth = Minecraft.getInstance().font.width(priorityString);
        if (mouseX >= 159.0 - priorityWidth && mouseX <= 175.0 - priorityWidth && mouseY >= 117.0 && mouseY <= 133.0) {
            tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "crock_pot_cooking.priority"));
        }
    }
}
