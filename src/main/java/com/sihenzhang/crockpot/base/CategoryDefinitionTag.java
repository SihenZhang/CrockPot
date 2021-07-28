package com.sihenzhang.crockpot.base;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;

public final class CategoryDefinitionTag {
    final String tag;
    final FoodValues foodValues;

    public CategoryDefinitionTag(String tag, FoodValues foodValues) {
        this.tag = tag;
        this.foodValues = foodValues;
    }

    public String getTag() {
        return tag;
    }

    public FoodValues getFoodValues() {
        return foodValues;
    }

    public static final class Serializer implements JsonSerializer<CategoryDefinitionTag>, JsonDeserializer<CategoryDefinitionTag> {
        @Override
        public CategoryDefinitionTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            String tag = JSONUtils.getAsString(object, "tag");
            FoodValues foodValues = FoodValues.fromJson(JSONUtils.getAsJsonObject(object, "values"));
            return new CategoryDefinitionTag(tag, foodValues);
        }

        @Override
        public JsonElement serialize(CategoryDefinitionTag src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("tag", src.tag);
            object.add("values", src.foodValues.toJson());
            return object;
        }
    }
}
