package com.sihenzhang.crockpot.integration.jei.category;

import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.RecipeTypes;
import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.IdUtil;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.stream.IntStream;

public class ParrotFeedingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<ParrotFeedingRecipe>> {
    private final IDrawable background;

    public ParrotFeedingRecipeCategory(IGuiHelper guiHelper) {
        super(
                RecipeTypes.PARROT_FEEDING,
                I18nUtil.integration(ModIntegrationJei.MOD_ID, "parrot_feeding"),
                guiHelper.createDrawable(ModIntegrationJei.ICONS, 64, 0, 16, 16),
                87,
                33
        );
        this.background = guiHelper.createDrawable(IdUtil.mod("textures/gui/jei/parrot_feeding.png"), 0, 0, 87, 33);
    }

    @Override
    public void draw(RecipeHolder<ParrotFeedingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<ParrotFeedingRecipe> recipeHolder, IFocusGroup focuses) {
        var recipe = recipeHolder.value();
        var result = recipe.getResult();
        builder.addInputSlot(1, 8).setStandardSlotBackground().add(recipe.getIngredient());
        var outputSlot = builder.addOutputSlot(66, 8).setOutputSlotBackground();
        if (result.isRanged()) {
            var resultList = IntStream.rangeClosed(result.min, result.max)
                    .filter(i -> i != 0)
                    .mapToObj(cnt -> new ItemStack(result.item, cnt))
                    .toList();
            outputSlot.addIngredients(VanillaTypes.ITEM_STACK, resultList).addRichTooltipCallback((_, tooltip) -> {
                tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "min_output", result.min).withStyle(ChatFormatting.GRAY));
                tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "max_output", result.max).withStyle(ChatFormatting.GRAY));
            });
        } else {
            outputSlot.add(new ItemStack(result.item, result.min));
        }
    }
}
