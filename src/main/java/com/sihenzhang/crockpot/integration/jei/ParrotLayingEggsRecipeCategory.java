package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.NbtUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.IntStream;

public class ParrotLayingEggsRecipeCategory implements IRecipeCategory<ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper> {
    public static final RecipeType<ParrotLayingEggsRecipeWrapper> RECIPE_TYPE = RecipeType.create(CrockPot.MOD_ID, "parrot_laying_eggs", ParrotLayingEggsRecipeWrapper.class);
    private final IDrawable background;
    private final IDrawable icon;

    public ParrotLayingEggsRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/parrot_feeding.png"), 0, 0, 87, 33);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 48, 0, 16, 16);
    }

    @Override
    public RecipeType<ParrotLayingEggsRecipeWrapper> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent(ModIntegrationJei.MOD_ID, "parrot_laying_eggs");
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
    public void setRecipe(IRecipeLayoutBuilder builder, ParrotLayingEggsRecipeWrapper recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 8).addIngredients(recipe.ingredient);
        var eggs = ForgeRegistries.ITEMS.tags().getTag(CrockPotItemTags.PARROT_EGGS).stream().toList();
        var counts = IntStream.rangeClosed(recipe.min, recipe.max).filter(i -> i != 0).toArray();
        var result = IntStream.range(0, eggs.size() * counts.length).mapToObj(i -> {
            var egg = eggs.get(i % eggs.size());
            var count = counts[i % counts.length];
            var stack = new ItemStack(egg, count);
            if (recipe.min != recipe.max) {
                NbtUtils.setLoreString(stack, recipe.min + "-" + recipe.max);
            }
            return stack;
        }).toList();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 8).addItemStacks(result);
    }

    public static class ParrotLayingEggsRecipeWrapper {
        Ingredient ingredient;
        int min;
        int max;

        public ParrotLayingEggsRecipeWrapper(Ingredient ingredient, int min, int max) {
            this.ingredient = ingredient;
            this.min = min;
            this.max = max;
        }
    }
}
