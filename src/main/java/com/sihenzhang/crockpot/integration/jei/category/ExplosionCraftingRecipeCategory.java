package com.sihenzhang.crockpot.integration.jei.category;

import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.RecipeTypes;
import com.sihenzhang.crockpot.integration.jei.gui.DrawableFramed;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.IdUtil;
import com.sihenzhang.crockpot.util.NumberUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.crafting.RecipeHolder;

public class ExplosionCraftingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<ExplosionCraftingRecipe>> {
    private final IDrawable background;
    private final IDrawableAnimated animatedExplosion;
    private final IDrawable onlyBlock;

    public ExplosionCraftingRecipeCategory(IGuiHelper guiHelper) {
        super(
                RecipeTypes.EXPLOSION_CRAFTING,
                I18nUtil.integration(ModIntegrationJei.MOD_ID, "explosion_crafting"),
                guiHelper.createDrawable(ModIntegrationJei.ICONS, 0, 0, 16, 16),
                127,
                46
        );
        var recipeGui = IdUtil.mod("textures/gui/jei/explosion_crafting.png");
        this.background = guiHelper.createDrawable(recipeGui, 0, 0, 127, 46);
        this.animatedExplosion = new DrawableFramed(guiHelper.createDrawable(recipeGui, 127, 0, 27, 240), 20, 10, IDrawableAnimated.StartDirection.TOP);
        this.onlyBlock = guiHelper.createDrawable(recipeGui, 154, 0, 16, 16);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ExplosionCraftingRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
        builder.addInputSlot(19, 10).setStandardSlotBackground().add(recipe.getIngredient());
        builder.addOutputSlot(88, 10).setOutputSlotBackground().add(recipe.getResult());
    }

    @Override
    public void draw(RecipeHolder<ExplosionCraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        var recipe = recipeHolder.value();
        this.background.draw(guiGraphics);
        animatedExplosion.draw(guiGraphics, 46, 6);
        if (recipe.isOnlyBlock()) {
            onlyBlock.draw(guiGraphics, 21, 29);
        }
        var font = Minecraft.getInstance().font;
        var chance = NumberUtil.decimalFormat("0.##%", 1.0F - recipe.getLossRate());
        guiGraphics.text(font, chance, 97 - font.width(chance) / 2, 36, 0xFF808080, false);
    }

    @Override
    public void getTooltip(ITooltipBuilder tooltip, RecipeHolder<ExplosionCraftingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        if (recipeHolder.value().isOnlyBlock() && mouseX >= 21.0 && mouseX <= 37.0 && mouseY >= 29.0 && mouseY <= 45.0) {
            tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "explosion_crafting.only_block"));
        }
    }
}
