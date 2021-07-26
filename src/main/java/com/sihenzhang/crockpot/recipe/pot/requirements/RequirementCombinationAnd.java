package com.sihenzhang.crockpot.recipe.pot.requirements;

import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import net.minecraft.nbt.CompoundNBT;

import java.util.Objects;

public class RequirementCombinationAnd extends Requirement {
    Requirement first, second;

    public RequirementCombinationAnd(Requirement first, Requirement second) {
        this.first = first;
        this.second = second;
    }

    public RequirementCombinationAnd(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(CrockPotRecipeInput recipeInput) {
        return first.test(recipeInput) && second.test(recipeInput);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(RequirementConstants.TYPE, RequirementType.COMBINATION_AND.name().toLowerCase());
        nbt.put(RequirementConstants.FIRST, first.serializeNBT());
        nbt.put(RequirementConstants.SECOND, second.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!RequirementType.COMBINATION_AND.name().equals(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            throw new IllegalArgumentException(RequirementConstants.REQUIREMENT_TYPE_NOT_MATCH);
        }
        this.first = RequirementUtil.deserialize(Objects.requireNonNull(nbt.get(RequirementConstants.FIRST)));
        this.second = RequirementUtil.deserialize(Objects.requireNonNull(nbt.get(RequirementConstants.SECOND)));
    }
}
