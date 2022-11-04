package com.sihenzhang.crockpot.integration.jei;

import com.mojang.blaze3d.vertex.PoseStack;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.jei.gui.DrawableFramed;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.util.RLUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.util.Collections;
import java.util.List;

public class ExplosionCraftingRecipeCategory implements IRecipeCategory<ExplosionCraftingRecipe> {
    public static final RecipeType<ExplosionCraftingRecipe> RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "explosion_crafting", ExplosionCraftingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableAnimated animatedExplosion;
    private final IDrawable onlyBlock;

    public ExplosionCraftingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/explosion_crafting.png"), 0, 0, 127, 46);
        this.icon = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/icons.png"), 0, 0, 16, 16);
        this.animatedExplosion = new DrawableFramed(guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/explosion_crafting.png"), 127, 0, 27, 240), 20, 10, IDrawableAnimated.StartDirection.TOP);
        this.onlyBlock = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/explosion_crafting.png"), 154, 0, 16, 16);
    }

    @SuppressWarnings("removal")
    @Override
    public ResourceLocation getUid() {
        return this.getRecipeType().getUid();
    }

    @SuppressWarnings("removal")
    @Override
    public Class<? extends ExplosionCraftingRecipe> getRecipeClass() {
        return this.getRecipeType().getRecipeClass();
    }

    @Override
    public RecipeType<ExplosionCraftingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return new TranslatableComponent("integration.crockpot.jei.explosion_crafting");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ExplosionCraftingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 10).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 88, 10).addItemStack(recipe.getResult());
    }

    @Override
    public void draw(ExplosionCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, PoseStack stack, double mouseX, double mouseY) {
        this.animatedExplosion.draw(stack, 46, 6);

        if (recipe.isOnlyBlock()) {
            this.onlyBlock.draw(stack, 21, 29);
        }

        Font font = Minecraft.getInstance().font;
        String chance = StringUtils.format(1.0F - recipe.getLossRate(), "0.##%");
        int width = font.width(chance);
        font.draw(stack, chance, 97 - width / 2.0F, 36, 0xFF808080);
    }

    @Override
    public List<Component> getTooltipStrings(ExplosionCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (recipe.isOnlyBlock() && mouseX >= 21.0 && mouseX <= 37.0 && mouseY >= 29.0 && mouseY <= 45.0) {
            return Collections.singletonList(new TranslatableComponent("integration.crockpot.jei.explosion_crafting.only_block"));
        }
        return IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}
