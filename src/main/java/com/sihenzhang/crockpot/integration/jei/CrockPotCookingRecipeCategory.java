package com.sihenzhang.crockpot.integration.jei;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.integration.jei.gui.requirement.AbstractDrawableRequirement;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class CrockPotCookingRecipeCategory implements IRecipeCategory<CrockPotCookingRecipe> {
    public static final RecipeType<CrockPotCookingRecipe> RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "crock_pot_cooking", CrockPotCookingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable priority;
    private final IDrawable time;
    private final LoadingCache<CrockPotCookingRecipe, List<AbstractDrawableRequirement<? extends IRequirement>>> cachedDrawables;

    public CrockPotCookingRecipeCategory(IGuiHelper guiHelper) {
        var recipeGui = RLUtils.createRL("textures/gui/jei/crock_pot_cooking.png");
        this.background = guiHelper.createDrawable(recipeGui, 0, 0, 176, 133);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 80, 0, 16, 16);
        this.priority = guiHelper.createDrawable(recipeGui, 176, 0, 16, 16);
        this.time = guiHelper.createDrawable(recipeGui, 176, 16, 16, 16);
        this.cachedDrawables = CacheBuilder.newBuilder().maximumSize(32).build(new CacheLoader<>() {
            @Override
            public List<AbstractDrawableRequirement<? extends IRequirement>> load(CrockPotCookingRecipe key) {
                return AbstractDrawableRequirement.getDrawables(key.getRequirements());
            }
        });
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends CrockPotCookingRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<CrockPotCookingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "crock_pot_cooking");
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CrockPotCookingRecipe recipe, IFocusGroup focuses) {
        var xOffset = 2;
        var yOffset = 2;
        var maxWidth = 0;
        var drawables = cachedDrawables.getUnchecked(recipe);
        for (var drawable : drawables) {
            if (!drawable.getInvisibleInputs().isEmpty()) {
                builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStacks(drawable.getInvisibleInputs());
            }
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            var guiItemStacksInfos = drawable.getGuiItemStacksInfos(xOffset, yOffset);
            guiItemStacksInfos.forEach(guiItemStacksInfo -> builder.addSlot(guiItemStacksInfo.role, guiItemStacksInfo.x, guiItemStacksInfo.y).addItemStacks(guiItemStacksInfo.stacks));
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
        var pots = ForgeRegistries.BLOCKS.tags().getTag(CrockPotBlockTags.CROCK_POTS).stream()
                .filter(CrockPotBlock.class::isInstance)
                .map(CrockPotBlock.class::cast)
                .filter(pot -> pot.getPotLevel() >= recipe.getPotLevel())
                .map(block -> block.asItem().getDefaultInstance())
                .toList();
        builder.addSlot(RecipeIngredientRole.CATALYST, 62, 104).addItemStacks(pots);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 104, 110).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(CrockPotCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        var cookingTime = recipe.getCookingTime();
        if (cookingTime > 0) {
            time.draw(stack, 0, 117);
            font.draw(stack, I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "crock_pot_cooking.cooking_time.second", cookingTime / 20), 17, 121, 0xFF808080);
        }
        var priorityString = String.valueOf(recipe.getPriority());
        var priorityWidth = font.width(priorityString);
        priority.draw(stack, 159 - priorityWidth, 117);
        font.draw(stack, priorityString, 175.0F - priorityWidth, 121, 0xFF808080);
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
            drawable.draw(stack, xOffset, yOffset);
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
    }

    @Override
    public List<Component> getTooltipStrings(CrockPotCookingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (mouseX >= 0.0 && mouseX <= 16.0 && mouseY >= 117.0 && mouseY <= 133.0) {
            return List.of(I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "crock_pot_cooking.cooking_time"));
        }
        var priorityString = String.valueOf(recipe.getPriority());
        var priorityWidth = Minecraft.getInstance().font.width(priorityString);
        if (mouseX >= 159.0 - priorityWidth && mouseX <= 175.0 - priorityWidth && mouseY >= 117.0 && mouseY <= 133.0) {
            return List.of(I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "crock_pot_cooking.priority"));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}
