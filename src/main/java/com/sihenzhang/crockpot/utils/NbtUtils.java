package com.sihenzhang.crockpot.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.ListNBT;

import java.io.StringReader;
import java.util.Objects;

public final class NbtUtils {
    public static INBT writeIngredient(Ingredient ingredient) throws CommandSyntaxException {
        JsonElement json = ingredient.toJson();
        if (json.isJsonObject()) {
            return JsonToNBT.parseTag(json.toString());
        } else {
            ListNBT list = new ListNBT();
            for (JsonElement e : json.getAsJsonArray()) {
                list.add(JsonToNBT.parseTag(e.toString()));
            }
            return list;
        }
    }

    public static Ingredient readIngredient(INBT tag) {
        Objects.requireNonNull(tag);
        JsonReader reader = new JsonReader(new StringReader(tag.toString()));
        reader.setLenient(true);
        return Ingredient.fromJson(new JsonParser().parse(reader));
    }
}
