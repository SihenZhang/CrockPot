package com.sihenzhang.crockpot.data.recipes;

import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.recipe.DryingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMax;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMaxExclusive;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMin;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMinExclusive;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DryingRecipeBuilder extends AbstractRecipeBuilder {
    private final Optional<Ingredient> ingredient;
    private final List<IRequirement> requirements = new ArrayList<>();
    private final Item result;
    private final int dryingTime;

    private DryingRecipeBuilder(Optional<Ingredient> ingredient, ItemLike result, int dryingTime) {
        this.ingredient = ingredient;
        this.result = result.asItem();
        this.dryingTime = dryingTime;
    }

    public static DryingRecipeBuilder drying(Ingredient ingredient, ItemLike result, int dryingTime) {
        return new DryingRecipeBuilder(Optional.of(ingredient), result, dryingTime);
    }

    public static DryingRecipeBuilder dryingByFoodValues(ItemLike result, int dryingTime) {
        return new DryingRecipeBuilder(Optional.empty(), result, dryingTime);
    }

    public DryingRecipeBuilder requirement(IRequirement requirement) {
        requirements.add(requirement);
        return this;
    }

    public DryingRecipeBuilder requirementCategoryMax(Holder<FoodCategory> category, float max) {
        return this.requirement(new RequirementCategoryMax(category, max));
    }

    public DryingRecipeBuilder requirementCategoryMaxExclusive(Holder<FoodCategory> category, float max) {
        return this.requirement(new RequirementCategoryMaxExclusive(category, max));
    }

    public DryingRecipeBuilder requirementCategoryMin(Holder<FoodCategory> category, float min) {
        return this.requirement(new RequirementCategoryMin(category, min));
    }

    public DryingRecipeBuilder requirementCategoryMinExclusive(Holder<FoodCategory> category, float min) {
        return this.requirement(new RequirementCategoryMinExclusive(category, min));
    }

    public DryingRecipeBuilder requirementWithoutCategory(Holder<FoodCategory> category) {
        return this.requirementCategoryMax(category, 0.0F);
    }

    public DryingRecipeBuilder requirementWithAnyCategory(Holder<FoodCategory> category) {
        return this.requirementCategoryMinExclusive(category, 0.0F);
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        output.accept(id, new DryingRecipe(ingredient, requirements, new ItemStackTemplate(result, 1), dryingTime), null);
    }
}
