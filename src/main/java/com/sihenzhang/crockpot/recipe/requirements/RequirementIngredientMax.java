package com.sihenzhang.crockpot.recipe.requirements;

import com.sihenzhang.crockpot.base.CrockPotIngredientType;
import com.sihenzhang.crockpot.recipe.RecipeInput;
import net.minecraft.nbt.CompoundNBT;

public class RequirementIngredientMax extends Requirement {
    CrockPotIngredientType type;
    float max;

    public RequirementIngredientMax(CrockPotIngredientType type, float min) {
        this.type = type;
        this.max = min;
    }

    public RequirementIngredientMax(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(RecipeInput recipeInput) {
        return recipeInput.ingredients.getIngredient(type) <= max;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", "ingredient_max");
        nbt.putString("ingredient", type.name());
        nbt.putFloat("max", max);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!nbt.getString().equals("ingredient_max"))
            throw new IllegalArgumentException("requirement type doesn't match");
        this.max = nbt.getFloat("max");
        this.type = CrockPotIngredientType.valueOf(nbt.getString("ingredient"));
    }
}
