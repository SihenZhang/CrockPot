package com.sihenzhang.crockpot.base;

import com.google.gson.*;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.item.Item;

import java.lang.reflect.Type;
import java.util.EnumMap;

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
            JsonObject obj = json.getAsJsonObject();
            Item item = JsonUtils.deserializeItem(obj.get("ingredient").getAsString());
            EnumMap<CrockPotIngredientType, Float> ingredientValue = JsonUtils.deserializeIngredientValue(obj.get("value").getAsJsonObject());
            return new CrockPotIngredient(item, ingredientValue);
        }

        @Override
        public JsonElement serialize(CrockPotIngredient src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty("ingredient", JsonUtils.serializeItem(src.item));
            obj.add("value", JsonUtils.serializeIngredientValue(src.ingredientValue));
            return obj;
        }
    }
}
