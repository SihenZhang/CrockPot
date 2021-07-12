package com.sihenzhang.crockpot.base;

import com.google.gson.*;
import com.sihenzhang.crockpot.utils.JsonUtils;
import net.minecraft.item.Item;

import java.lang.reflect.Type;
import java.util.EnumMap;
import java.util.Objects;

public final class CategoryDefinitionItem {
    final Item item;
    final EnumMap<FoodCategory, Float> foodValue;

    public CategoryDefinitionItem(Item item, EnumMap<FoodCategory, Float> foodValue) {
        this.item = item;
        this.foodValue = foodValue;
    }

    public Item getItem() {
        return item;
    }

    public EnumMap<FoodCategory, Float> getValues() {
        return foodValue;
    }

    public static final class Serializer implements JsonDeserializer<CategoryDefinitionItem>, JsonSerializer<CategoryDefinitionItem> {
        @Override
        public CategoryDefinitionItem deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            Item item = JsonUtils.getAsItem(object, "item");
            EnumMap<FoodCategory, Float> foodValue = JsonUtils.getAsEnumMap(object, "values", FoodCategory.class, Float.class);
            return new CategoryDefinitionItem(item, foodValue);
        }

        @Override
        public JsonElement serialize(CategoryDefinitionItem src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.addProperty("item", Objects.requireNonNull(src.item.getRegistryName()).toString());
            JsonObject foodValueJsonObject = new JsonObject();
            src.foodValue.forEach((k, v) -> foodValueJsonObject.addProperty(k.name(), v));
            object.add("values", foodValueJsonObject);
            return object;
        }
    }
}
