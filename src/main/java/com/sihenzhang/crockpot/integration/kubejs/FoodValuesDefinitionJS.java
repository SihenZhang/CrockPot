package com.sihenzhang.crockpot.integration.kubejs;

public class FoodValuesDefinitionJS extends AbstractCrockPotRecipeJS {
//    public final List<Ingredient> inputItems = new ArrayList<>();
//    public final List<ItemStack> outputItems = new ArrayList<>();
//    public Set<ResourceLocation> names = new HashSet<>();
//    public FoodValues foodValues;
//    public boolean isTag;
//
//    @Override
//    public void create(RecipeArguments args) {
//        this.foodValues = this.parseFoodValues(args.get(0));
//        this.foodValues.entrySet().forEach(entry -> this.outputItems.add(this.parseItemOutput(FoodCategory.getItemStack(entry.getKey()))));
//        this.isTag = (Boolean) args.get(1);
//        for (int i = 2; i < args.size(); i++) {
//            this.define(args.get(i).toString());
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
//        foodValues = this.parseFoodValues(GsonHelper.getAsJsonObject(json, "values"));
//        foodValues.entrySet().forEach(entry -> outputItems.add(this.parseItemOutput(FoodCategory.getItemStack(entry.getKey()))));
//        isTag = json.has("tags");
//        GsonHelper.getAsJsonArray(json, isTag ? "tags" : "items").forEach(o -> this.define(o.getAsString()));
//    }
//
//    @Override
//    public void serialize() {
//        if (serializeOutputs) {
//            json.add("values", foodValues.toJson());
//        }
//        if (serializeInputs) {
//            JsonArray arr = new JsonArray();
//            names.forEach(name -> arr.add(name.toString()));
//            json.add(isTag ? "tags" : "items", arr);
//        }
//    }
//
//    public FoodValuesDefinitionJS define(String name) {
//        String nameWithoutHashSymbol = name.replace("#", "");
//        ResourceLocation rl = new ResourceLocation(nameWithoutHashSymbol);
//        names.add(rl);
//        if (isTag) {
//            TagKey<Item> tag = ItemTags.create(rl);
//            if (ForgeRegistries.ITEMS.tags().isKnownTagName(tag)) {
//                inputItems.add(this.parseItemInput(Ingredient.of(tag)));
//            }
//        } else {
//            Item item = ForgeRegistries.ITEMS.getValue(rl);
//            if (item != null && item != Items.AIR) {
//                inputItems.add(this.parseItemInput(Ingredient.of(item)));
//            }
//        }
//        return this;
//    }
}
