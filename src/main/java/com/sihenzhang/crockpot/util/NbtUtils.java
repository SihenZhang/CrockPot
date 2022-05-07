package com.sihenzhang.crockpot.util;

import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITagManager;

import java.io.StringReader;
import java.util.Objects;

public final class NbtUtils {
    public static JsonElement convertToJson(Tag nbt) {
        JsonReader reader = new JsonReader(new StringReader(nbt.toString()));
        reader.setLenient(true);
        return new JsonParser().parse(reader);
    }

    public static Tag writeIngredient(Ingredient ingredient) throws CommandSyntaxException {
        JsonElement json = ingredient.toJson();
        if (json.isJsonObject()) {
            return TagParser.parseTag(json.toString());
        } else {
            ListTag list = new ListTag();
            for (JsonElement e : json.getAsJsonArray()) {
                list.add(TagParser.parseTag(e.toString()));
            }
            return list;
        }
    }

    public static Ingredient readIngredient(Tag nbt) {
        Preconditions.checkArgument(nbt != null, "Ingredient cannot be null");
        if (nbt.getType() == ListTag.TYPE) {
            ListTag list = (ListTag) nbt;
            ListTag result = new ListTag();
            for (Tag e : list) {
                Preconditions.checkArgument(e.getType() == CompoundTag.TYPE, "Expcted ingredient to be a object or array of objects");
                CompoundTag compound = (CompoundTag) e;
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
                    TagKey<Item> tag = ItemTags.create(new ResourceLocation(name));
                    if (!ForgeRegistries.ITEMS.tags().isKnownTagName(tag)) {
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
        CompoundTag displayTag = new CompoundTag();
        CompoundTag loreTag = new CompoundTag();
        ListTag loreListTag = new ListTag();
        loreListTag.add(StringTag.valueOf("{\"text\":\"" + string + "\"}"));
        loreTag.put("Lore", loreListTag);
        displayTag.put("display", loreTag);
        stack.setTag(displayTag);
        return stack;
    }
}
