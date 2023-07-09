package com.sihenzhang.crockpot.integration.kubejs;

public class CrockPotCookingRecipeJS extends AbstractCrockPotRecipeJS {
//    public final List<Ingredient> inputItems = new ArrayList<>();
//    public final List<ItemStack> outputItems = new ArrayList<>();
//    public List<IRequirement> requirements = new ArrayList<>();
//
//    @Override
//    public void create(RecipeArguments args) {
//        this.outputItems.add(this.parseItemOutput(args.get(0)));
//        if (args.size() >= 5) {
//            this.priority(((Number) args.get(1)).intValue());
//            this.weight(((Number) args.get(2)).intValue());
//            this.cookingTime(((Number) args.get(3)).intValue());
//            this.potLevel(((Number) args.get(4)).intValue());
//        } else {
//            this.priority(((Number) args.get(1)).intValue());
//            this.cookingTime(((Number) args.get(2)).intValue());
//            this.potLevel(((Number) args.get(3)).intValue());
//        }
//    }
//
//    @Override
//    public boolean hasInput(IngredientMatch ingredientMatch) {
//        throw new RuntimeException("PLEASE IMPLEMENT THIS");
//    }
//
//    @Override
//    public boolean hasOutput(IngredientMatch ingredientMatch) {
//        throw new RuntimeException("PLEASE IMPLEMENT THIS");
//    }
//
//    @Override
//    public boolean replaceInput(IngredientMatch ingredientMatch, Ingredient ingredient, ItemInputTransformer itemInputTransformer) {
//        throw new RuntimeException("PLEASE IMPLEMENT THIS");
//    }
//
//    @Override
//    public boolean replaceOutput(IngredientMatch ingredientMatch, ItemStack itemStack, ItemOutputTransformer itemOutputTransformer) {
//        throw new RuntimeException("PLEASE IMPLEMENT THIS");
//    }
//
//    @Override
//    public void deserialize() {
//        outputItems.add(this.parseItemOutput(json.get("result")));
//        GsonHelper.getAsJsonArray(json, "requirements").forEach(this::requirement);
//    }
//
//    @Override
//    public void serialize() {
//        if (serializeOutputs) {
//            json.add("result", itemToJson(outputItems.get(0)));
//        }
//        if (serializeInputs) {
//            JsonArray arr = new JsonArray();
//            requirements.forEach(requirement -> arr.add(requirement.toJson()));
//            json.add("requirements", arr);
//        }
//    }
//
//    public CrockPotCookingRecipeJS requirement(Object requirement) {
//        requirements.add(this.parseRequirement(requirement));
//        return this;
//    }
//
//    public CrockPotCookingRecipeJS requirementCategoryMax(String category, float max) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", "category_max");
//        json.addProperty("category", category);
//        json.addProperty("max", max);
//        return this.requirement(json);
//    }
//
//    public CrockPotCookingRecipeJS requirementCategoryMaxExclusive(String category, float max) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", "category_max_exclusive");
//        json.addProperty("category", category);
//        json.addProperty("max", max);
//        return this.requirement(json);
//    }
//
//    public CrockPotCookingRecipeJS requirementCategoryMin(String category, float min) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", "category_min");
//        json.addProperty("category", category);
//        json.addProperty("min", min);
//        return this.requirement(json);
//    }
//
//    public CrockPotCookingRecipeJS requirementCategoryMinExclusive(String category, float min) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", "category_min_exclusive");
//        json.addProperty("category", category);
//        json.addProperty("min", min);
//        return this.requirement(json);
//    }
//
//    public CrockPotCookingRecipeJS requirementCombinationAnd(Object first, Object second) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", "combination_and");
//        IRequirement firstRequirement = this.parseRequirement(first);
//        json.add("first", firstRequirement.toJson());
//        IRequirement secondRequirement = this.parseRequirement(second);
//        json.add("second", secondRequirement.toJson());
//        return this.requirement(json);
//    }
//
//    public CrockPotCookingRecipeJS requirementCombinationOr(Object first, Object second) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", "combination_or");
//        IRequirement firstRequirement = this.parseRequirement(first);
//        json.add("first", firstRequirement.toJson());
//        IRequirement secondRequirement = this.parseRequirement(second);
//        json.add("second", secondRequirement.toJson());
//        return this.requirement(json);
//    }
//
//    public CrockPotCookingRecipeJS requirementMustContainIngredient(Object ingredient, int quantity) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", "must_contain_ingredient");
//        json.add("ingredient", this.parseItemInput(ingredient).toJson());
//        json.addProperty("quantity", quantity);
//        return this.requirement(json);
//    }
//
//    public CrockPotCookingRecipeJS requirementMustContainIngredientLessThan(Object ingredient, int quantity) {
//        JsonObject json = new JsonObject();
//        json.addProperty("type", "must_contain_ingredient_less_than");
//        json.add("ingredient", this.parseItemInput(ingredient).toJson());
//        json.addProperty("quantity", quantity);
//        return this.requirement(json);
//    }
//
//    public CrockPotCookingRecipeJS priority(int priority) {
//        json.addProperty("priority", priority);
//        return this;
//    }
//
//    public CrockPotCookingRecipeJS weight(int weight) {
//        json.addProperty("weight", Math.max(weight, 1));
//        return this;
//    }
//
//    public CrockPotCookingRecipeJS cookingTime(int cookingTime) {
//        json.addProperty("cookingtime", cookingTime);
//        return this;
//    }
//
//    public CrockPotCookingRecipeJS potLevel(int potLevel) {
//        json.addProperty("potlevel", potLevel);
//        return this;
//    }
}
