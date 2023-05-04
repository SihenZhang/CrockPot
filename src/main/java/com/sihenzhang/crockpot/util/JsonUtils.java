package com.sihenzhang.crockpot.util;

import com.google.gson.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.EnumUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class JsonUtils {
    public static final Gson GSON = new GsonBuilder().create();

    @Nullable
    public static Item convertToItem(JsonElement json, String memberName) {
        if (GsonHelper.isStringValue(json)) {
            String s = json.getAsString();
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
            return item == Items.AIR ? null : item;
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be an item, was " + GsonHelper.getType(json));
        }
    }

    @Nullable
    public static Item getAsItem(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return convertToItem(json.get(memberName), memberName);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an item");
        }
    }

    public static ItemStack convertToItemStack(JsonElement json, String memberName) {
        if (json.isJsonObject()) {
            return ShapedRecipe.itemStackFromJson(json.getAsJsonObject());
        } else if (GsonHelper.isStringValue(json)) {
            return GsonHelper.convertToItem(json, memberName).getDefaultInstance();
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be an item stack(String or JsonObject), was " + GsonHelper.getType(json));
        }
    }

    public static ItemStack getAsItemStack(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return convertToItemStack(json.get(memberName), memberName);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an item stack(String or JsonObject)");
        }
    }

    @Nonnull
    public static Ingredient getAsIngredient(JsonObject json, String memberName) {
        return getAsIngredient(json, memberName, false);
    }

    public static Ingredient getAsIngredient(JsonObject json, String memberName, boolean skip) {
        if (json.has(memberName)) {
            if (skip) {
                if (json.get(memberName).isJsonArray()) {
                    JsonArray array = json.getAsJsonArray(memberName);
                    JsonArray result = new JsonArray();
                    for (JsonElement e : array) {
                        JsonObject obj = GsonHelper.convertToJsonObject(e, "item");
                        if (obj.has("item") && obj.has("tag")) {
                            throw new JsonParseException("An ingredient entry is either a tag or an item, not both");
                        }
                        if (!obj.has("item") && !obj.has("tag")) {
                            throw new JsonParseException("An ingredient entry needs either a tag or an item");
                        }
                        if (obj.has("item")) {
                            ResourceLocation name = new ResourceLocation(GsonHelper.getAsString(obj, "item"));
                            Item item = ForgeRegistries.ITEMS.getValue(name);
                            if (item == null || item == Items.AIR) {
                                continue;
                            }
                        }
                        result.add(e);
                    }
                    return Ingredient.fromJson(result);
                } else {
                    return Ingredient.fromJson(json.get(memberName));
                }
            } else {
                return Ingredient.fromJson(json.get(memberName));
            }
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an ingredient");
        }
    }

    public static <E extends Enum<E>> E convertToEnum(JsonElement json, String memberName, Class<E> enumClass) {
        if (GsonHelper.isStringValue(json)) {
            String enumName = GsonHelper.convertToString(json, memberName).toUpperCase();
            if (!EnumUtils.isValidEnum(enumClass, enumName)) {
                throw new JsonSyntaxException("Expected " + memberName + " to be an enum of " + enumClass.getName() + ", was unknown name: '" + enumName + "'");
            }
            return EnumUtils.getEnum(enumClass, enumName);
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be an enum of " + enumClass.getName() + ", was" + GsonHelper.getType(json));
        }
    }

    public static <E extends Enum<E>> E getAsEnum(JsonObject json, String memberName, Class<E> enumClass) {
        if (json.has(memberName)) {
            return convertToEnum(json.get(memberName), memberName, enumClass);
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an enum");
        }
    }
}
