package com.sihenzhang.crockpot.integration.kubejs;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.WeightedItem;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;

public class PiglinBarteringRecipeJS extends AbstractCrockPotRecipeJS {
    public List<WeightedItem> weightedOutputs = new ArrayList<>();

    @Override
    public void create(ListJS args) {
        if (args.size() == 1) {
            inputItems.add(this.parseIngredientItem(args.get(0)));
        } else {
            if (args.get(0) instanceof ListJS) {
                ListJS.of(args.get(0)).forEach(this::weightedOutput);
            } else {
                this.weightedOutput(args.get(0));
            }
            inputItems.add(this.parseIngredientItem(args.get(1)));
        }
    }

    @Override
    public void deserialize() {
        JSONUtils.getAsJsonArray(json, "results").forEach(this::weightedOutput);
        inputItems.add(this.parseIngredientItem(json.get("ingredient")));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            JsonArray arr = new JsonArray();
            weightedOutputs.forEach(o -> arr.add(o.toJson()));
            json.add("results", arr);
        }
        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
        }
    }

    public PiglinBarteringRecipeJS weightedOutput(Object o) {
        WeightedItem weightedItem = this.parseWeightedItem(o);
        weightedOutputs.add(weightedItem);
        outputItems.add(this.parseResultItem(weightedItem.item.getDefaultInstance()));
        return this;
    }

    public PiglinBarteringRecipeJS weightedOutput(CharSequence item, int weight) {
        JsonObject json = new JsonObject();
        json.addProperty("item", item.toString());
        json.addProperty("weight", weight);
        return this.weightedOutput(json);
    }

    public PiglinBarteringRecipeJS weightedOutput(CharSequence item, int count, int weight) {
        JsonObject json = new JsonObject();
        json.addProperty("item", item.toString());
        json.addProperty("count", count);
        json.addProperty("weight", weight);
        return this.weightedOutput(json);
    }

    public PiglinBarteringRecipeJS weightedOutput(CharSequence item, int min, int max, int weight) {
        JsonObject json = new JsonObject();
        json.addProperty("item", item.toString());
        JsonObject count = new JsonObject();
        count.addProperty("min", min);
        count.addProperty("max", max);
        json.add("count", count);
        json.addProperty("weight", weight);
        return this.weightedOutput(json);
    }
}
