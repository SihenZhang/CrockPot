package com.sihenzhang.crockpot.tag;

import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import com.sihenzhang.crockpot.util.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class CrockPotItemTags {
    private CrockPotItemTags() {
    }

    public static final TagKey<Item> CROCK_POTS = TagUtils.createItemTag("crock_pots");
    public static final TagKey<Item> MILKMADE_HATS = TagUtils.createItemTag("milkmade_hats");
    public static final TagKey<Item> PARROT_EGGS = TagUtils.createItemTag("parrot_eggs");

    public static final TagKey<Item> CROPS_ASPARAGUS = TagUtils.createForgeItemTag("crops/asparagus");
    public static final TagKey<Item> CROPS_CORN = TagUtils.createForgeItemTag("crops/corn");
    public static final TagKey<Item> CROPS_EGGPLANT = TagUtils.createForgeItemTag("crops/eggplant");
    public static final TagKey<Item> CROPS_GARLIC = TagUtils.createForgeItemTag("crops/garlic");
    public static final TagKey<Item> CROPS_ONION = TagUtils.createForgeItemTag("crops/onion");
    public static final TagKey<Item> CROPS_PEPPER = TagUtils.createForgeItemTag("crops/pepper");
    public static final TagKey<Item> CROPS_TOMATO = TagUtils.createForgeItemTag("crops/tomato");
    public static final TagKey<Item> SEEDS_ASPARAGUS = TagUtils.createForgeItemTag("seeds/asparagus");
    public static final TagKey<Item> SEEDS_CORN = TagUtils.createForgeItemTag("seeds/corn");
    public static final TagKey<Item> SEEDS_EGGPLANT = TagUtils.createForgeItemTag("seeds/eggplant");
    public static final TagKey<Item> SEEDS_GARLIC = TagUtils.createForgeItemTag("seeds/garlic");
    public static final TagKey<Item> SEEDS_ONION = TagUtils.createForgeItemTag("seeds/onion");
    public static final TagKey<Item> SEEDS_PEPPER = TagUtils.createForgeItemTag("seeds/pepper");
    public static final TagKey<Item> SEEDS_TOMATO = TagUtils.createForgeItemTag("seeds/tomato");
    public static final TagKey<Item> VEGETABLES = TagUtils.createForgeItemTag("vegetables");
    public static final TagKey<Item> VEGETABLES_BEETROOT = TagUtils.createForgeItemTag("vegetables/beetroot");
    public static final TagKey<Item> VEGETABLES_CARROT = TagUtils.createForgeItemTag("vegetables/carrot");
    public static final TagKey<Item> VEGETABLES_POTATO = TagUtils.createForgeItemTag("vegetables/potato");
    public static final TagKey<Item> VEGETABLES_PUMPKIN = TagUtils.createForgeItemTag("vegetables/pumpkin");
    public static final TagKey<Item> VEGETABLES_ASPARAGUS = TagUtils.createForgeItemTag("vegetables/asparagus");
    public static final TagKey<Item> VEGETABLES_CORN = TagUtils.createForgeItemTag("vegetables/corn");
    public static final TagKey<Item> VEGETABLES_EGGPLANT = TagUtils.createForgeItemTag("vegetables/eggplant");
    public static final TagKey<Item> VEGETABLES_GARLIC = TagUtils.createForgeItemTag("vegetables/garlic");
    public static final TagKey<Item> VEGETABLES_ONION = TagUtils.createForgeItemTag("vegetables/onion");
    public static final TagKey<Item> VEGETABLES_PEPPER = TagUtils.createForgeItemTag("vegetables/pepper");
    public static final TagKey<Item> VEGETABLES_TOMATO = TagUtils.createForgeItemTag("vegetables/tomato");

    public static final TagKey<Item> CURIO = TagUtils.createItemTag(ModIntegrationCurios.MOD_ID, "curio");
    public static final TagKey<Item> HEAD = TagUtils.createItemTag(ModIntegrationCurios.MOD_ID, "head");
}
