package com.sihenzhang.crockpot.recipe.requirements;

import com.sihenzhang.crockpot.recipe.RecipeInput;
import net.minecraft.nbt.CompoundNBT;

import java.util.Objects;

public class RequirementCombinationOr extends Requirement {
    Requirement first, second;

    public RequirementCombinationOr(Requirement first, Requirement second) {
        this.first = first;
        this.second = second;
    }

    public RequirementCombinationOr(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(RecipeInput recipeInput) {
        return first.test(recipeInput) || second.test(recipeInput);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(RequirementConstants.TYPE, RequirementType.COMBINATION_OR.name().toLowerCase());
        nbt.put(RequirementConstants.FIRST, first.serializeNBT());
        nbt.put(RequirementConstants.SECOND, second.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!RequirementType.COMBINATION_OR.name().equals(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            throw new IllegalArgumentException(RequirementConstants.REQUIREMENT_TYPE_NOT_MATCH);
        }
        this.first = RequirementUtil.deserialize(Objects.requireNonNull(nbt.get(RequirementConstants.FIRST)));
        this.second = RequirementUtil.deserialize(Objects.requireNonNull(nbt.get(RequirementConstants.SECOND)));
    }
}
