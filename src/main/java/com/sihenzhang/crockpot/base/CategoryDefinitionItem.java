package com.sihenzhang.crockpot.base;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public final class CategoryDefinitionItem {
    Item item;
    EnumMap<FoodCategory, Float> ingredientValue;

    public CategoryDefinitionItem(Item item, EnumMap<FoodCategory, Float> ingredientValue) {
        this.item = item;
        this.ingredientValue = ingredientValue;
    }

    public Item getItem() {
        return item;
    }

    public EnumMap<FoodCategory, Float> getValues() {
        return ingredientValue;
    }

    public static final class Serializer implements JsonDeserializer<CategoryDefinitionItem>, JsonSerializer<CategoryDefinitionItem> {
        @Override
        public CategoryDefinitionItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            Item item = JSONUtils.getItem(object, "item");
            JsonObject ingredientValueJsonObject = JSONUtils.getJsonObject(object, "values");
            EnumMap<FoodCategory, Float> ingredientValue = new EnumMap<>(FoodCategory.class);
            for (Map.Entry<String, JsonElement> entry : ingredientValueJsonObject.entrySet()) {
                ingredientValue.put(FoodCategory.valueOf(entry.getKey().toUpperCase()), JSONUtils.getFloat(entry.getValue(), "crock pot ingredient value"));
            }
            return new CategoryDefinitionItem(item, ingredientValue);
        }

        @Override
        public JsonElement serialize(CategoryDefinitionItem src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("ingredient", Objects.requireNonNull(src.item.getRegistryName()).toString());
            JsonObject ingredientValueJsonObject = new JsonObject();
            for (Map.Entry<FoodCategory, Float> entry : src.ingredientValue.entrySet()) {
                ingredientValueJsonObject.addProperty(entry.getKey().name(), entry.getValue());
            }
            object.add("values", ingredientValueJsonObject);
            return object;
        }
    }
}
