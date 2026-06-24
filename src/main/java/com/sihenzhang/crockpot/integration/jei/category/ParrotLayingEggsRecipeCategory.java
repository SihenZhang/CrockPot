package com.sihenzhang.crockpot.integration.jei.category;

import com.sihenzhang.crockpot.integration.jei.ModIntegrationJei;
import com.sihenzhang.crockpot.integration.jei.RecipeTypes;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.tag.ModItemTags;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.IdUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.stream.IntStream;

public class ParrotLayingEggsRecipeCategory extends AbstractRecipeCategory<ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper> {
    private final IDrawable background;

    public ParrotLayingEggsRecipeCategory(IGuiHelper guiHelper) {
        super(
                RecipeTypes.PARROT_LAYING_EGGS,
                I18nUtil.integration(ModIntegrationJei.MOD_ID, "parrot_laying_eggs"),
                guiHelper.createDrawable(ModIntegrationJei.ICONS, 48, 0, 16, 16),
                87,
                33
        );
        this.background = guiHelper.createDrawable(IdUtil.mod("textures/gui/jei/parrot_feeding.png"), 0, 0, 87, 33);
    }

    @Override
    public void draw(ParrotLayingEggsRecipeWrapper recipe, mezz.jei.api.gui.ingredient.IRecipeSlotsView recipeSlotsView, net.minecraft.client.gui.GuiGraphicsExtractor guiGraphics, double mouseX, double mouseY) {
        this.background.draw(guiGraphics);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, ParrotLayingEggsRecipeWrapper recipe, IFocusGroup focuses) {
        builder.addInputSlot(1, 8).setStandardSlotBackground().addItemStacks(recipe.inputStacks);
        var eggs = getParrotEggs();
        var counts = IntStream.rangeClosed(recipe.min, recipe.max).filter(i -> i != 0).toArray();
        var result = IntStream.range(0, eggs.size() * counts.length).mapToObj(i -> {
            var egg = eggs.get(i % eggs.size());
            var count = counts[i % counts.length];
            return new ItemStack(egg, count);
        }).toList();
        builder.addOutputSlot(66, 8).setOutputSlotBackground().addItemStacks(result).addRichTooltipCallback((_, tooltip) -> {
            if (recipe.min != recipe.max) {
                tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "min_output", recipe.min).withStyle(ChatFormatting.GRAY));
                tooltip.add(I18nUtil.integration(ModIntegrationJei.MOD_ID, "max_output", recipe.max).withStyle(ChatFormatting.GRAY));
            }
        });
    }

    private static List<Item> getParrotEggs() {
        var taggedEggs = BuiltInRegistries.ITEM.get(ModItemTags.PARROT_EGGS)
                .map(holders -> holders.stream().map(holder -> holder.value()).toList())
                .orElse(List.of());
        if (!taggedEggs.isEmpty()) {
            return taggedEggs;
        }
        return ModItems.PARROT_EGGS.values().stream()
                .map(egg -> (Item) egg.get())
                .toList();
    }

    public static class ParrotLayingEggsRecipeWrapper {
        private final List<ItemStack> inputStacks;
        private final int min;
        private final int max;

        public ParrotLayingEggsRecipeWrapper(List<ItemStack> inputStacks, int min, int max) {
            this.inputStacks = List.copyOf(inputStacks);
            this.min = min;
            this.max = max;
        }
    }
}
