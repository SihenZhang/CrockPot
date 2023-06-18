package com.sihenzhang.crockpot.integration.kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.RangedItem;
//import dev.latvian.mods.kubejs.recipe.IngredientMatch;
//import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
//import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
//import dev.latvian.mods.kubejs.recipe.RecipeArguments;
//import dev.latvian.mods.kubejs.util.ListJS;
//import dev.latvian.mods.kubejs.util.MapJS;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class PiglinBarteringRecipeJS extends AbstractCrockPotRecipeJS {
//    public final List<Ingredient> inputItems = new ArrayList<>();
//    public final List<ItemStack> outputItems = new ArrayList<>();
//    public List<WeightedEntry.Wrapper<RangedItem>> weightedOutputs = new ArrayList<>();
//
//    @Override
//    public void create(RecipeArguments args) {
//        if (args.size() == 1) {
//            inputItems.add(this.parseItemInput(args.get(0)));
//        } else {
//            if (args.get(0) instanceof ListJS) {
//                ListJS.of(args.get(0)).forEach(this::weightedOutput);
//            } else {
//                this.weightedOutput(args.get(0));
//            }
//            inputItems.add(this.parseItemInput(args.get(1)));
//        }
//    }
//
//    @Override
//    public void deserialize() {
//        GsonHelper.getAsJsonArray(json, "results").forEach(this::weightedOutput);
//        inputItems.add(this.parseItemInput(json.get("ingredient")));
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
//    public void serialize() {
//        if (serializeOutputs) {
//            JsonArray arr = new JsonArray();
//            weightedOutputs.forEach(o -> {
//                JsonObject object = o.getData().toJson().getAsJsonObject();
//                object.addProperty("weight", o.getWeight().asInt());
//                arr.add(object);
//            });
//            json.add("results", arr);
//        }
//        if (serializeInputs) {
//            json.add("ingredient", inputItems.get(0).toJson());
//        }
//    }
//
//    public PiglinBarteringRecipeJS weightedOutput(Object o) {
//        RangedItem rangedItem = this.parseRangedItem(o);
//        if (o instanceof JsonElement json) {
//            int weight = GsonHelper.getAsInt(GsonHelper.convertToJsonObject(json, "weighted ranged item"), "weight", 1);
//            weightedOutputs.add(WeightedEntry.wrap(rangedItem, weight));
//        } else if (o instanceof CharSequence) {
//            weightedOutputs.add(WeightedEntry.wrap(rangedItem, 1));
//        } else {
//            int weight = GsonHelper.getAsInt(GsonHelper.convertToJsonObject(MapJS.json(o), "weighted ranged item"), "weight", 1);
//            weightedOutputs.add(WeightedEntry.wrap(rangedItem, weight));
//        }
//        outputItems.add(this.parseItemOutput(rangedItem.item.getDefaultInstance()));
//        return this;
//    }
//
//    public PiglinBarteringRecipeJS weightedOutput(CharSequence item, int weight) {
//        JsonObject json = new JsonObject();
//        json.addProperty("item", item.toString());
//        json.addProperty("weight", weight);
//        return this.weightedOutput(json);
//    }
//
//    public PiglinBarteringRecipeJS weightedOutput(CharSequence item, int count, int weight) {
//        JsonObject json = new JsonObject();
//        json.addProperty("item", item.toString());
//        json.addProperty("count", count);
//        json.addProperty("weight", weight);
//        return this.weightedOutput(json);
//    }
//
//    public PiglinBarteringRecipeJS weightedOutput(CharSequence item, int min, int max, int weight) {
//        JsonObject json = new JsonObject();
//        json.addProperty("item", item.toString());
//        JsonObject count = new JsonObject();
//        count.addProperty("min", min);
//        count.addProperty("max", max);
//        json.add("count", count);
//        json.addProperty("weight", weight);
//        return this.weightedOutput(json);
//    }
}
