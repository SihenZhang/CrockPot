package com.sihenzhang.crockpot.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.jei.gui.DrawableFramed;
import com.sihenzhang.crockpot.recipe.explosion.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.util.MathUtils;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collections;
import java.util.List;

public class ExplosionCraftingRecipeCategory implements IRecipeCategory<ExplosionCraftingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrockPot.MOD_ID, "explosion_crafting");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated animatedExplosion;
    private final IDrawable onlyBlock;

    public ExplosionCraftingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/explosion_crafting.png"), 0, 0, 127, 46);
        this.icon = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/icons.png"), 0, 0, 16, 16);
        this.animatedExplosion = new DrawableFramed(guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/explosion_crafting.png"), 127, 0, 27, 240), 20, 10, IDrawableAnimated.StartDirection.TOP);
        this.onlyBlock = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/explosion_crafting.png"), 154, 0, 16, 16);
    }

    @Override
    public ResourceLocation getUid() {
        return ExplosionCraftingRecipeCategory.UID;
    }

    @Override
    public Class<? extends ExplosionCraftingRecipe> getRecipeClass() {
        return ExplosionCraftingRecipe.class;
    }

    @Override
    public String getTitle() {
        return getTitleAsTextComponent().getString();
    }

    @Override
    public ITextComponent getTitleAsTextComponent() {
        return new TranslationTextComponent("integration.crockpot.jei.explosion_crafting");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    @Override
    public void setIngredients(ExplosionCraftingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(Collections.singletonList(recipe.getInput()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getOutput().getDefaultInstance());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, ExplosionCraftingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();
        guiItemStacks.init(0, true, 18, 9);
        guiItemStacks.init(1, false, 87, 9);
        guiItemStacks.set(ingredients);
    }

    @Override
    public void draw(ExplosionCraftingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        this.animatedExplosion.draw(matrixStack, 46, 6);

        if (recipe.isOnlyBlock()) {
            this.onlyBlock.draw(matrixStack, 21, 29);
        }

        FontRenderer fontRenderer = Minecraft.getInstance().font;
        String chance = MathUtils.format(1.0F - recipe.getLossRate(), "0.##%");
        int width = fontRenderer.width(chance);
        fontRenderer.draw(matrixStack, chance, 97 - width / 2.0F, 36, 0xFF808080);
    }

    @Override
    public List<ITextComponent> getTooltipStrings(ExplosionCraftingRecipe recipe, double mouseX, double mouseY) {
        if (recipe.isOnlyBlock() && mouseX >= 21.0 && mouseX <= 37.0 && mouseY >= 29.0 && mouseY <= 45.0) {
            return Collections.singletonList(new TranslationTextComponent("integration.crockpot.jei.explosion_crafting.only_block"));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, mouseX, mouseY);
    }
}
