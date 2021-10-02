package com.sihenzhang.crockpot.integration.patchouli;

import net.minecraft.item.crafting.Ingredient;
import vazkii.patchouli.api.IVariable;

import java.util.Arrays;
import java.util.stream.Collectors;

public final class PatchouliUtils {
    public static IVariable ingredientVariable(Ingredient ingredient) {
        return IVariable.wrapList(Arrays.stream(ingredient.getItems()).map(IVariable::from).collect(Collectors.toList()));
    }
}
