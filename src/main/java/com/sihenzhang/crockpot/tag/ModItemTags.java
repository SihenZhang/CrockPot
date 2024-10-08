package com.sihenzhang.crockpot.tag;

import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import com.sihenzhang.crockpot.util.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class ModItemTags {
    private ModItemTags() {
    }

    public static final TagKey<Item> CROCK_POTS = TagUtils.createItemTag("crock_pots");
    public static final TagKey<Item> MILKMADE_HATS = TagUtils.createItemTag("milkmade_hats");
    public static final TagKey<Item> PARROT_EGGS = TagUtils.createItemTag("parrot_eggs");

    public static final TagKey<Item> CROPS_ASPARAGUS = TagUtils.commonItemTag("crops/asparagus");
    public static final TagKey<Item> CROPS_CORN = TagUtils.commonItemTag("crops/corn");
    public static final TagKey<Item> CROPS_EGGPLANT = TagUtils.commonItemTag("crops/eggplant");
    public static final TagKey<Item> CROPS_GARLIC = TagUtils.commonItemTag("crops/garlic");
    public static final TagKey<Item> CROPS_ONION = TagUtils.commonItemTag("crops/onion");
    public static final TagKey<Item> CROPS_PEPPER = TagUtils.commonItemTag("crops/pepper");
    public static final TagKey<Item> CROPS_TOMATO = TagUtils.commonItemTag("crops/tomato");

    public static final TagKey<Item> SEEDS_ASPARAGUS = TagUtils.commonItemTag("seeds/asparagus");
    public static final TagKey<Item> SEEDS_CORN = TagUtils.commonItemTag("seeds/corn");
    public static final TagKey<Item> SEEDS_EGGPLANT = TagUtils.commonItemTag("seeds/eggplant");
    public static final TagKey<Item> SEEDS_GARLIC = TagUtils.commonItemTag("seeds/garlic");
    public static final TagKey<Item> SEEDS_ONION = TagUtils.commonItemTag("seeds/onion");
    public static final TagKey<Item> SEEDS_PEPPER = TagUtils.commonItemTag("seeds/pepper");
    public static final TagKey<Item> SEEDS_TOMATO = TagUtils.commonItemTag("seeds/tomato");

    public static final TagKey<Item> VEGETABLES = TagUtils.commonItemTag("vegetables");
    public static final TagKey<Item> VEGETABLES_BEETROOT = TagUtils.commonItemTag("vegetables/beetroot");
    public static final TagKey<Item> VEGETABLES_CARROT = TagUtils.commonItemTag("vegetables/carrot");
    public static final TagKey<Item> VEGETABLES_POTATO = TagUtils.commonItemTag("vegetables/potato");
    public static final TagKey<Item> VEGETABLES_PUMPKIN = TagUtils.commonItemTag("vegetables/pumpkin");
    public static final TagKey<Item> VEGETABLES_ASPARAGUS = TagUtils.commonItemTag("vegetables/asparagus");
    public static final TagKey<Item> VEGETABLES_CORN = TagUtils.commonItemTag("vegetables/corn");
    public static final TagKey<Item> VEGETABLES_EGGPLANT = TagUtils.commonItemTag("vegetables/eggplant");
    public static final TagKey<Item> VEGETABLES_GARLIC = TagUtils.commonItemTag("vegetables/garlic");
    public static final TagKey<Item> VEGETABLES_ONION = TagUtils.commonItemTag("vegetables/onion");
    public static final TagKey<Item> VEGETABLES_PEPPER = TagUtils.commonItemTag("vegetables/pepper");
    public static final TagKey<Item> VEGETABLES_TOMATO = TagUtils.commonItemTag("vegetables/tomato");

    public static final TagKey<Item> FRUITS = TagUtils.commonItemTag("fruits");
    public static final TagKey<Item> FRUITS_APPLE = TagUtils.commonItemTag("fruits/apple");

    public static final TagKey<Item> FOODS_ASPARAGUS = TagUtils.commonItemTag("foods/asparagus");
    public static final TagKey<Item> FOODS_CORN = TagUtils.commonItemTag("foods/corn");
    public static final TagKey<Item> FOODS_EGGPLANT = TagUtils.commonItemTag("foods/eggplant");
    public static final TagKey<Item> FOODS_GARLIC = TagUtils.commonItemTag("foods/garlic");
    public static final TagKey<Item> FOODS_ONION = TagUtils.commonItemTag("foods/onion");
    public static final TagKey<Item> FOODS_PEPPER = TagUtils.commonItemTag("foods/pepper");
    public static final TagKey<Item> FOODS_TOMATO = TagUtils.commonItemTag("foods/tomato");

    public static final TagKey<Item> RAW_BEEF = TagUtils.commonItemTag("raw_beef");
    public static final TagKey<Item> RAW_CHICKEN = TagUtils.commonItemTag("raw_chicken");
    public static final TagKey<Item> RAW_MUTTON = TagUtils.commonItemTag("raw_mutton");
    public static final TagKey<Item> RAW_PORK = TagUtils.commonItemTag("raw_pork");
    public static final TagKey<Item> RAW_RABBIT = TagUtils.commonItemTag("raw_rabbit");
    public static final TagKey<Item> COOKED_BEEF = TagUtils.commonItemTag("cooked_beef");
    public static final TagKey<Item> COOKED_CHICKEN = TagUtils.commonItemTag("cooked_chicken");
    public static final TagKey<Item> COOKED_MUTTON = TagUtils.commonItemTag("cooked_mutton");
    public static final TagKey<Item> COOKED_PORK = TagUtils.commonItemTag("cooked_pork");
    public static final TagKey<Item> COOKED_RABBIT = TagUtils.commonItemTag("cooked_rabbit");
    public static final TagKey<Item> RAW_FISHES = TagUtils.commonItemTag("raw_fishes");
    public static final TagKey<Item> RAW_FISHES_COD = TagUtils.commonItemTag("raw_fishes/cod");
    public static final TagKey<Item> RAW_FISHES_SALMON = TagUtils.commonItemTag("raw_fishes/salmon");
    public static final TagKey<Item> RAW_FISHES_TROPICAL_FISH = TagUtils.commonItemTag("raw_fishes/tropical_fish");
    public static final TagKey<Item> COOKED_FISHES = TagUtils.commonItemTag("cooked_fishes");
    public static final TagKey<Item> COOKED_FISHES_COD = TagUtils.commonItemTag("cooked_fishes/cod");
    public static final TagKey<Item> COOKED_FISHES_SALMON = TagUtils.commonItemTag("cooked_fishes/salmon");
    public static final TagKey<Item> RAW_FROGS = TagUtils.commonItemTag("raw_frogs");
    public static final TagKey<Item> COOKED_FROGS = TagUtils.commonItemTag("cooked_frogs");

    public static final TagKey<Item> CURIO = TagUtils.createItemTag(ModIntegrationCurios.MOD_ID, "curio");
    public static final TagKey<Item> HEAD = TagUtils.createItemTag(ModIntegrationCurios.MOD_ID, "head");
}
