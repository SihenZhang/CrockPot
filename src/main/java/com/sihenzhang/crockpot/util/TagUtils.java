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
        return ItemTags.create(RLUtils.mod(name));
    }

    public static TagKey<Item> createItemTag(String modId, String name) {
        return ItemTags.create(RLUtils.create(modId, name));
    }

    public static TagKey<Item> createForgeItemTag(String name) {
        return ItemTags.create(RLUtils.forge(name));
    }

    public static TagKey<Item> createVanillaItemTag(String name) {
        return ItemTags.create(RLUtils.vanilla(name));
    }

    public static TagKey<Block> createBlockTag(String name) {
        return BlockTags.create(RLUtils.mod(name));
    }

    public static TagKey<Block> createBlockTag(String modId, String name) {
        return BlockTags.create(RLUtils.create(modId, name));
    }

    public static TagKey<Block> createForgeBlockTag(String name) {
        return BlockTags.create(RLUtils.forge(name));
    }

    public static TagKey<Block> createVanillaBlockTag(String name) {
        return BlockTags.create(RLUtils.vanilla(name));
    }
}
