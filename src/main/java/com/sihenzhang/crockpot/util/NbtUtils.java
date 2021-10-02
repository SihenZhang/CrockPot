package com.sihenzhang.crockpot.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.*;

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

    public static ItemStack setLoreString(ItemStack stack, String string) {
        CompoundNBT displayTag = new CompoundNBT();
        CompoundNBT loreTag = new CompoundNBT();
        ListNBT loreListTag = new ListNBT();
        loreListTag.add(0, StringNBT.valueOf("{\"text\":\"" + string + "\"}"));
        loreTag.put("Lore", loreListTag);
        displayTag.put("display", loreTag);
        stack.setTag(displayTag);
        return stack;
    }
}
