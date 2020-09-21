package com.sihenzhang.crockpot.base;

import com.google.gson.*;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;

public final class CategoryDefinitionTag {
    final String tag;
    final EnumMap<FoodCategory, Float> ingredientValue;

    public CategoryDefinitionTag(String tag, EnumMap<FoodCategory, Float> ingredientValue) {
        this.tag = tag;
        this.ingredientValue = ingredientValue;
    }

    public String getTag() {
        return tag;
    }

    public EnumMap<FoodCategory, Float> getValues() {
        return ingredientValue;
    }

    public static final class Serializer implements JsonSerializer<CategoryDefinitionTag>, JsonDeserializer<CategoryDefinitionTag> {

        @Override
        public CategoryDefinitionTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            String tag = JSONUtils.getString(object, "tag");
            JsonObject ingredientValueJsonObject = JSONUtils.getJsonObject(object, "values");
            EnumMap<FoodCategory, Float> ingredientValue = new EnumMap<>(FoodCategory.class);
            for (Map.Entry<String, JsonElement> entry : ingredientValueJsonObject.entrySet()) {
                ingredientValue.put(FoodCategory.valueOf(entry.getKey().toUpperCase()), JSONUtils.getFloat(entry.getValue(), "crock pot ingredient value"));
            }
            return new CategoryDefinitionTag(tag, ingredientValue);
        }

        @Override
        public JsonElement serialize(CategoryDefinitionTag src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("tag", src.tag);
            JsonObject ingredientValueJsonObject = new JsonObject();
            for (Map.Entry<FoodCategory, Float> entry : src.ingredientValue.entrySet()) {
                ingredientValueJsonObject.addProperty(entry.getKey().name(), entry.getValue());
            }
            object.add("values", ingredientValueJsonObject);
            return object;
        }
    }
}
