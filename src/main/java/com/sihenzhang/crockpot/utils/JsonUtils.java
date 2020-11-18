package com.sihenzhang.crockpot.utils;

import com.google.gson.*;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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
            throw new JsonSyntaxException("Expected " + memberName + " to be an item, was " + JSONUtils.toString(json));
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
    public static <K extends Enum<K>, V> EnumMap<K, V> getEnumMap(JsonElement json, String memberName, Class<K> enumClass, Class<V> valueClass) {
        if (json.isJsonObject()) {
            JsonObject o = json.getAsJsonObject();
            EnumMap<K, V> enumMap = new EnumMap<>(enumClass);
            o.entrySet().forEach(e -> {
                String enumName = e.getKey();
                if (EnumUtils.isValidEnumIgnoreCase(enumClass, enumName)) {
                    enumMap.put(EnumUtils.getEnumIgnoreCase(enumClass, enumName), GSON.fromJson(e.getValue(), valueClass));
                } else {
                    throw new JsonSyntaxException("Expected the key of " + memberName + " to be a " + enumClass.getSimpleName() + ", was unknown " + enumClass.getSimpleName() + " name '" + enumName + "'");
                }
            });
            return enumMap;
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be an enum map, was " + JSONUtils.toString(json));
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
}
