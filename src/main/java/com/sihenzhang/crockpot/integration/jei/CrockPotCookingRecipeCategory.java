package com.sihenzhang.crockpot.integration.jei;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.integration.jei.gui.requirement.AbstractDrawableRequirement;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CrockPotCookingRecipeCategory implements IRecipeCategory<CrockPotCookingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrockPot.MOD_ID, "crock_pot_cooking");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable priority;
    private final IDrawable time;
    private final LoadingCache<CrockPotCookingRecipe, List<AbstractDrawableRequirement<? extends IRequirement>>> cachedDrawables;

    public static final List<ItemStack> POTS = ImmutableList.of(
            CrockPotRegistry.crockPotBasicBlockItem.getDefaultInstance(),
            CrockPotRegistry.crockPotAdvancedBlockItem.getDefaultInstance(),
            CrockPotRegistry.crockPotUltimateBlockItem.getDefaultInstance()
    );

    public CrockPotCookingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/crock_pot_cooking.png"), 0, 0, 176, 133);
        this.icon = guiHelper.createDrawableIngredient(CrockPotRegistry.crockPotBasicBlockItem.getDefaultInstance());
        this.priority = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/crock_pot_cooking.png"), 176, 0, 16, 16);
        this.time = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/crock_pot_cooking.png"), 176, 16, 16, 16);
        this.cachedDrawables = CacheBuilder.newBuilder().maximumSize(32).build(new CacheLoader<CrockPotCookingRecipe, List<AbstractDrawableRequirement<? extends IRequirement>>>() {
            @Override
            public List<AbstractDrawableRequirement<? extends IRequirement>> load(CrockPotCookingRecipe key) {
                return AbstractDrawableRequirement.getDrawables(key.getRequirements());
            }
        });
    }

    @Override
    public ResourceLocation getUid() {
        return CrockPotCookingRecipeCategory.UID;
    }

    @Override
    public Class<? extends CrockPotCookingRecipe> getRecipeClass() {
        return CrockPotCookingRecipe.class;
    }

    @Override
    public String getTitle() {
        return getTitleAsTextComponent().getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("integration.crockpot.jei.crock_pot_cooking");
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
    public void setIngredients(CrockPotCookingRecipe recipe, IIngredients ingredients) {
        List<List<ItemStack>> inputLists = new ArrayList<>();
        List<ItemStack> pots = new ArrayList<>();
        for (int i = 0; i < POTS.size(); i++) {
            if (i >= recipe.getPotLevel()) {
                pots.add(POTS.get(i));
            }
        }
        inputLists.add(pots);
        List<AbstractDrawableRequirement<? extends IRequirement>> drawables = cachedDrawables.getUnchecked(recipe);
        drawables.forEach(drawable -> inputLists.addAll(drawable.getInputLists()));
        ingredients.setInputLists(VanillaTypes.ITEM, inputLists);
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResult());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CrockPotCookingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        int slot = 0;
        guiItemStacks.init(slot, true, 61, 103);
        guiItemStacks.set(slot++, ingredients.getInputs(VanillaTypes.ITEM).get(0));
        guiItemStacks.init(slot, false, 103, 109);
        guiItemStacks.set(slot++, ingredients.getOutputs(VanillaTypes.ITEM).get(0));

        int xOffset = 2;
        int yOffset = 2;
        int maxWidth = 0;
        List<AbstractDrawableRequirement<? extends IRequirement>> drawables = cachedDrawables.getUnchecked(recipe);
        for (AbstractDrawableRequirement<? extends IRequirement> drawable : drawables) {
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            List<AbstractDrawableRequirement.GuiItemStacksInfo> guiItemStacksInfos = drawable.getGuiItemStacksInfos(xOffset, yOffset);
            for (AbstractDrawableRequirement.GuiItemStacksInfo guiItemStacksInfo : guiItemStacksInfos) {
                guiItemStacks.init(slot, true, guiItemStacksInfo.x - 1, guiItemStacksInfo.y - 1);
                guiItemStacks.set(slot++, guiItemStacksInfo.stacks);
            }
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
    }

    @Override
    public void draw(CrockPotCookingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        FontRenderer font = Minecraft.getInstance().font;

        int cookingTime = recipe.getCookingTime();
        if (cookingTime > 0) {
            int cookingTimeSeconds = cookingTime / 20;
            time.draw(matrixStack, 0, 117);
            font.draw(matrixStack, new TranslationTextComponent("integration.crockpot.jei.crock_pot_cooking.cooking_time.second", cookingTimeSeconds), 17, 121, 0xFF808080);
        }
        String priorityString = String.valueOf(recipe.getPriority());
        int priorityWidth = font.width(priorityString);
        priority.draw(matrixStack, 159 - priorityWidth, 117);
        font.draw(matrixStack, priorityString, 175 - priorityWidth, 121, 0xFF808080);

        int xOffset = 2;
        int yOffset = 2;
        int maxWidth = 0;
        List<AbstractDrawableRequirement<? extends IRequirement>> drawables = cachedDrawables.getUnchecked(recipe);
        for (AbstractDrawableRequirement<? extends IRequirement> drawable : drawables) {
            if (yOffset != 2 && yOffset + drawable.getHeight() > 96) {
                xOffset += maxWidth + 2;
                yOffset = 2;
                maxWidth = 0;
            }
            drawable.draw(matrixStack, xOffset, yOffset);
            maxWidth = Math.max(drawable.getWidth(), maxWidth);
            yOffset += drawable.getHeight() + 2;
        }
    }

    @Override
    public List<ITextComponent> getTooltipStrings(CrockPotCookingRecipe recipe, double mouseX, double mouseY) {
        if (mouseX >= 0.0 && mouseX <= 16.0 && mouseY >= 117.0 && mouseY <= 133.0) {
            return Collections.singletonList(new TranslationTextComponent("integration.crockpot.jei.crock_pot_cooking.cooking_time"));
        }
        String priorityString = String.valueOf(recipe.getPriority());
        int priorityWidth = Minecraft.getInstance().font.width(priorityString);
        if (mouseX >= 159.0 - priorityWidth && mouseX <= 175.0 - priorityWidth && mouseY >= 117.0 && mouseY <= 133.0) {
            return Collections.singletonList(new TranslationTextComponent("integration.crockpot.jei.crock_pot_cooking.priority"));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, mouseX, mouseY);
    }
}
