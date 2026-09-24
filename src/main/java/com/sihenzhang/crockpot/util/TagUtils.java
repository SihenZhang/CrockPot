package com.sihenzhang.crockpot.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class TagUtils {
    private TagUtils() {
    }

    public static TagKey<Item> createItemTag(String name) {
        return ItemTags.create(RLUtils.createRL(name));
    }

    public static TagKey<Item> createItemTag(String modId, String name) {
        return ItemTags.create(RLUtils.createRL(modId, name));
    }

    public static TagKey<Item> createForgeItemTag(String name) {
        var separator = name.indexOf('/');
        var root = separator < 0 ? name : name.substring(0, separator);
        var suffix = separator < 0 ? "" : name.substring(separator);
        var path = switch (root) {
            case "vegetables" -> "foods/vegetable" + suffix;
            case "fruits" -> "foods/fruit" + suffix;
            case "berries" -> "foods/berry" + suffix;
            case "raw_fishes" -> "foods/raw_fish" + suffix;
            case "cooked_fishes" -> "foods/cooked_fish" + suffix;
            default -> switch (name) {
                case "foods/raw_meats" -> "foods/raw_meat";
                case "foods/cooked_meats" -> "foods/cooked_meat";
                default -> name;
            };
        };
        return ItemTags.create(RLUtils.createForgeRL(path));
    }

    public static TagKey<Item> createVanillaItemTag(String name) {
        return ItemTags.create(RLUtils.createVanillaRL(name));
    }

    public static TagKey<Block> createBlockTag(String name) {
        return BlockTags.create(RLUtils.createRL(name));
    }

    public static TagKey<Block> createBlockTag(String modId, String name) {
        return BlockTags.create(RLUtils.createRL(modId, name));
    }

    public static TagKey<Block> createForgeBlockTag(String name) {
        return BlockTags.create(RLUtils.createForgeRL(name));
    }

    public static TagKey<Block> createVanillaBlockTag(String name) {
        return BlockTags.create(RLUtils.createVanillaRL(name));
    }
}
