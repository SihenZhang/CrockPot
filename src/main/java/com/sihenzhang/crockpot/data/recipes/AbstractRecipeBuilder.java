package com.sihenzhang.crockpot.data.recipes;

import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.RecipeBuilder;

import javax.annotation.Nullable;

public abstract class AbstractRecipeBuilder implements RecipeBuilder {
    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }
}
