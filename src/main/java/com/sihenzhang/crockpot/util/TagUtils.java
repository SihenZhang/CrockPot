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
        return ItemTags.create(RLUtils.createForgeRL(name));
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
