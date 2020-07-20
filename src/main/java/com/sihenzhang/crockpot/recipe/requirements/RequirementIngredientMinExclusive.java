package com.sihenzhang.crockpot.recipe.requirements;

import com.sihenzhang.crockpot.base.CrockPotIngredientType;
import com.sihenzhang.crockpot.recipe.RecipeInput;
import net.minecraft.nbt.CompoundNBT;

public class RequirementIngredientMinExclusive extends Requirement {
    CrockPotIngredientType type;
    float min;

    public RequirementIngredientMinExclusive(CrockPotIngredientType type, float min) {
        this.type = type;
        this.min = min;
    }

    public RequirementIngredientMinExclusive(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(RecipeInput recipeInput) {
        return recipeInput.ingredients.getIngredient(type) > min;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", "ingredient_min_exclusive");
        nbt.putString("ingredient", type.name());
        nbt.putFloat("min", min);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!nbt.getString("type").equals("ingredient_min_exclusive"))
            throw new IllegalArgumentException("requirement type doesn't match");
        this.min = nbt.getFloat("min");
        this.type = CrockPotIngredientType.valueOf(nbt.getString("ingredient"));
    }
}
