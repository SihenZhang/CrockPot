package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sihenzhang.crockpot.util.JsonUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.WeightedRandom;

import java.util.List;
import java.util.Objects;

public class WeightedItem extends WeightedRandom.Item {
    public final Item item;
    public final int min;
    public final int max;

    public WeightedItem(Item item, int min, int max, int itemWeight) {
        super(itemWeight);
        this.item = item;
        this.min = Math.min(min, max);
        this.max = Math.max(min, max);
    }

    public WeightedItem(Item item, int count, int itemWeight) {
        this(item, count, count, itemWeight);
    }

    public WeightedItem(Item item, int itemWeight) {
        this(item, 1, itemWeight);
    }

    public boolean isRanged() {
        return this.min != this.max;
    }

    public boolean isEmpty() {
        return this.item == null || this.item == Items.AIR || (this.min <= 0 && this.max <= 0) || this.weight <= 0;
    }

    public static WeightedItem fromJson(JsonElement json) {
        if (json == null || json.isJsonNull()) {
            throw new JsonSyntaxException("Json cannot be null");
        }
        if (!json.isJsonObject()) {
            throw new JsonSyntaxException("Expected weighted item to be an object, was " + JSONUtils.getType(json));
        }
        JsonObject obj = json.getAsJsonObject();
        Item item = JsonUtils.getAsItem(obj, "item");
        if (item != null) {
            int weight = JSONUtils.getAsInt(obj, "weight", 1);
            if (obj.has("count")) {
                JsonElement e = obj.get("count");
                if (e.isJsonObject()) {
                    JsonObject count = e.getAsJsonObject();
                    if (count.has("min") && count.has("max")) {
                        int min = JSONUtils.getAsInt(count, "min");
                        int max = JSONUtils.getAsInt(count, "max");
                        return new WeightedItem(item, min, max, weight);
                    } else if (count.has("min")) {
                        int min = JSONUtils.getAsInt(count, "min");
                        return new WeightedItem(item, min, weight);
                    } else if (count.has("max")) {
                        int max = JSONUtils.getAsInt(count, "max");
                        return new WeightedItem(item, max, weight);
                    } else {
                        return new WeightedItem(item, weight);
                    }
                } else {
                    int count = JSONUtils.getAsInt(obj, "count", 1);
                    return new WeightedItem(item, count, weight);
                }
            } else {
                return new WeightedItem(item, weight);
            }
        } else {
            return null;
        }
    }

    public static JsonElement toJson(WeightedItem weightedItem) {
        final JsonObject obj = new JsonObject();
        obj.addProperty("item", Objects.requireNonNull(weightedItem.item.getRegistryName()).toString());
        if (weightedItem.isRanged()) {
            JsonObject count = new JsonObject();
            count.addProperty("min", weightedItem.min);
            count.addProperty("max", weightedItem.max);
            obj.add("count", count);
        } else {
            obj.addProperty("count", weightedItem.min);
        }
        obj.addProperty("weight", weightedItem.weight);
        return obj;
    }

    public static String getCountAndChance(WeightedItem weightedItem, List<WeightedItem> totalWeightedItems) {
        float chance = (float) weightedItem.weight / WeightedRandom.getTotalWeight(totalWeightedItems);
        StringBuilder chanceTooltip = new StringBuilder();
        if (weightedItem.isRanged()) {
            chanceTooltip.append(weightedItem.min).append("-").append(weightedItem.max);
        } else {
            chanceTooltip.append(weightedItem.min);
        }
        chanceTooltip.append(" (").append(MathUtils.format(chance, "0.00%")).append(")");
        return chanceTooltip.toString();
    }
}
