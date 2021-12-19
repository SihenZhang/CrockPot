package com.sihenzhang.crockpot.integration.patchouli;

import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.util.NbtUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
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
        pagedResults = PatchouliUtils.pagedItemVariables(recipe.getWeightedResults().unwrap().stream().map(e -> NbtUtils.setLoreString(e.getData().item.getDefaultInstance(), StringUtils.formatCountAndChance(e, recipe.getWeightedResults().totalWeight))).collect(Collectors.toList()), 30);
    }

    @Override
    public IVariable process(String key) {
        if ("input".equals(key)) {
            return PatchouliUtils.ingredientVariable(recipe.getIngredient());
        } else if (key.startsWith("output")) {
            int index = Integer.parseInt(key.substring(6)) - 1;
            if (index < 0 || index >= Math.min(recipe.getWeightedResults().unwrap().size(), 30)) {
                return IVariable.from(ItemStack.EMPTY);
            }
            return pagedResults.get(index);
        } else if ("piglinBarteringTooltip".equals(key)) {
            return IVariable.from(new TranslatableComponent("integration.crockpot.book.piglin_bartering.piglin_bartering"));
        }
        return null;
    }
}
