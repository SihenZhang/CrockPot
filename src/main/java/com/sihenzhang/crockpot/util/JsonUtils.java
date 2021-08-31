package com.sihenzhang.crockpot.util;

import com.google.gson.*;
import com.sihenzhang.crockpot.recipe.WeightedItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class JsonUtils {
    public static final Gson GSON = new GsonBuilder().create();

    @Nullable
    public static Item convertToItem(JsonElement json, String memberName) {
        if (json.isJsonPrimitive()) {
            String s = json.getAsString();
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(s));
            return item == Items.AIR ? null : item;
        } else {
            throw new JsonSyntaxException("Expected " + memberName + " to be an item, was " + JSONUtils.getType(json));
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

    @Nonnull
    public static Ingredient getAsIngredient(JsonObject json, String memberName) {
        if (json.has(memberName)) {
            return Ingredient.fromJson(json.get(memberName));
        } else {
            throw new JsonSyntaxException("Missing " + memberName + ", expected to find an ingredient");
        }
    }
}
