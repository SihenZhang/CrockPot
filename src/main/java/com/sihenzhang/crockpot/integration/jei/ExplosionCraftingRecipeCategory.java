package com.sihenzhang.crockpot.integration.jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.jei.gui.DrawableFramed;
import com.sihenzhang.crockpot.recipe.explosion.ExplosionCraftingRecipe;
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
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.text.DecimalFormat;
import java.util.Collections;

public class ExplosionCraftingRecipeCategory implements IRecipeCategory<ExplosionCraftingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CrockPot.MOD_ID, "explosion_crafting");
    private static final DecimalFormat CHANCE_FORMAT = new DecimalFormat("0.##%");
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated animatedExplosion;

    public ExplosionCraftingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/explosion_crafting.png"), 0, 0, 127, 46);
        this.icon = guiHelper.createDrawableIngredient(Items.TNT.getDefaultInstance());
        this.animatedExplosion = new DrawableFramed(guiHelper.createDrawable(new ResourceLocation(CrockPot.MOD_ID, "textures/gui/jei/explosion_crafting.png"), 127, 0, 27, 240), 20, 10, IDrawableAnimated.StartDirection.TOP);
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

        FontRenderer fontRenderer = Minecraft.getInstance().font;
        if (recipe.isOnlyBlock()) {
            ITextComponent onlyBlockTextComponent = new TranslationTextComponent("integration.crockpot.jei.explosion_crafting.only_block");
            int width = fontRenderer.width(onlyBlockTextComponent);
            fontRenderer.draw(matrixStack, onlyBlockTextComponent, 28 - width / 2.0F, 32, 0xFFFF5555);
        }
        String chance = CHANCE_FORMAT.format(1.0F - recipe.getLossRate());
        int width = fontRenderer.width(chance);
        fontRenderer.draw(matrixStack, chance, 97 - width / 2.0F, 36, 0xFF808080);
    }
}
