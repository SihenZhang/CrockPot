package com.sihenzhang.crockpot.integration.kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;

public class CrockPotCookingRecipeJS extends AbstractCrockPotRecipeJS {
    public List<IRequirement> requirements = new ArrayList<>();

    @Override
    public void create(ListJS args) {
        this.outputItems.add(this.parseResultItem(args.get(0)));
        if (args.size() >= 5) {
            this.priority(((Number) args.get(1)).intValue());
            this.weight(((Number) args.get(2)).intValue());
            this.cookingTime(((Number) args.get(3)).intValue());
            this.potLevel(((Number) args.get(4)).intValue());
        } else {
            this.priority(((Number) args.get(1)).intValue());
            this.cookingTime(((Number) args.get(2)).intValue());
            this.potLevel(((Number) args.get(3)).intValue());
        }
    }

    @Override
    public void deserialize() {
        outputItems.add(this.parseResultItem(json.get("result")));
        JSONUtils.getAsJsonArray(json, "requirements").forEach(this::requirement);
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }
        if (serializeInputs) {
            JsonArray arr = new JsonArray();
            requirements.forEach(requirement -> arr.add(requirement.toJson()));
            json.add("requirements", arr);
        }
    }

    public CrockPotCookingRecipeJS requirement(Object requirement) {
        requirements.add(this.parseRequirement(requirement));
        return this;
    }

    public CrockPotCookingRecipeJS requirementCategoryMax(String category, float max) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "category_max");
        json.addProperty("category", category);
        json.addProperty("max", max);
        return this.requirement(json);
    }

    public CrockPotCookingRecipeJS requirementCategoryMaxExclusive(String category, float max) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "category_max_exclusive");
        json.addProperty("category", category);
        json.addProperty("max", max);
        return this.requirement(json);
    }

    public CrockPotCookingRecipeJS requirementCategoryMin(String category, float min) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "category_min");
        json.addProperty("category", category);
        json.addProperty("min", min);
        return this.requirement(json);
    }

    public CrockPotCookingRecipeJS requirementCategoryMinExclusive(String category, float min) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "category_min_exclusive");
        json.addProperty("category", category);
        json.addProperty("min", min);
        return this.requirement(json);
    }

    public CrockPotCookingRecipeJS requirementCombinationAnd(Object first, Object second) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "combination_and");
        IRequirement firstRequirement = this.parseRequirement(first);
        json.add("first", firstRequirement.toJson());
        IRequirement secondRequirement = this.parseRequirement(second);
        json.add("second", secondRequirement.toJson());
        return this.requirement(json);
    }

    public CrockPotCookingRecipeJS requirementCombinationOr(Object first, Object second) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "combination_or");
        IRequirement firstRequirement = this.parseRequirement(first);
        json.add("first", firstRequirement.toJson());
        IRequirement secondRequirement = this.parseRequirement(second);
        json.add("second", secondRequirement.toJson());
        return this.requirement(json);
    }

    public CrockPotCookingRecipeJS requirementMustContainIngredient(Object ingredient, int quantity) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "must_contain_ingredient");
        json.add("ingredient", this.parseIngredientItem(ingredient).toJson());
        json.addProperty("quantity", quantity);
        return this.requirement(json);
    }

    public CrockPotCookingRecipeJS requirementMustContainIngredientLessThan(Object ingredient, int quantity) {
        JsonObject json = new JsonObject();
        json.addProperty("type", "must_contain_ingredient_less_than");
        json.add("ingredient", this.parseIngredientItem(ingredient).toJson());
        json.addProperty("quantity", quantity);
        return this.requirement(json);
    }

    public CrockPotCookingRecipeJS priority(int priority) {
        json.addProperty("priority", priority);
        return this;
    }

    public CrockPotCookingRecipeJS weight(int weight) {
        json.addProperty("weight", Math.max(weight, 1));
        return this;
    }

    public CrockPotCookingRecipeJS cookingTime(int cookingTime) {
        json.addProperty("cookingtime", cookingTime);
        return this;
    }

    public CrockPotCookingRecipeJS potLevel(int potLevel) {
        json.addProperty("potlevel", potLevel);
        return this;
    }
}
