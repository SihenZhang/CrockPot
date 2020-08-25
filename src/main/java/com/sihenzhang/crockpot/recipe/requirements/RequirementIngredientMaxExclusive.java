package com.sihenzhang.crockpot.recipe.requirements;

import com.sihenzhang.crockpot.base.CrockPotIngredientType;
import com.sihenzhang.crockpot.recipe.RecipeInput;
import net.minecraft.nbt.CompoundNBT;

public class RequirementIngredientMaxExclusive extends Requirement {
    CrockPotIngredientType type;
    float max;

    public RequirementIngredientMaxExclusive(CrockPotIngredientType type, float max) {
        this.type = type;
        this.max = max;
    }

    public RequirementIngredientMaxExclusive(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(RecipeInput recipeInput) {
        return recipeInput.ingredients.getIngredient(type) < max;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", "ingredient_max_exclusive");
        nbt.putString("ingredient", type.name());
        nbt.putFloat("max", max);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!"ingredient_max_exclusive".equals(nbt.getString("type"))) {
            throw new IllegalArgumentException("requirement type doesn't match");
        }
        this.max = nbt.getFloat("max");
        this.type = CrockPotIngredientType.valueOf(nbt.getString("ingredient"));
    }
}
