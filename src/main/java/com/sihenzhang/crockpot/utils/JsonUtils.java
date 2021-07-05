package com.sihenzhang.crockpot.utils;

import com.google.gson.*;
import com.sihenzhang.crockpot.recipe.WeightedItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.EnumMap;

public final class JsonUtils {
    private static final Gson GSON = new GsonBuilder().create();

    @Nullable
    public static Item getItem(JsonElement json, String memberName) {
        if (json.isJsonPrimitive()) {
            String s = json.getAsString();
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
            return item == Items.AIR ? null : item;
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be an item, was " + JSONUtils.getType(json));
        }
    }

    @Nullable
    public static Item getItem(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return getItem(json.get(memberName), memberName);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an item");
        }
    }

    @Nonnull
    public static Ingredient getIngredient(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return Ingredient.fromJson(json.get(memberName));
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an ingredient");
        }
    }

    @Nonnull
    public static <K extends Enum<K>, V> EnumMap<K, V> getEnumMap(JsonElement json, String memberName, Class<K> enumClass, Class<V> valueClass) {
        if (json.isJsonObject()) {
            JsonObject o = json.getAsJsonObject();
            EnumMap<K, V> enumMap = new EnumMap<>(enumClass);
            o.entrySet().forEach(e -> {
                String enumName = e.getKey();
                if (EnumUtils.isValidEnum(enumClass, enumName.toUpperCase())) {
                    enumMap.put(EnumUtils.getEnum(enumClass, enumName.toUpperCase()), GSON.fromJson(e.getValue(), valueClass));
                } else {
                    throw new JsonSyntaxException("Expected the key of " + memberName + " to be a " + enumClass.getSimpleName() + ", was unknown " + enumClass.getSimpleName() + " name '" + enumName + "'");
                }
            });
            return enumMap;
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be an enum map, was " + JSONUtils.getType(json));
        }
    }

    @Nonnull
    public static <K extends Enum<K>, V> EnumMap<K, V> getEnumMap(JsonObject json, String memberName, Class<K> enumClass, Class<V> valueClass) {
        if (json.has(memberName)) {
            return getEnumMap(json.get(memberName), memberName, enumClass, valueClass);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an enum map");
        }
    }

    @Nullable
    public static WeightedItem getWeightedItem(JsonElement json, String memberName) {
        if (json.isJsonObject()) {
            JsonObject o = json.getAsJsonObject();
            Item item = JsonUtils.getItem(o, "item");
            if (item != null) {
                int weight = JSONUtils.getAsInt(o, "weight", 1);
                if (o.has("count")) {
                    JsonElement e = o.get("count");
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
                        int count = JSONUtils.getAsInt(o, "count", 1);
                        return new WeightedItem(item, count, weight);
                    }
                } else {
                    return new WeightedItem(item, weight);
                }
            } else {
                return null;
            }
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be a weighted item, was " + JSONUtils.getType(json));
        }
    }

    @Nullable
    public static WeightedItem getWeightedItem(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return getWeightedItem(json.get(memberName), memberName);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find a weighted item");
        }
    }
}
