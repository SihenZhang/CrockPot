package com.sihenzhang.crockpot.base;

import com.google.gson.*;
import com.sihenzhang.crockpot.utils.JsonUtils;
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
            EnumMap<FoodCategory, Float> foodValue = JsonUtils.getEnumMap(object, "values", FoodCategory.class, Float.class);
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
