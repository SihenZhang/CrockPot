package com.sihenzhang.crockpot.item;

import com.google.common.base.Suppliers;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.CrockPotDamageTypes;
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
import net.minecraft.world.item.Items;
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

    public static final RegistryObject<Item> CROCK_POT = ITEMS.register("crock_pot", () -> new CrockPotBlockItem(CrockPotBlocks.CROCK_POT.get()));
    public static final RegistryObject<Item> PORTABLE_CROCK_POT = ITEMS.register("portable_crock_pot", () -> new CrockPotBlockItem(CrockPotBlocks.PORTABLE_CROCK_POT.get()));

    public static final RegistryObject<Item> UNKNOWN_SEEDS = ITEMS.register("unknown_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.UNKNOWN_CROPS.get()));
    public static final RegistryObject<Item> ASPARAGUS_SEEDS = ITEMS.register("asparagus_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.ASPARAGUS.get()));
    public static final RegistryObject<Item> ASPARAGUS = ITEMS.register("asparagus", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final RegistryObject<Item> CORN_SEEDS = ITEMS.register("corn_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.CORN.get()));
    public static final RegistryObject<Item> CORN = ITEMS.register("corn", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final RegistryObject<Item> POPCORN = ITEMS.register("popcorn", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.8F).duration(FoodUseDuration.FAST).hideEffects().build()));
    public static final RegistryObject<Item> EGGPLANT_SEEDS = ITEMS.register("eggplant_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.EGGPLANT.get()));
    public static final RegistryObject<Item> EGGPLANT = ITEMS.register("eggplant", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final RegistryObject<Item> COOKED_EGGPLANT = ITEMS.register("cooked_eggplant", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(5, 0.6F).hideEffects().build()));
    public static final RegistryObject<Item> GARLIC_SEEDS = ITEMS.register("garlic_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.GARLIC.get()));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final RegistryObject<Item> ONION_SEEDS = ITEMS.register("onion_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.ONION.get()));
    public static final RegistryObject<Item> ONION = ITEMS.register("onion", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final RegistryObject<Item> PEPPER_SEEDS = ITEMS.register("pepper_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.PEPPER.get()));
    public static final RegistryObject<Item> PEPPER = ITEMS.register("pepper", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).damage(CrockPotDamageTypes.SPICY, 1).hideEffects().build()));
    public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds", () -> new CrockPotSeedsItem(CrockPotBlocks.TOMATO.get()));
    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));

    public static final Supplier<Set<Item>> SEEDS = Suppliers.memoize(() -> Set.of(UNKNOWN_SEEDS.get(), ASPARAGUS_SEEDS.get(), CORN_SEEDS.get(), EGGPLANT_SEEDS.get(), GARLIC_SEEDS.get(), ONION_SEEDS.get(), PEPPER_SEEDS.get(), TOMATO_SEEDS.get()));
    public static final Supplier<Set<Item>> CROPS = Suppliers.memoize(() -> Set.of(ASPARAGUS.get(), CORN.get(), EGGPLANT.get(), GARLIC.get(), ONION.get(), PEPPER.get(), TOMATO.get()));
    public static final Supplier<Set<Item>> COOKED_CROPS = Suppliers.memoize(() -> Set.of(POPCORN.get(), COOKED_EGGPLANT.get()));

    public static final RegistryObject<Item> BIRDCAGE = ITEMS.register("birdcage", () -> new BlockItem(CrockPotBlocks.BIRDCAGE.get(), new Item.Properties()));
    public static final Map<Parrot.Variant, RegistryObject<Item>> PARROT_EGGS = Util.make(new EnumMap<>(Parrot.Variant.class), map -> {
        for (var variant : Parrot.Variant.values()) {
            map.put(variant, ITEMS.register("parrot_egg_" + variant.getSerializedName(), () -> new ParrotEggItem(variant)));
        }
    });

    public static final RegistryObject<Item> BLACKSTONE_DUST = ITEMS.register("blackstone_dust", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COLLECTED_DUST = ITEMS.register("collected_dust", CollectedDustItem::new);
    public static final RegistryObject<Item> COOKED_EGG = ITEMS.register("cooked_egg", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final RegistryObject<Item> FROG_LEGS = ITEMS.register("frog_legs", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(2, 0.4F).meat().hideEffects().build()));
    public static final RegistryObject<Item> COOKED_FROG_LEGS = ITEMS.register("cooked_frog_legs", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(5, 0.7F).meat().hideEffects().build()));
    public static final RegistryObject<Item> HOGLIN_NOSE = ITEMS.register("hoglin_nose", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.2F).meat().hideEffects().build()));
    public static final RegistryObject<Item> COOKED_HOGLIN_NOSE = ITEMS.register("cooked_hoglin_nose", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(8, 0.7F).meat().hideEffects().build()));
    public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder().alwaysEat().drink().craftRemainder(Items.GLASS_BOTTLE).tooltip("milk_bottle", ChatFormatting.DARK_AQUA).hideEffects().build()));
    public static final RegistryObject<Item> SYRUP = ITEMS.register("syrup", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(1, 0.3F).drink().sound(SoundEvents.HONEY_DRINK).hideEffects().build()));
    public static final RegistryObject<Item> VOLT_GOAT_HORN = ITEMS.register("volt_goat_horn", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> MILKMADE_HAT = ITEMS.register("milkmade_hat", MilkmadeHatItem::new);
    public static final RegistryObject<Item> CREATIVE_MILKMADE_HAT = ITEMS.register("creative_milkmade_hat", CreativeMilkmadeHatItem::new);
    public static final RegistryObject<Item> GNAWS_COIN = ITEMS.register("gnaws_coin", GnawsCoinItem::new);

    public static final RegistryObject<Item> ASPARAGUS_SOUP = ITEMS.register("asparagus_soup", () -> new CrockPotFoodBlockItem(CrockPotBlocks.ASPARAGUS_SOUP.get(), CrockPotFoodProperties.builder(4, 0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().removeEffect(MobEffects.WEAKNESS).removeEffect(MobEffects.DIG_SLOWDOWN).removeEffect(MobEffects.BLINDNESS).removeEffect(MobEffects.BAD_OMEN).build()));
    public static final RegistryObject<Item> AVAJ = ITEMS.register("avaj", () -> new CrockPotFoodBlockItem(CrockPotBlocks.AVAJ.get(), CrockPotFoodProperties.builder(2, 3.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, (32 * 60 + 20) * 20, 2).rarity(Rarity.EPIC).build()));
    public static final RegistryObject<Item> BACON_EGGS = ITEMS.register("bacon_eggs", () -> new CrockPotFoodBlockItem(CrockPotBlocks.BACON_EGGS.get(), CrockPotFoodProperties.builder(12, 0.8F).heal(4.0F).meat().build()));
    public static final RegistryObject<Item> BONE_SOUP = ITEMS.register("bone_soup", () -> new CrockPotFoodBlockItem(CrockPotBlocks.BONE_SOUP.get(), CrockPotFoodProperties.builder(10, 0.6F).effect(MobEffects.ABSORPTION, 2 * 60 * 20, 1).meat().build()));
    public static final RegistryObject<Item> BONE_STEW = ITEMS.register("bone_stew", () -> new CrockPotFoodBlockItem(CrockPotBlocks.BONE_STEW.get(), CrockPotFoodProperties.builder(20, 0.4F).duration(FoodUseDuration.SUPER_SLOW).effect(MobEffects.HEAL, 1, 1).meat().build()));
    public static final RegistryObject<Item> BREAKFAST_SKILLET = ITEMS.register("breakfast_skillet", () -> new CrockPotFoodBlockItem(CrockPotBlocks.BREAKFAST_SKILLET.get(), CrockPotFoodProperties.builder(8, 0.8F).meat().build()));
    public static final RegistryObject<Item> BUNNY_STEW = ITEMS.register("bunny_stew", () -> new CrockPotFoodBlockItem(CrockPotBlocks.BUNNY_STEW.get(), CrockPotFoodProperties.builder(6, 0.8F).effect(MobEffects.REGENERATION, 5 * 20).effect(CrockPotEffects.WELL_FED, 2 * 60 * 20).meat().build()));
    public static final RegistryObject<Item> CALIFORNIA_ROLL = ITEMS.register("california_roll", () -> new CrockPotFoodBlockItem(CrockPotBlocks.CALIFORNIA_ROLL.get(), CrockPotFoodProperties.builder(10, 0.6F).heal(4.0F).effect(MobEffects.ABSORPTION, 60 * 20).meat().build()));
    public static final RegistryObject<Item> CANDY = ITEMS.register("candy", CandyItem::new);
    public static final RegistryObject<Item> CEVICHE = ITEMS.register("ceviche", () -> new CrockPotFoodBlockItem(CrockPotBlocks.CEVICHE.get(), CrockPotFoodProperties.builder(7, 0.7F).alwaysEat().effect(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 1).effect(MobEffects.ABSORPTION, 20 * 20, 1).meat().build()));
    public static final RegistryObject<Item> FISH_STICKS = ITEMS.register("fish_sticks", () -> new CrockPotFoodBlockItem(CrockPotBlocks.FISH_STICKS.get(), CrockPotFoodProperties.builder(7, 0.7F).effect(MobEffects.REGENERATION, 30 * 20).meat().build()));
    public static final RegistryObject<Item> FISH_TACOS = ITEMS.register("fish_tacos", () -> new CrockPotFoodBlockItem(CrockPotBlocks.FISH_TACOS.get(), CrockPotFoodProperties.builder(8, 0.9F).heal(2.0F).meat().build()));
    public static final RegistryObject<Item> FLOWER_SALAD = ITEMS.register("flower_salad", FlowerSaladItem::new);
    public static final RegistryObject<Item> FROGGLE_BUNWICH = ITEMS.register("froggle_bunwich", () -> new CrockPotFoodBlockItem(CrockPotBlocks.FROGGLE_BUNWICH.get(), CrockPotFoodProperties.builder(7, 0.8F).meat().build()));
    public static final RegistryObject<Item> FRUIT_MEDLEY = ITEMS.register("fruit_medley", () -> new CrockPotFoodBlockItem(CrockPotBlocks.FRUIT_MEDLEY.get(), CrockPotFoodProperties.builder(8, 0.4F).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).build()));
    public static final RegistryObject<Item> GAZPACHO = ITEMS.register("gazpacho", () -> new CrockPotFoodBlockItem(CrockPotBlocks.GAZPACHO.get(), CrockPotFoodProperties.builder(6, 0.4F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.FIRE_RESISTANCE, 10 * 60 * 20).build()));
    public static final RegistryObject<Item> GLOW_BERRY_MOUSSE = ITEMS.register("glow_berry_mousse", () -> new CrockPotFoodBlockItem(CrockPotBlocks.GLOW_BERRY_MOUSSE.get(), CrockPotFoodProperties.builder(6, 0.6F).duration(FoodUseDuration.FAST).effect(MobEffects.GLOWING, 10 * 20).build()));
    public static final RegistryObject<Item> HONEY_HAM = ITEMS.register("honey_ham", () -> new CrockPotFoodBlockItem(CrockPotBlocks.HONEY_HAM.get(), CrockPotFoodProperties.builder(12, 0.8F).effect(MobEffects.REGENERATION, 20 * 20).effect(MobEffects.ABSORPTION, 60 * 20, 1).heal(6.0F).meat().build()));
    public static final RegistryObject<Item> HONEY_NUGGETS = ITEMS.register("honey_nuggets", () -> new CrockPotFoodBlockItem(CrockPotBlocks.HONEY_NUGGETS.get(), CrockPotFoodProperties.builder(8, 0.3F).effect(MobEffects.REGENERATION, 10 * 20).effect(MobEffects.ABSORPTION, 60 * 20).heal(4.0F).meat().build()));
    public static final RegistryObject<Item> HOT_CHILI = ITEMS.register("hot_chili", () -> new CrockPotFoodBlockItem(CrockPotBlocks.HOT_CHILI.get(), CrockPotFoodProperties.builder(9, 0.8F).effect(MobEffects.DAMAGE_BOOST, (60 + 30) * 20).effect(MobEffects.DIG_SPEED, (60 + 30) * 20).meat().build()));
    public static final RegistryObject<Item> HOT_COCOA = ITEMS.register("hot_cocoa", () -> new CrockPotFoodBlockItem(CrockPotBlocks.HOT_COCOA.get(), CrockPotFoodProperties.builder(2, 0.1F).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 8 * 60 * 20, 1).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).removeEffect(MobEffects.DIG_SLOWDOWN).build()));
    public static final RegistryObject<Item> ICE_CREAM = ITEMS.register("ice_cream", IceCreamItem::new);
    public static final RegistryObject<Item> ICED_TEA = ITEMS.register("iced_tea", () -> new CrockPotFoodBlockItem(CrockPotBlocks.ICED_TEA.get(), CrockPotFoodProperties.builder(3, 0.1F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.JUMP, 5 * 60 * 20, 1).build()));
    public static final RegistryObject<Item> JAMMY_PRESERVES = ITEMS.register("jammy_preserves", () -> new CrockPotFoodBlockItem(CrockPotBlocks.JAMMY_PRESERVES.get(), CrockPotFoodProperties.builder(6, 0.3F).duration(FoodUseDuration.FAST).build()));
    public static final RegistryObject<Item> KABOBS = ITEMS.register("kabobs", () -> new CrockPotFoodBlockItem(CrockPotBlocks.KABOBS.get(), CrockPotFoodProperties.builder(7, 0.7F).meat().build()));
    public static final RegistryObject<Item> MASHED_POTATOES = ITEMS.register("mashed_potatoes", () -> new CrockPotFoodBlockItem(CrockPotBlocks.MASHED_POTATOES.get(), CrockPotFoodProperties.builder(9, 0.6F).effect(MobEffects.DAMAGE_RESISTANCE, 4 * 60 * 20).build()));
    public static final RegistryObject<Item> MEAT_BALLS = ITEMS.register("meat_balls", () -> new CrockPotFoodBlockItem(CrockPotBlocks.MEAT_BALLS.get(), CrockPotFoodProperties.builder(9, 0.5F).meat().build()));
    public static final RegistryObject<Item> MONSTER_LASAGNA = ITEMS.register("monster_lasagna", () -> new CrockPotFoodBlockItem(CrockPotBlocks.MONSTER_LASAGNA.get(), CrockPotFoodProperties.builder(7, 0.2F).effect(MobEffects.HUNGER, 15 * 20).effect(MobEffects.POISON, 2 * 20).damage(CrockPotDamageTypes.MONSTER_FOOD, 6.0F).meat().build()));
    public static final RegistryObject<Item> MONSTER_TARTARE = ITEMS.register("monster_tartare", () -> new CrockPotFoodBlockItem(CrockPotBlocks.MONSTER_TARTARE.get(), CrockPotFoodProperties.builder(8, 0.7F).effect(MobEffects.DAMAGE_BOOST, 2 * 60 * 20, 1).meat().build()));
    public static final RegistryObject<Item> MOQUECA = ITEMS.register("moqueca", () -> new CrockPotFoodBlockItem(CrockPotBlocks.MOQUECA.get(), CrockPotFoodProperties.builder(14, 0.7F).duration(FoodUseDuration.SLOW).effect(MobEffects.HEALTH_BOOST, (60 + 30) * 20, 2).heal(6.0F).meat().build()));
    public static final RegistryObject<Item> MUSHY_CAKE = ITEMS.register("mushy_cake", () -> new CrockPotFoodBlockItem(CrockPotBlocks.MUSHY_CAKE.get(), CrockPotFoodProperties.builder(6, 0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(CrockPotEffects.WITHER_RESISTANCE, 60 * 20).build()));
    public static final RegistryObject<Item> NETHEROSIA = ITEMS.register("netherosia", NetherosiaItem::new);
    public static final RegistryObject<Item> PEPPER_POPPER = ITEMS.register("pepper_popper", () -> new CrockPotFoodBlockItem(CrockPotBlocks.PEPPER_POPPER.get(), CrockPotFoodProperties.builder(8, 0.8F).effect(MobEffects.DAMAGE_BOOST, 60 * 20, 1).meat().build()));
    public static final RegistryObject<Item> PEROGIES = ITEMS.register("perogies", () -> new CrockPotFoodBlockItem(CrockPotBlocks.PEROGIES.get(), CrockPotFoodProperties.builder(8, 0.8F).heal(6.0F).meat().build()));
    public static final RegistryObject<Item> PLAIN_OMELETTE = ITEMS.register("plain_omelette", () -> new CrockPotFoodBlockItem(CrockPotBlocks.PLAIN_OMELETTE.get(), CrockPotFoodProperties.builder(7, 0.6F).meat().build()));
    public static final RegistryObject<Item> POTATO_SOUFFLE = ITEMS.register("potato_souffle", () -> new CrockPotFoodBlockItem(CrockPotBlocks.POTATO_SOUFFLE.get(), CrockPotFoodProperties.builder(8, 0.7F).effect(MobEffects.DAMAGE_RESISTANCE, (60 + 30) * 20, 1).build()));
    public static final RegistryObject<Item> POTATO_TORNADO = ITEMS.register("potato_tornado", () -> new CrockPotFoodBlockItem(CrockPotBlocks.POTATO_TORNADO.get(), CrockPotFoodProperties.builder(8, 0.6F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build()));
    public static final RegistryObject<Item> POW_CAKE = ITEMS.register("pow_cake", () -> new CrockPotFoodBlockItem(CrockPotBlocks.POW_CAKE.get(), CrockPotFoodProperties.builder(2, 0.1F).alwaysEat().damage(CrockPotDamageTypes.POW_CAKE, 1.0F).build()));
    public static final RegistryObject<Item> PUMPKIN_COOKIE = ITEMS.register("pumpkin_cookie", () -> new CrockPotFoodBlockItem(CrockPotBlocks.PUMPKIN_COOKIE.get(), CrockPotFoodProperties.builder(10, 0.7F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build()));
    public static final RegistryObject<Item> RATATOUILLE = ITEMS.register("ratatouille", () -> new CrockPotFoodBlockItem(CrockPotBlocks.RATATOUILLE.get(), CrockPotFoodProperties.builder(6, 0.4F).duration(FoodUseDuration.FAST).build()));
    public static final RegistryObject<Item> SALMON_SUSHI = ITEMS.register("salmon_sushi", () -> new CrockPotFoodBlockItem(CrockPotBlocks.SALMON_SUSHI.get(), CrockPotFoodProperties.builder(5, 0.8F).duration(FoodUseDuration.FAST).heal(1.0F).meat().build()));
    public static final RegistryObject<Item> SALSA = ITEMS.register("salsa", () -> new CrockPotFoodBlockItem(CrockPotBlocks.SALSA.get(), CrockPotFoodProperties.builder(7, 0.8F).duration(FoodUseDuration.FAST).effect(MobEffects.DIG_SPEED, 6 * 60 * 20).build()));
    public static final RegistryObject<Item> SCOTCH_EGG = ITEMS.register("scotch_egg", () -> new CrockPotFoodBlockItem(CrockPotBlocks.SCOTCH_EGG.get(), CrockPotFoodProperties.builder(8, 1.0F).meat().build()));
    public static final RegistryObject<Item> SEAFOOD_GUMBO = ITEMS.register("seafood_gumbo", () -> new CrockPotFoodBlockItem(CrockPotBlocks.SEAFOOD_GUMBO.get(), CrockPotFoodProperties.builder(9, 0.7F).effect(MobEffects.REGENERATION, 2 * 60 * 20).meat().build()));
    public static final RegistryObject<Item> STUFFED_EGGPLANT = ITEMS.register("stuffed_eggplant", () -> new CrockPotFoodBlockItem(CrockPotBlocks.STUFFED_EGGPLANT.get(), CrockPotFoodProperties.builder(7, 0.6F).duration(FoodUseDuration.FAST).heal(2.0F).build()));
    public static final RegistryObject<Item> SURF_N_TURF = ITEMS.register("surf_n_turf", () -> new CrockPotFoodBlockItem(CrockPotBlocks.SURF_N_TURF.get(), CrockPotFoodProperties.builder(8, 1.2F).alwaysEat().effect(MobEffects.REGENERATION, 30 * 20, 1).heal(8.0F).meat().build()));
    public static final RegistryObject<Item> TAFFY = ITEMS.register("taffy", () -> new CrockPotFoodBlockItem(CrockPotBlocks.TAFFY.get(), CrockPotFoodProperties.builder(5, 0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(MobEffects.LUCK, 8 * 60 * 20).damage(CrockPotDamageTypes.TAFFY, 1.0F).removeEffect(MobEffects.POISON).build()));
    public static final RegistryObject<Item> TEA = ITEMS.register("tea", () -> new CrockPotFoodBlockItem(CrockPotBlocks.TEA.get(), CrockPotFoodProperties.builder(3, 0.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.DIG_SPEED, 5 * 60 * 20, 1).build()));
    public static final RegistryObject<Item> TROPICAL_BOUILLABAISSE = ITEMS.register("tropical_bouillabaisse", () -> new CrockPotFoodBlockItem(CrockPotBlocks.TROPICAL_BOUILLABAISSE.get(), CrockPotFoodProperties.builder(7, 0.6F).alwaysEat().effect(CrockPotEffects.OCEAN_AFFINITY, (2 * 60 + 30) * 20).meat().build()));
    public static final RegistryObject<Item> TURKEY_DINNER = ITEMS.register("turkey_dinner", () -> new CrockPotFoodBlockItem(CrockPotBlocks.TURKEY_DINNER.get(), CrockPotFoodProperties.builder(12, 0.8F).effect(MobEffects.HEALTH_BOOST, 3 * 60 * 20).meat().build()));
    public static final RegistryObject<Item> VEG_STINGER = ITEMS.register("veg_stinger", () -> new CrockPotFoodBlockItem(CrockPotBlocks.VEG_STINGER.get(), CrockPotFoodProperties.builder(6, 0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.NIGHT_VISION, 10 * 60 * 20).build()));
    public static final RegistryObject<Item> VOLT_GOAT_JELLY = ITEMS.register("volt_goat_jelly", () -> new CrockPotFoodBlockItem(CrockPotBlocks.VOLT_GOAT_JELLY.get(), CrockPotFoodProperties.builder(4, 0.6F).alwaysEat().effect(CrockPotEffects.CHARGE.get(), 6 * 60 * 20).meat().build()));
    public static final RegistryObject<Item> WATERMELON_ICLE = ITEMS.register("watermelon_icle", () -> new CrockPotFoodBlockItem(CrockPotBlocks.WATERMELON_ICLE.get(), CrockPotFoodProperties.builder(5, 0.4F).duration(FoodUseDuration.FAST).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).effect(MobEffects.JUMP, 3 * 60 * 20).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).build()));
    public static final RegistryObject<Item> WET_GOOP = ITEMS.register("wet_goop", () -> new CrockPotFoodBlockItem(CrockPotBlocks.WET_GOOP.get(), CrockPotFoodProperties.builder().duration(FoodUseDuration.SUPER_SLOW).alwaysEat().effect(MobEffects.CONFUSION, 10 * 20).tooltip("wet_goop", ChatFormatting.DARK_AQUA).build()));

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
            TEA.get(), TROPICAL_BOUILLABAISSE.get(), TURKEY_DINNER.get(), VEG_STINGER.get(), VOLT_GOAT_JELLY.get(),
            WATERMELON_ICLE.get()
    ));

    public static final RegistryObject<Item> VOLT_GOAT_SPAWN_EGG = ITEMS.register("volt_goat_spawn_egg", () -> new ForgeSpawnEggItem(CrockPotEntities.VOLT_GOAT, 0x546596, 0xD1CB4F, new Item.Properties()));

    public static final Map<FoodCategory, RegistryObject<Item>> FOOD_CATEGORY_ITEMS = Util.make(new EnumMap<>(FoodCategory.class), map -> {
        for (FoodCategory category : FoodCategory.values()) {
            map.put(category, ITEMS.register("food_category_" + category.name().toLowerCase(), CrockPotBaseItem::new));
        }
    });
}
