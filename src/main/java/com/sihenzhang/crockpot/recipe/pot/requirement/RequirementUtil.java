package com.sihenzhang.crockpot.recipe.pot.requirement;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

public final class RequirementUtil {
    public static IRequirement deserialize(INBT nbtIn) {
        CompoundNBT nbt = (CompoundNBT) nbtIn;
        switch (RequirementType.valueOf(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            case COMBINATION_AND:
                return new RequirementCombinationAnd(nbt);
            case COMBINATION_OR:
                return new RequirementCombinationOr(nbt);
            case CATEGORY_MAX:
                return new RequirementCategoryMax(nbt);
            case CATEGORY_MAX_EXCLUSIVE:
                return new RequirementCategoryMaxExclusive(nbt);
            case CATEGORY_MIN:
                return new RequirementCategoryMin(nbt);
            case CATEGORY_MIN_EXCLUSIVE:
                return new RequirementCategoryMinExclusive(nbt);
            case MUST_CONTAIN_INGREDIENT:
                return new RequirementMustContainIngredient(nbt);
            case MUST_CONTAIN_INGREDIENT_LESS_THAN:
                return new RequirementMustContainIngredientLessThan(nbt);
            default:
                throw new IllegalArgumentException("No valid type found");
        }
    }
}
