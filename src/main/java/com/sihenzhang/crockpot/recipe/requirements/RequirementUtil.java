package com.sihenzhang.crockpot.recipe.requirements;

import net.minecraft.nbt.CompoundNBT;

public final class RequirementUtil {
    public static Requirement deserialize(CompoundNBT nbt) {
        switch (nbt.getString("type")) {
            case "comb_and": return new RequirementCombinationAnd(nbt);
            case "comb_or": return new RequirementCombinationOr(nbt);
            case "ingredient_max": return new RequirementIngredientMax(nbt);
            case "ingredient_min": return new RequirementIngredientMin(nbt);
            case "must_contain_item": return new RequirementMustContainItem(nbt);
        }
        throw new IllegalArgumentException("no valid type found");
    }
}
