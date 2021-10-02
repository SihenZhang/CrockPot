package com.sihenzhang.crockpot.integration.patchouli;

import com.google.common.base.Preconditions;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.WeightedItem;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.util.NbtUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessorPiglinBartering implements IComponentProcessor {
    private PiglinBarteringRecipe recipe;
    private List<IVariable> pagedOutputs;

    @Override
    public void setup(IVariableProvider variables) {
        String input = variables.get("input").asString();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(input));
        Preconditions.checkArgument(item != null, "input cannot be null");
        recipe = CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.match(item);
        pagedOutputs = PatchouliUtils.pagedItemVariables(recipe.getWeightedOutputs().stream().map(e -> NbtUtils.setLoreString(e.item.getDefaultInstance(), WeightedItem.getCountAndChance(e, recipe.getWeightedOutputs()))).collect(Collectors.toList()), 30);
    }

    @Override
    public IVariable process(String key) {
        if ("input".equals(key)) {
            return PatchouliUtils.ingredientVariable(recipe.getInput());
        } else if (key.startsWith("output")) {
            int index = Integer.parseInt(key.substring(6)) - 1;
            if (index < 0 || index >= Math.min(recipe.getWeightedOutputs().size(), 30)) {
                return IVariable.from(ItemStack.EMPTY);
            }
            return pagedOutputs.get(index);
        }
        return null;
    }
}
