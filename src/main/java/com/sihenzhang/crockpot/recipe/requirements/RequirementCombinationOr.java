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
        nbt.putString("type", "comb_or");
        nbt.put("first", first.serializeNBT());
        nbt.put("second", second.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!nbt.getString("type").equals("comb_or"))
            throw new IllegalArgumentException("requirement type doesn't match");
        this.first = RequirementUtil.deserialize((CompoundNBT) Objects.requireNonNull(nbt.get("first")));
        this.second = RequirementUtil.deserialize((CompoundNBT) Objects.requireNonNull(nbt.get("second")));
    }
}
