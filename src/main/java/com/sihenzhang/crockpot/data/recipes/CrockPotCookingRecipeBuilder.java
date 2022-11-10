package com.sihenzhang.crockpot.data.recipes;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.cooking.requirement.*;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class CrockPotCookingRecipeBuilder extends AbstractRecipeBuilder {
    private final Item result;
    private final int resultCount;
    private final int priority;
    private final int weight;
    private final int cookingTime;
    private final int potLevel;
    private final List<IRequirement> requirements = new ArrayList<>();

    public CrockPotCookingRecipeBuilder(ItemLike result, int count, int priority, int weight, int cookingTime, int potLevel) {
        this.result = result.asItem();
        this.resultCount = count;
        this.priority = priority;
        this.weight = weight;
        this.cookingTime = cookingTime;
        this.potLevel = potLevel;
    }

    public static CrockPotCookingRecipeBuilder crockPotCooking(ItemLike result, int resultCount, int priority, int weight, int cookingTime, int potLevel) {
        return new CrockPotCookingRecipeBuilder(result, resultCount, priority, weight, cookingTime, potLevel);
    }

    public static CrockPotCookingRecipeBuilder crockPotCooking(ItemLike result, int priority, int weight, int cookingTime, int potLevel) {
        return crockPotCooking(result, 1, priority, weight, cookingTime, potLevel);
    }

    public static CrockPotCookingRecipeBuilder crockPotCooking(ItemLike result, int priority, int cookingTime, int potLevel) {
        return crockPotCooking(result, 1, priority, 1, cookingTime, potLevel);
    }

    public CrockPotCookingRecipeBuilder requirement(IRequirement requirement) {
        requirements.add(requirement);
        return this;
    }

    public CrockPotCookingRecipeBuilder requirementCategoryMax(FoodCategory category, float max) {
        return this.requirement(new RequirementCategoryMax(category, max));
    }

    public CrockPotCookingRecipeBuilder requirementCategoryMaxExclusive(FoodCategory category, float max) {
        return this.requirement(new RequirementCategoryMaxExclusive(category, max));
    }

    public CrockPotCookingRecipeBuilder requirementCategoryMin(FoodCategory category, float min) {
        return this.requirement(new RequirementCategoryMin(category, min));
    }

    public CrockPotCookingRecipeBuilder requirementCategoryMinExclusive(FoodCategory category, float min) {
        return this.requirement(new RequirementCategoryMinExclusive(category, min));
    }

    public CrockPotCookingRecipeBuilder requirementWithoutCategory(FoodCategory category) {
        return this.requirement(new RequirementCategoryMax(category, 0.0F));
    }

    public CrockPotCookingRecipeBuilder requirementWithAnyCategory(FoodCategory category) {
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
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, requirements, result, resultCount, priority, weight, cookingTime, potLevel));
    }

    public static class Result extends AbstractFinishedRecipe {
        private final List<IRequirement> requirements;
        private final Item result;
        private final int resultCount;
        private final int priority;
        private final int weight;
        private final int cookingTime;
        private final int potLevel;

        public Result(ResourceLocation id, List<IRequirement> requirements, Item result, int resultCount, int priority, int weight, int cookingTime, int potLevel) {
            super(id);
            this.requirements = ImmutableList.copyOf(requirements);
            this.result = result;
            this.resultCount = resultCount;
            this.priority = priority;
            this.weight = weight;
            this.cookingTime = cookingTime;
            this.potLevel = potLevel;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            var requirementsArray = new JsonArray();
            requirements.forEach(requirement -> requirementsArray.add(requirement.toJson()));
            pJson.add("requirements", requirementsArray);
            var resultKey = ForgeRegistries.ITEMS.getKey(result).toString();
            if (resultCount > 1) {
                var resultObject = new JsonObject();
                resultObject.addProperty("item", resultKey);
                resultObject.addProperty("count", resultCount);
                pJson.add("result", resultObject);
            } else {
                pJson.addProperty("result", resultKey);
            }
            pJson.addProperty("priority", priority);
            if (weight > 1) {
                pJson.addProperty("weight", weight);
            }
            pJson.addProperty("cookingtime", cookingTime);
            pJson.addProperty("potlevel", potLevel);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CrockPotRecipes.CROCK_POT_COOKING_RECIPE_SERIALIZER.get();
        }
    }
}
