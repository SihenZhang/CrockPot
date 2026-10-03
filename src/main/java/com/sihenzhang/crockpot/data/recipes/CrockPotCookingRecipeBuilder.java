package com.sihenzhang.crockpot.data.recipes;

import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import com.sihenzhang.crockpot.registry.FoodCategory;
import net.minecraft.core.Holder;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.ArrayList;
import java.util.List;

public class CrockPotCookingRecipeBuilder extends AbstractRecipeBuilder {
    private final Item result;
    private final int resultCount;
    private final int priority;
    private final int cookingTime;
    private final int potLevel;
    private int weight = 1;
    private final List<IRequirement> requirements = new ArrayList<>();

    public CrockPotCookingRecipeBuilder(ItemLike result, int count, int priority, int cookingTime, int potLevel) {
        this.result = result.asItem();
        this.resultCount = count;
        this.priority = priority;
        this.cookingTime = cookingTime;
        this.potLevel = potLevel;
    }

    public static CrockPotCookingRecipeBuilder crockPotCooking(ItemLike result, int resultCount, int priority, int cookingTime, int potLevel) {
        return new CrockPotCookingRecipeBuilder(result, resultCount, priority, cookingTime, potLevel);
    }

    public static CrockPotCookingRecipeBuilder crockPotCooking(ItemLike result, int priority, int cookingTime, int potLevel) {
        return crockPotCooking(result, 1, priority, cookingTime, potLevel);
    }

    public CrockPotCookingRecipeBuilder weight(int weight) {
        this.weight = weight;
        return this;
    }

    public CrockPotCookingRecipeBuilder requirement(IRequirement requirement) {
        requirements.add(requirement);
        return this;
    }

    public CrockPotCookingRecipeBuilder requirementCategoryMax(Holder<FoodCategory> category, float max) {
        return this.requirement(new RequirementCategoryMax(category, max));
    }

    public CrockPotCookingRecipeBuilder requirementCategoryMaxExclusive(Holder<FoodCategory> category, float max) {
        return this.requirement(new RequirementCategoryMaxExclusive(category, max));
    }

    public CrockPotCookingRecipeBuilder requirementCategoryMin(Holder<FoodCategory> category, float min) {
        return this.requirement(new RequirementCategoryMin(category, min));
    }

    public CrockPotCookingRecipeBuilder requirementCategoryMinExclusive(Holder<FoodCategory> category, float min) {
        return this.requirement(new RequirementCategoryMinExclusive(category, min));
    }

    public CrockPotCookingRecipeBuilder requirementWithoutCategory(Holder<FoodCategory> category) {
        return this.requirement(new RequirementCategoryMax(category, 0.0F));
    }

    public CrockPotCookingRecipeBuilder requirementWithAnyCategory(Holder<FoodCategory> category) {
        return this.requirement(new RequirementCategoryMinExclusive(category, 0.0F));
    }

    public CrockPotCookingRecipeBuilder requirementCombinationAnd(IRequirement first, IRequirement second) {
        return this.requirement(new RequirementCombinationAnd(first, second));
    }

    public CrockPotCookingRecipeBuilder requirementCombinationOr(IRequirement first, IRequirement second) {
        return this.requirement(new RequirementCombinationOr(first, second));
    }

    public CrockPotCookingRecipeBuilder requirementMustContainIngredient(Ingredient ingredient, int quantity) {
        return this.requirement(new RequirementMustContainIngredient(ingredient, quantity));
    }

    public CrockPotCookingRecipeBuilder requirementMustContainIngredient(Ingredient ingredient) {
        return this.requirementMustContainIngredient(ingredient, 1);
    }

    public CrockPotCookingRecipeBuilder requirementMustContainIngredientLessThan(Ingredient ingredient, int quantity) {
        return this.requirement(new RequirementMustContainIngredientLessThan(ingredient, quantity));
    }

    public CrockPotCookingRecipeBuilder requirementMustContainIngredientLessThan(Ingredient ingredient) {
        return this.requirementMustContainIngredientLessThan(ingredient, 1);
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(pRecipeId, new com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe(requirements, new net.minecraft.world.item.ItemStack(result, resultCount), priority, weight, cookingTime, potLevel), null);
    }
}
