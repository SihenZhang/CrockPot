package com.sihenzhang.crockpot.integration.patchouli;

import com.google.common.base.Preconditions;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.WeightedItem;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class ProcessorPiglinBartering implements IComponentProcessor {
    private PiglinBarteringRecipe recipe;

    @Override
    public void setup(IVariableProvider variables) {
        String input = variables.get("input").asString();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(input));
        Preconditions.checkArgument(item != null, "input cannot be null");
        recipe = CrockPot.PIGLIN_BARTERING_RECIPE_MANAGER.match(item);
    }

    @Override
    public IVariable process(String key) {
        if ("input".equals(key)) {
            return PatchouliUtils.ingredientVariable(recipe.getInput());
        } else if (key.startsWith("output")) {
            int index = Integer.parseInt(key.substring(6)) - 1;
            if (index < 0 || index >= recipe.getWeightedOutputs().size()) {
                return IVariable.from(ItemStack.EMPTY);
            }
            WeightedItem weightedItem = recipe.getWeightedOutputs().get(index);
            ItemStack stack = weightedItem.item.getDefaultInstance();
            float chance = (float) weightedItem.weight / WeightedRandom.getTotalWeight(recipe.getWeightedOutputs());
            CompoundNBT displayTag = new CompoundNBT();
            CompoundNBT loreTag = new CompoundNBT();
            ListNBT loreListTag = new ListNBT();
            StringBuilder chanceTooltip = new StringBuilder("{\"text\":\"");
            if (weightedItem.isRanged()) {
                chanceTooltip.append(weightedItem.min).append("-").append(weightedItem.max);
            } else {
                chanceTooltip.append(weightedItem.min);
            }
            chanceTooltip.append(" (").append(MathUtils.format(chance, "0.00%")).append(")");
            chanceTooltip.append("\"}");
            loreListTag.add(0, StringNBT.valueOf(chanceTooltip.toString()));
            loreTag.put("Lore", loreListTag);
            displayTag.put("display", loreTag);
            stack.setTag(displayTag);
            return IVariable.from(stack);
        }
        return null;
    }
}
