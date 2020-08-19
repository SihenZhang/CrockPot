package com.sihenzhang.crockpot.base;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.util.JSONUtils;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class CrockPotIngredient {
    protected Item item;
    protected EnumMap<CrockPotIngredientType, Float> ingredientValue;

    public CrockPotIngredient(Item item, EnumMap<CrockPotIngredientType, Float> ingredientValue) {
        this.item = item;
        this.ingredientValue = ingredientValue;
    }

    public Item getItem() {
        return item;
    }

    public EnumMap<CrockPotIngredientType, Float> getIngredientValue() {
        return ingredientValue;
    }

    public float getIngredient(CrockPotIngredientType type) {
        return ingredientValue.getOrDefault(type, 0F);
    }

    public static class Serializer implements JsonDeserializer<CrockPotIngredient>, JsonSerializer<CrockPotIngredient> {
        @Override
        public CrockPotIngredient deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = (JsonObject) json;
            Item item = JSONUtils.getItem(object, "ingredient");
            JsonObject ingredientValueJsonObject = JSONUtils.getJsonObject(object, "value");
            EnumMap<CrockPotIngredientType, Float> ingredientValue = new EnumMap<>(CrockPotIngredientType.class);
            for (Map.Entry<String, JsonElement> entry : ingredientValueJsonObject.entrySet()) {
                ingredientValue.put(CrockPotIngredientType.valueOf(entry.getKey().toUpperCase()), JSONUtils.getFloat(entry.getValue(), "crock pot ingredient value"));
            }
            return new CrockPotIngredient(item, ingredientValue);
        }

        @Override
        public JsonElement serialize(CrockPotIngredient src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("ingredient", Objects.requireNonNull(src.item.getRegistryName()).toString());
            JsonObject ingredientValueJsonObject = new JsonObject();
            for (Map.Entry<CrockPotIngredientType, Float> entry : src.ingredientValue.entrySet()) {
                ingredientValueJsonObject.addProperty(entry.getKey().name(), entry.getValue());
            }
            object.add("value", ingredientValueJsonObject);
            return object;
        }
    }
}
