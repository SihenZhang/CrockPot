package com.sihenzhang.crockpot.base;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;
import java.util.EnumMap;

public final class CategoryDefinitionTag {
    final String tag;
    final EnumMap<FoodCategory, Float> foodValue;

    public CategoryDefinitionTag(String tag, EnumMap<FoodCategory, Float> foodValue) {
        this.tag = tag;
        this.foodValue = foodValue;
    }

    public String getTag() {
        return tag;
    }

    public EnumMap<FoodCategory, Float> getValues() {
        return foodValue;
    }

    public static final class Serializer implements JsonSerializer<CategoryDefinitionTag>, JsonDeserializer<CategoryDefinitionTag> {

        @Override
        public CategoryDefinitionTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            String tag = JSONUtils.getString(object, "tag");
            JsonObject foodValueJsonObject = JSONUtils.getJsonObject(object, "values");
            EnumMap<FoodCategory, Float> foodValue = new EnumMap<>(FoodCategory.class);
            foodValueJsonObject.entrySet().forEach(e -> foodValue.put(FoodCategory.valueOf(e.getKey().toUpperCase()), e.getValue().getAsFloat()));
            return new CategoryDefinitionTag(tag, foodValue);
        }

        @Override
        public JsonElement serialize(CategoryDefinitionTag src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("tag", src.tag);
            JsonObject foodValueJsonObject = new JsonObject();
            src.foodValue.forEach((k, v) -> foodValueJsonObject.addProperty(k.name(), v));
            object.add("values", foodValueJsonObject);
            return object;
        }
    }
}
