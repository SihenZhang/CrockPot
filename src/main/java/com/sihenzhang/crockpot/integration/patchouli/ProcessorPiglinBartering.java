package com.sihenzhang.crockpot.integration.patchouli;

import com.sihenzhang.crockpot.recipe.WeightedItem;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.util.NbtUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessorPiglinBartering implements IComponentProcessor {
    private PiglinBarteringRecipe recipe;
    private List<IVariable> pagedResults;

    @Override
    public void setup(IVariableProvider variables) {
        recipe = PatchouliUtils.getRecipe(variables.get("recipe").asString());
        pagedResults = PatchouliUtils.pagedItemVariables(recipe.getWeightedResults().stream().map(e -> NbtUtils.setLoreString(e.item.getDefaultInstance(), WeightedItem.getCountAndChance(e, recipe.getWeightedResults()))).collect(Collectors.toList()), 30);
    }

    @Override
    public IVariable process(String key) {
        if ("input".equals(key)) {
            return PatchouliUtils.ingredientVariable(recipe.getIngredient());
        } else if (key.startsWith("output")) {
            int index = Integer.parseInt(key.substring(6)) - 1;
            if (index < 0 || index >= Math.min(recipe.getWeightedResults().size(), 30)) {
                return IVariable.from(ItemStack.EMPTY);
            }
            return pagedResults.get(index);
        } else if ("piglinBarteringTooltip".equals(key)) {
            return IVariable.from(new TranslationTextComponent("integration.crockpot.book.piglin_bartering.piglin_bartering"));
        }
        return null;
    }
}
