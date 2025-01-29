package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.ITooltipBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.IntStream;

public class ParrotFeedingRecipeCategory implements IRecipeCategory<ParrotFeedingRecipe> {
    public static final RecipeType<ParrotFeedingRecipe> RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "parrot_feeding", ParrotFeedingRecipe.class);
    private final IDrawable background;
    private final IDrawable icon;

    public ParrotFeedingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/parrot_feeding.png"), 0, 0, 87, 33);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 64, 0, 16, 16);
    }

    @Override
    public RecipeType<ParrotFeedingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "parrot_feeding");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ParrotFeedingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 8).addIngredients(recipe.getIngredients().get(0));
        var result = recipe.getResult();
        if (result.isRanged()) {
            var resultList = IntStream.rangeClosed(result.min, result.max)
                    .filter(i -> i != 0)
                    .mapToObj(cnt -> new ItemStack(result.item, cnt))
                    .toList();
            builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 8).addItemStacks(resultList).addTooltipCallback(new IRecipeSlotTooltipCallback() {
                @Override
                @SuppressWarnings("removal")
                public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
                    tooltip.add(I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "min_output", result.min).withStyle(ChatFormatting.GRAY));
                    tooltip.add(I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "max_output", result.max).withStyle(ChatFormatting.GRAY));
                }

                @Override
                public void onRichTooltip(IRecipeSlotView recipeSlotView, ITooltipBuilder tooltip) {
                    tooltip.add(I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "min_output", result.min).withStyle(ChatFormatting.GRAY));
                    tooltip.add(I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "max_output", result.max).withStyle(ChatFormatting.GRAY));
                }
            });
        } else {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 8).addItemStack(new ItemStack(result.item, result.min));
        }
    }
}
