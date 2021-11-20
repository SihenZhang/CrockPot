package com.sihenzhang.crockpot.recipe.pot.requirement;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import net.minecraft.nbt.CompoundNBT;
import org.apache.commons.lang3.EnumUtils;

public class RequirementCategoryMaxExclusive implements IRequirement {
    FoodCategory category;
    float max;

    public RequirementCategoryMaxExclusive(FoodCategory category, float max) {
        this.category = category;
        this.max = max;
    }

    public RequirementCategoryMaxExclusive(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(CrockPotRecipeInput recipeInput) {
        return recipeInput.mergedFoodValues.get(category) < max;
    }

    public FoodCategory getCategory() {
		return category;
	}

	public float getMax() {
		return max;
	}

	@Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(RequirementConstants.TYPE, RequirementType.CATEGORY_MAX_EXCLUSIVE.name().toLowerCase());
        nbt.putString(RequirementConstants.CATEGORY, category.name());
        nbt.putFloat(RequirementConstants.MAX, max);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!RequirementType.CATEGORY_MAX_EXCLUSIVE.name().equals(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            throw new IllegalArgumentException(RequirementConstants.REQUIREMENT_TYPE_NOT_MATCH);
        }
        this.category = EnumUtils.getEnum(FoodCategory.class, nbt.getString(RequirementConstants.CATEGORY).toUpperCase());
        this.max = nbt.getFloat(RequirementConstants.MAX);
    }
}
