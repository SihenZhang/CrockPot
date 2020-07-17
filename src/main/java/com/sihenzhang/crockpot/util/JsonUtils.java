package com.sihenzhang.crockpot.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.base.CrockPotIngredientType;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

public class JsonUtils {
    public static Item deserializeItem(String resourceName) {
        return ForgeRegistries.ITEMS.getValue(new ResourceLocation(resourceName));
    }

    public static String serializeItem(Item item) {
        return Objects.requireNonNull(item.getRegistryName()).toString();
    }

    public static EnumMap<CrockPotIngredientType, Float> deserializeIngredientValue(JsonObject object) {
        EnumMap<CrockPotIngredientType, Float> ingredientValue = new EnumMap<>(CrockPotIngredientType.class);
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            ingredientValue.put(CrockPotIngredientType.valueOf(entry.getKey()), entry.getValue().getAsFloat());
        }
        return ingredientValue;
    }

    public static JsonObject serializeIngredientValue(EnumMap<CrockPotIngredientType, Float> ingredientValue) {
        JsonObject object = new JsonObject();
        for (Map.Entry<CrockPotIngredientType, Float> entry : ingredientValue.entrySet()) {
            object.addProperty(entry.getKey().name(), entry.getValue());
        }
        return object;
    }
}
