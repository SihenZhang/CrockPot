package com.sihenzhang.crockpot.recipe.requirements;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;

import java.util.Objects;

public final class RequirementUtil {
    public static Requirement deserialize(INBT nbtIn) {
        CompoundNBT nbt = (CompoundNBT) nbtIn;
        switch (Objects.requireNonNull(nbt.getString("type"))) {
            case "comb_and": return new RequirementCombinationAnd(nbt);
            case "comb_or": return new RequirementCombinationOr(nbt);
            case "ingredient_max": return new RequirementIngredientMax(nbt);
            case "ingredient_max_exclusive": return new RequirementIngredientMaxExclusive(nbt);
            case "ingredient_min": return new RequirementIngredientMin(nbt);
            case "ingredient_min_exclusive": return new RequirementIngredientMinExclusive(nbt);
            case "must_contain_item": return new RequirementMustContainItem(nbt);
            case "must_contain_item_less_than": return new RequirementMustContainItemLessThan(nbt);
            case "must_contain_ingredient": return new RequirementMustContainIngredient(nbt);
            case "must_contain_ingredient_less_than": return new RequirementMustContainIngredientLessThan(nbt);
            default: throw new IllegalArgumentException("no valid type found");
        }
    }
}
