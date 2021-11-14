package com.sihenzhang.crockpot.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.*;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.io.StringReader;

public final class NbtUtils {
    public static JsonElement convertToJson(INBT nbt) {
        JsonReader reader = new JsonReader(new StringReader(nbt.toString()));
        reader.setLenient(true);
        return new JsonParser().parse(reader);
    }

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

    public static Ingredient readIngredient(INBT nbt) {
        Preconditions.checkArgument(nbt != null, "Ingredient cannot be null");
        if (nbt.getType() == ListNBT.TYPE) {
            ListNBT list = (ListNBT) nbt;
            ListNBT result = new ListNBT();
            for (INBT e : list) {
                Preconditions.checkArgument(e.getType() == CompoundNBT.TYPE, "Expcted ingredient to be a object or array of objects");
                CompoundNBT compound = (CompoundNBT) e;
                if (compound.contains("item") && compound.contains("tag")) {
                    throw new IllegalArgumentException("An ingredient entry is either a tag or an item, not both");
                } else if (compound.contains("item")) {
                    String name = compound.getString("item");
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(name));
                    if (item == null || item == Items.AIR) {
                        continue;
                    }
                } else if (compound.contains("tag")) {
                    String name = compound.getString("tag");
                    ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(new ResourceLocation(name));
                    if (tag == null) {
                        continue;
                    }
                } else {
                    throw new IllegalArgumentException("An ingredient entry needs either a tag or an item");
                }
                result.add(e);
            }
            return Ingredient.fromJson(convertToJson(result));
        }
        return Ingredient.fromJson(convertToJson(nbt));
    }

    public static ItemStack setLoreString(ItemStack stack, String string) {
        CompoundNBT displayTag = new CompoundNBT();
        CompoundNBT loreTag = new CompoundNBT();
        ListNBT loreListTag = new ListNBT();
        loreListTag.add(StringNBT.valueOf("{\"text\":\"" + string + "\"}"));
        loreTag.put("Lore", loreListTag);
        displayTag.put("display", loreTag);
        stack.setTag(displayTag);
        return stack;
    }
}
