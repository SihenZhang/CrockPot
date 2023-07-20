package com.sihenzhang.crockpot.item;

import com.google.common.base.Suppliers;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.item.food.*;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class CrockPotItems {
    private CrockPotItems() {
    }

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrockPot.MOD_ID);

    public static final RegistryObject<Item> BASIC_CROCK_POT = ITEMS.register("crock_pot_basic", () -> new CrockPotBlockItem(CrockPotBlocks.BASIC_CROCK_POT.get()));
    public static final RegistryObject<Item> ADVANCED_CROCK_POT = ITEMS.register("crock_pot_advanced", () -> new CrockPotBlockItem(CrockPotBlocks.ADVANCED_CROCK_POT.get()));
    public static final RegistryObject<Item> ULTIMATE_CROCK_POT = ITEMS.register("crock_pot_ultimate", () -> new CrockPotBlockItem(CrockPotBlocks.ULTIMATE_CROCK_POT.get()));

    public static final RegistryObject<Item> UNKNOWN_SEEDS = ITEMS.register("unknown_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.UNKNOWN_CROPS.get()));
    public static final RegistryObject<Item> ASPARAGUS_SEEDS = ITEMS.register("asparagus_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.ASPARAGUS.get()));
    public static final RegistryObject<Item> ASPARAGUS = ITEMS.register("asparagus", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> CORN_SEEDS = ITEMS.register("corn_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.CORN.get()));
    public static final RegistryObject<Item> CORN = ITEMS.register("corn", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> POPCORN = ITEMS.register("popcorn", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.8F).duration(FoodUseDuration.FAST).hideEffects().build());
    public static final RegistryObject<Item> EGGPLANT_SEEDS = ITEMS.register("eggplant_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.EGGPLANT.get()));
    public static final RegistryObject<Item> EGGPLANT = ITEMS.register("eggplant", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> COOKED_EGGPLANT = ITEMS.register("cooked_eggplant", () -> CrockPotFoodItem.builder().nutrition(5).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> GARLIC_SEEDS = ITEMS.register("garlic_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.GARLIC.get()));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> ONION_SEEDS = ITEMS.register("onion_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.ONION.get()));
    public static final RegistryObject<Item> ONION = ITEMS.register("onion", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> PEPPER_SEEDS = ITEMS.register("pepper_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.PEPPER.get()));
    public static final RegistryObject<Item> PEPPER = ITEMS.register("pepper", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).damage(CrockPotDamageSource.SPICY, 1).hideEffects().build());
    public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.TOMATO.get()));
    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());

    public static final Supplier<Set<Item>> SEEDS = Suppliers.memoize(() -> Set.of(UNKNOWN_SEEDS.get(), ASPARAGUS_SEEDS.get(), CORN_SEEDS.get(), EGGPLANT_SEEDS.get(), GARLIC_SEEDS.get(), ONION_SEEDS.get(), PEPPER_SEEDS.get(), TOMATO_SEEDS.get()));
    public static final Supplier<Set<Item>> CROPS = Suppliers.memoize(() -> Set.of(ASPARAGUS.get(), CORN.get(), EGGPLANT.get(), GARLIC.get(), ONION.get(), PEPPER.get(), TOMATO.get()));
    public static final Supplier<Set<Item>> COOKED_CROPS = Suppliers.memoize(() -> Set.of(POPCORN.get(), COOKED_EGGPLANT.get()));

    public static final RegistryObject<Item> BIRDCAGE = ITEMS.register("birdcage", () -> new BlockItem(CrockPotBlocks.BIRDCAGE.get(), new Item.Properties()));
    public static final Map<Parrot.Variant, RegistryObject<Item>> PARROT_EGGS = Util.make(new EnumMap<>(Parrot.Variant.class), map -> {
        for (var variant : Parrot.Variant.values()) {
            map.put(variant, ITEMS.register("parrot_egg_" + variant.getSerializedName(), () -> new ParrotEggItem(variant)));
        }
    });

    public static final RegistryObject<Item> BLACKSTONE_DUST = ITEMS.register("blackstone_dust", CrockPotBaseItem::new);
    public static final RegistryObject<Item> COLLECTED_DUST = ITEMS.register("collected_dust", CollectedDustItem::new);
    public static final RegistryObject<Item> COOKED_EGG = ITEMS.register("cooked_egg", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> FROG_LEGS = ITEMS.register("frog_legs", () -> CrockPotFoodItem.builder().nutrition(2).saturationMod(0.4F).meat().hideEffects().build());
    public static final RegistryObject<Item> COOKED_FROG_LEGS = ITEMS.register("cooked_frog_legs", () -> CrockPotFoodItem.builder().nutrition(5).saturationMod(0.7F).meat().hideEffects().build());
    public static final RegistryObject<Item> HOGLIN_NOSE = ITEMS.register("hoglin_nose", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.2F).meat().hideEffects().build());
    public static final RegistryObject<Item> COOKED_HOGLIN_NOSE = ITEMS.register("cooked_hoglin_nose", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.7F).meat().hideEffects().build());
    public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle", () -> CrockPotFoodItem.builder().nutrition(0).saturationMod(0.0F).alwaysEat().drink().tooltip("milk_bottle", ChatFormatting.DARK_AQUA).hideEffects().build());
    public static final RegistryObject<Item> SYRUP = ITEMS.register("syrup", () -> CrockPotFoodItem.builder().nutrition(1).saturationMod(0.3F).drink().eatingSound(SoundEvents.HONEY_DRINK).hideEffects().build());
    public static final RegistryObject<Item> MILKMADE_HAT = ITEMS.register("milkmade_hat", MilkmadeHatItem::new);
    public static final RegistryObject<Item> CREATIVE_MILKMADE_HAT = ITEMS.register("creative_milkmade_hat", CreativeMilkmadeHatItem::new);
    public static final RegistryObject<Item> GNAWS_COIN = ITEMS.register("gnaws_coin", GnawsCoinItem::new);

    public static final RegistryObject<Item> ASPARAGUS_SOUP = ITEMS.register("asparagus_soup", () -> CrockPotFoodItem.builder().nutrition(4).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().removeEffect(MobEffects.WEAKNESS).removeEffect(MobEffects.DIG_SLOWDOWN).removeEffect(MobEffects.BLINDNESS).removeEffect(MobEffects.BAD_OMEN).build());
    public static final RegistryObject<Item> AVAJ = ITEMS.register("avaj", () -> CrockPotFoodItem.builder().nutrition(2).saturationMod(3.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, (32 * 60 + 20) * 20, 2).hide().rarity(Rarity.EPIC).build());
    public static final RegistryObject<Item> BACON_EGGS = ITEMS.register("bacon_eggs", () -> CrockPotFoodItem.builder().nutrition(12).saturationMod(0.8F).heal(4.0F).build());
    public static final RegistryObject<Item> BONE_SOUP = ITEMS.register("bone_soup", () -> CrockPotFoodItem.builder().nutrition(10).saturationMod(0.6F).effect(MobEffects.ABSORPTION, 2 * 60 * 20, 1).build());
    public static final RegistryObject<Item> BONE_STEW = ITEMS.register("bone_stew", () -> CrockPotFoodItem.builder().nutrition(20).saturationMod(0.4F).duration(FoodUseDuration.SUPER_SLOW).effect(MobEffects.HEAL, 1, 1).build());
    public static final RegistryObject<Item> BREAKFAST_SKILLET = ITEMS.register("breakfast_skillet", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.8F).build());
    public static final RegistryObject<Item> BUNNY_STEW = ITEMS.register("bunny_stew", () -> CrockPotFoodItem.builder().nutrition(6).saturationMod(0.8F).effect(MobEffects.REGENERATION, 5 * 20).effect(CrockPotEffects.WELL_FED, 2 * 60 * 20).build());
    public static final RegistryObject<Item> CALIFORNIA_ROLL = ITEMS.register("california_roll", () -> CrockPotFoodItem.builder().nutrition(10).saturationMod(0.6F).heal(4.0F).effect(MobEffects.ABSORPTION, 60 * 20).build());
    public static final RegistryObject<Item> CANDY = ITEMS.register("candy", CandyItem::new);
    public static final RegistryObject<Item> CEVICHE = ITEMS.register("ceviche", () -> CrockPotFoodItem.builder().nutrition(7).saturationMod(0.7F).alwaysEat().effect(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 1).effect(MobEffects.ABSORPTION, 20 * 20, 1).build());
    public static final RegistryObject<Item> FISH_STICKS = ITEMS.register("fish_sticks", () -> CrockPotFoodItem.builder().nutrition(7).saturationMod(0.7F).effect(MobEffects.REGENERATION, 30 * 20).build());
    public static final RegistryObject<Item> FISH_TACOS = ITEMS.register("fish_tacos", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.9F).heal(2.0F).build());
    public static final RegistryObject<Item> FLOWER_SALAD = ITEMS.register("flower_salad", FlowerSaladItem::new);
    public static final RegistryObject<Item> FROGGLE_BUNWICH = ITEMS.register("froggle_bunwich", () -> CrockPotFoodItem.builder().nutrition(7).saturationMod(0.8F).build());
    public static final RegistryObject<Item> FRUIT_MEDLEY = ITEMS.register("fruit_medley", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.4F).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).build());
    public static final RegistryObject<Item> GAZPACHO = ITEMS.register("gazpacho", () -> CrockPotFoodItem.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.FIRE_RESISTANCE, 10 * 60 * 20).build());
    public static final RegistryObject<Item> GLOW_BERRY_MOUSSE = ITEMS.register("glow_berry_mousse", () -> CrockPotFoodItem.builder().nutrition(6).saturationMod(0.6F).duration(FoodUseDuration.FAST).effect(MobEffects.GLOWING, 10 * 20).build());
    public static final RegistryObject<Item> HONEY_HAM = ITEMS.register("honey_ham", () -> CrockPotFoodItem.builder().nutrition(12).saturationMod(0.8F).effect(MobEffects.REGENERATION, 20 * 20).effect(MobEffects.ABSORPTION, 60 * 20, 1).heal(6.0F).build());
    public static final RegistryObject<Item> HONEY_NUGGETS = ITEMS.register("honey_nuggets", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.3F).effect(MobEffects.REGENERATION, 10 * 20).effect(MobEffects.ABSORPTION, 60 * 20).heal(4.0F).build());
    public static final RegistryObject<Item> HOT_CHILI = ITEMS.register("hot_chili", () -> CrockPotFoodItem.builder().nutrition(9).saturationMod(0.8F).effect(MobEffects.DAMAGE_BOOST, (60 + 30) * 20).effect(MobEffects.DIG_SPEED, (60 + 30) * 20).build());
    public static final RegistryObject<Item> HOT_COCOA = ITEMS.register("hot_cocoa", () -> CrockPotFoodItem.builder().nutrition(2).saturationMod(0.1F).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 8 * 60 * 20, 1).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).removeEffect(MobEffects.DIG_SLOWDOWN).build());
    public static final RegistryObject<Item> ICE_CREAM = ITEMS.register("ice_cream", IceCreamItem::new);
    public static final RegistryObject<Item> ICED_TEA = ITEMS.register("iced_tea", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.1F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.JUMP, 5 * 60 * 20, 1).build());
    public static final RegistryObject<Item> JAMMY_PRESERVES = ITEMS.register("jammy_preserves", () -> CrockPotFoodItem.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).build());
    public static final RegistryObject<Item> KABOBS = ITEMS.register("kabobs", () -> CrockPotFoodItem.builder().nutrition(7).saturationMod(0.7F).build());
    public static final RegistryObject<Item> MASHED_POTATOES = ITEMS.register("mashed_potatoes", () -> CrockPotFoodItem.builder().nutrition(9).saturationMod(0.6F).effect(MobEffects.DAMAGE_RESISTANCE, 4 * 60 * 20).build());
    public static final RegistryObject<Item> MEAT_BALLS = ITEMS.register("meat_balls", () -> CrockPotFoodItem.builder().nutrition(9).saturationMod(0.5F).build());
    public static final RegistryObject<Item> MONSTER_LASAGNA = ITEMS.register("monster_lasagna", () -> CrockPotFoodItem.builder().nutrition(7).saturationMod(0.2F).effect(MobEffects.HUNGER, 15 * 20).effect(MobEffects.POISON, 2 * 20).damage(CrockPotDamageSource.MONSTER_FOOD, 6.0F).build());
    public static final RegistryObject<Item> MONSTER_TARTARE = ITEMS.register("monster_tartare", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.7F).effect(MobEffects.DAMAGE_BOOST, 2 * 60 * 20, 1).build());
    public static final RegistryObject<Item> MOQUECA = ITEMS.register("moqueca", () -> CrockPotFoodItem.builder().nutrition(14).saturationMod(0.7F).duration(FoodUseDuration.SLOW).effect(MobEffects.HEALTH_BOOST, (60 + 30) * 20, 2).heal(6.0F).build());
    public static final RegistryObject<Item> MUSHY_CAKE = ITEMS.register("mushy_cake", () -> CrockPotFoodItem.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(CrockPotEffects.WITHER_RESISTANCE, 60 * 20).build());
    public static final RegistryObject<Item> NETHEROSIA = ITEMS.register("netherosia", NetherosiaItem::new);
    public static final RegistryObject<Item> PEPPER_POPPER = ITEMS.register("pepper_popper", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.8F).effect(MobEffects.DAMAGE_BOOST, 60 * 20, 1).build());
    public static final RegistryObject<Item> PEROGIES = ITEMS.register("perogies", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.8F).heal(6.0F).build());
    public static final RegistryObject<Item> PLAIN_OMELETTE = ITEMS.register("plain_omelette", () -> CrockPotFoodItem.builder(7, 0.6F).build());
    public static final RegistryObject<Item> POTATO_SOUFFLE = ITEMS.register("potato_souffle", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.7F).effect(MobEffects.DAMAGE_RESISTANCE, (60 + 30) * 20, 1).build());
    public static final RegistryObject<Item> POTATO_TORNADO = ITEMS.register("potato_tornado", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(0.6F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build());
    public static final RegistryObject<Item> POW_CAKE = ITEMS.register("pow_cake", () -> CrockPotFoodItem.builder().nutrition(2).saturationMod(0.1F).alwaysEat().damage(CrockPotDamageSource.POW_CAKE, 1.0F).build());
    public static final RegistryObject<Item> PUMPKIN_COOKIE = ITEMS.register("pumpkin_cookie", () -> CrockPotFoodItem.builder().nutrition(10).saturationMod(0.7F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build());
    public static final RegistryObject<Item> RATATOUILLE = ITEMS.register("ratatouille", () -> CrockPotFoodItem.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).build());
    public static final RegistryObject<Item> SALMON_SUSHI = ITEMS.register("salmon_sushi", () -> CrockPotFoodItem.builder().nutrition(5).saturationMod(0.8F).duration(FoodUseDuration.FAST).heal(1.0F).build());
    public static final RegistryObject<Item> SALSA = ITEMS.register("salsa", () -> CrockPotFoodItem.builder().nutrition(7).saturationMod(0.8F).duration(FoodUseDuration.FAST).effect(MobEffects.DIG_SPEED, 6 * 60 * 20).build());
    public static final RegistryObject<Item> SCOTCH_EGG = ITEMS.register("scotch_egg", () -> CrockPotFoodItem.builder(8, 1.0F).build());
    public static final RegistryObject<Item> SEAFOOD_GUMBO = ITEMS.register("seafood_gumbo", () -> CrockPotFoodItem.builder().nutrition(9).saturationMod(0.7F).effect(MobEffects.REGENERATION, 2 * 60 * 20).build());
    public static final RegistryObject<Item> STUFFED_EGGPLANT = ITEMS.register("stuffed_eggplant", () -> CrockPotFoodItem.builder().nutrition(7).saturationMod(0.6F).duration(FoodUseDuration.FAST).heal(2.0F).build());
    public static final RegistryObject<Item> SURF_N_TURF = ITEMS.register("surf_n_turf", () -> CrockPotFoodItem.builder().nutrition(8).saturationMod(1.2F).alwaysEat().effect(MobEffects.REGENERATION, 30 * 20, 1).heal(8.0F).build());
    public static final RegistryObject<Item> TAFFY = ITEMS.register("taffy", () -> CrockPotFoodItem.builder().nutrition(5).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(MobEffects.LUCK, 8 * 60 * 20).damage(CrockPotDamageSource.TAFFY, 1.0F).removeEffect(MobEffects.POISON).build());
    public static final RegistryObject<Item> TEA = ITEMS.register("tea", () -> CrockPotFoodItem.builder().nutrition(3).saturationMod(0.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.DIG_SPEED, 5 * 60 * 20, 1).build());
    public static final RegistryObject<Item> TROPICAL_BOUILLABAISSE = ITEMS.register("tropical_bouillabaisse", () -> CrockPotFoodItem.builder().nutrition(7).saturationMod(0.6F).alwaysEat().effect(CrockPotEffects.OCEAN_AFFINITY, (2 * 60 + 30) * 20).build());
    public static final RegistryObject<Item> TURKEY_DINNER = ITEMS.register("turkey_dinner", () -> CrockPotFoodItem.builder().nutrition(12).saturationMod(0.8F).effect(MobEffects.HEALTH_BOOST, 3 * 60 * 20).build());
    public static final RegistryObject<Item> VEG_STINGER = ITEMS.register("veg_stinger", () -> CrockPotFoodItem.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.NIGHT_VISION, 10 * 60 * 20).build());
    public static final RegistryObject<Item> WATERMELON_ICLE = ITEMS.register("watermelon_icle", () -> CrockPotFoodItem.builder().nutrition(5).saturationMod(0.4F).duration(FoodUseDuration.FAST).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).effect(MobEffects.JUMP, 3 * 60 * 20).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).build());
    public static final RegistryObject<Item> WET_GOOP = ITEMS.register("wet_goop", () -> CrockPotFoodItem.builder().nutrition(0).saturationMod(0.0F).duration(FoodUseDuration.SUPER_SLOW).alwaysEat().effect(MobEffects.CONFUSION, 10 * 20).tooltip("wet_goop", ChatFormatting.DARK_AQUA).build());

    public static final Supplier<List<Item>> FOODS_WITHOUT_AVAJ = Suppliers.memoize(() -> List.of(
            ASPARAGUS_SOUP.get(), BACON_EGGS.get(), BONE_SOUP.get(), BONE_STEW.get(), BREAKFAST_SKILLET.get(),
            BUNNY_STEW.get(), CALIFORNIA_ROLL.get(), CANDY.get(), CEVICHE.get(), FISH_STICKS.get(),
            FISH_TACOS.get(), FLOWER_SALAD.get(), FROGGLE_BUNWICH.get(), FRUIT_MEDLEY.get(), GAZPACHO.get(),
            GLOW_BERRY_MOUSSE.get(), HONEY_HAM.get(), HONEY_NUGGETS.get(), HOT_CHILI.get(), HOT_COCOA.get(),
            ICE_CREAM.get(), ICED_TEA.get(), JAMMY_PRESERVES.get(), KABOBS.get(), MASHED_POTATOES.get(),
            MEAT_BALLS.get(), MONSTER_LASAGNA.get(), MONSTER_TARTARE.get(), MOQUECA.get(), MUSHY_CAKE.get(),
            PEPPER_POPPER.get(), PEROGIES.get(), PLAIN_OMELETTE.get(), POTATO_SOUFFLE.get(), POTATO_TORNADO.get(),
            POW_CAKE.get(), PUMPKIN_COOKIE.get(), RATATOUILLE.get(), SALMON_SUSHI.get(), SALSA.get(),
            SCOTCH_EGG.get(), SEAFOOD_GUMBO.get(), STUFFED_EGGPLANT.get(), SURF_N_TURF.get(), TAFFY.get(),
            TEA.get(), TROPICAL_BOUILLABAISSE.get(), TURKEY_DINNER.get(), VEG_STINGER.get(), WATERMELON_ICLE.get()
    ));

    public static final RegistryObject<Item> VOLT_GOAT_SPAWN_EGG = ITEMS.register("volt_goat_spawn_egg", () -> new ForgeSpawnEggItem(CrockPotEntities.VOLT_GOAT, 0x8B8B8B, 0x000000, new Item.Properties()));

    public static final Map<FoodCategory, RegistryObject<Item>> FOOD_CATEGORY_ITEMS = Util.make(new EnumMap<>(FoodCategory.class), map -> {
        for (FoodCategory category : FoodCategory.values()) {
            map.put(category, ITEMS.register("food_category_" + category.name().toLowerCase(), CrockPotBaseItem::new));
        }
    });
}
