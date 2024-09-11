package com.sihenzhang.crockpot.item;

import com.google.common.base.Suppliers;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.ModDamageTypes;
import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.effect.ModEffects;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.item.food.*;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.animal.Parrot;
import net.minecraft.world.item.*;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

public final class ModItems {
    private ModItems() {
    }

    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CrockPot.MOD_ID);

    public static final DeferredItem<BlockItem> CROCK_POT = ITEMS.register("crock_pot", () -> new CrockPotBlockItem(ModBlocks.CROCK_POT.get()));
    public static final DeferredItem<BlockItem> PORTABLE_CROCK_POT = ITEMS.register("portable_crock_pot", () -> new CrockPotBlockItem(ModBlocks.PORTABLE_CROCK_POT.get()));

    public static final DeferredItem<BlockItem> UNKNOWN_SEEDS = ITEMS.registerItem("unknown_seeds", (properties) -> new ItemNameBlockItem(ModBlocks.UNKNOWN_CROPS.get(), properties));
    public static final DeferredItem<BlockItem> ASPARAGUS_SEEDS = ITEMS.registerItem("asparagus_seeds", (properties) -> new ItemNameBlockItem(ModBlocks.ASPARAGUS.get(), properties));
    public static final DeferredItem<Item> ASPARAGUS = ITEMS.register("asparagus", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final DeferredItem<BlockItem> CORN_SEEDS = ITEMS.registerItem("corn_seeds", (properties) -> new ItemNameBlockItem(ModBlocks.CORN.get(), properties));
    public static final DeferredItem<Item> CORN = ITEMS.register("corn", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final DeferredItem<Item> POPCORN = ITEMS.register("popcorn", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.8F).duration(FoodUseDuration.FAST).hideEffects().build()));
    public static final DeferredItem<BlockItem> EGGPLANT_SEEDS = ITEMS.registerItem("eggplant_seeds", (properties) -> new ItemNameBlockItem(ModBlocks.EGGPLANT.get(), properties));
    public static final DeferredItem<Item> EGGPLANT = ITEMS.register("eggplant", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final DeferredItem<Item> COOKED_EGGPLANT = ITEMS.register("cooked_eggplant", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(5, 0.6F).hideEffects().build()));
    public static final DeferredItem<BlockItem> GARLIC_SEEDS = ITEMS.registerItem("garlic_seeds", (properties) -> new ItemNameBlockItem(ModBlocks.GARLIC.get(), properties));
    public static final DeferredItem<Item> GARLIC = ITEMS.register("garlic", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final DeferredItem<BlockItem> ONION_SEEDS = ITEMS.registerItem("onion_seeds", (properties) -> new ItemNameBlockItem(ModBlocks.ONION.get(), properties));
    public static final DeferredItem<Item> ONION = ITEMS.register("onion", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final DeferredItem<BlockItem> PEPPER_SEEDS = ITEMS.registerItem("pepper_seeds", (properties) -> new ItemNameBlockItem(ModBlocks.PEPPER.get(), properties));
    public static final DeferredItem<Item> PEPPER = ITEMS.register("pepper", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).damage(ModDamageTypes.SPICY, 1).hideEffects().build()));
    public static final DeferredItem<BlockItem> TOMATO_SEEDS = ITEMS.registerItem("tomato_seeds", (properties) -> new ItemNameBlockItem(ModBlocks.TOMATO.get(), properties));
    public static final DeferredItem<Item> TOMATO = ITEMS.register("tomato", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));

    public static final Supplier<Set<Item>> SEEDS = Suppliers.memoize(() -> Set.of(UNKNOWN_SEEDS.get(), ASPARAGUS_SEEDS.get(), CORN_SEEDS.get(), EGGPLANT_SEEDS.get(), GARLIC_SEEDS.get(), ONION_SEEDS.get(), PEPPER_SEEDS.get(), TOMATO_SEEDS.get()));
    public static final Supplier<Set<Item>> CROPS = Suppliers.memoize(() -> Set.of(ASPARAGUS.get(), CORN.get(), EGGPLANT.get(), GARLIC.get(), ONION.get(), PEPPER.get(), TOMATO.get()));
    public static final Supplier<Set<Item>> COOKED_CROPS = Suppliers.memoize(() -> Set.of(POPCORN.get(), COOKED_EGGPLANT.get()));

    public static final DeferredItem<BlockItem> BIRDCAGE = ITEMS.registerSimpleBlockItem("birdcage", ModBlocks.BIRDCAGE);
    public static final Map<Parrot.Variant, DeferredItem<Item>> PARROT_EGGS = Util.make(new EnumMap<>(Parrot.Variant.class), map -> {
        for (var variant : Parrot.Variant.values()) {
            map.put(variant, ITEMS.register("parrot_egg_" + variant.getSerializedName(), () -> new ParrotEggItem(variant)));
        }
    });

    public static final DeferredItem<Item> CROCK_POT_UPGRADE_SMITHING_TEMPLATE = ITEMS.register("pot_upgrade_smithing_template", () -> new SmithingTemplateItem(I18nUtils.createTooltipComponent("smithing_template.pot_upgrade.applies_to").withStyle(ChatFormatting.BLUE), I18nUtils.createTooltipComponent("smithing_template.pot_upgrade.ingredients").withStyle(ChatFormatting.BLUE), I18nUtils.createComponent("upgrade", "pot_upgrade").withStyle(ChatFormatting.GRAY), I18nUtils.createTooltipComponent("smithing_template.pot_upgrade.base_slot_description"), I18nUtils.createTooltipComponent("smithing_template.pot_upgrade.additions_slot_description"), List.of(RLUtils.mod("item/empty_slot_pot")), List.of(RLUtils.mod("item/empty_slot_block"))));
    public static final DeferredItem<Item> BLACKSTONE_DUST = ITEMS.registerSimpleItem("blackstone_dust");
    public static final DeferredItem<Item> COLLECTED_DUST = ITEMS.registerItem("collected_dust", CollectedDustItem::new);
    public static final DeferredItem<Item> COOKED_EGG = ITEMS.register("cooked_egg", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.6F).hideEffects().build()));
    public static final DeferredItem<Item> FROG_LEGS = ITEMS.register("frog_legs", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(2, 0.4F).hideEffects().build()));
    public static final DeferredItem<Item> COOKED_FROG_LEGS = ITEMS.register("cooked_frog_legs", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(5, 0.7F).hideEffects().build()));
    public static final DeferredItem<Item> HOGLIN_NOSE = ITEMS.register("hoglin_nose", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(3, 0.2F).hideEffects().build()));
    public static final DeferredItem<Item> COOKED_HOGLIN_NOSE = ITEMS.register("cooked_hoglin_nose", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(8, 0.7F).hideEffects().build()));
    public static final DeferredItem<Item> MILK_BOTTLE = ITEMS.register("milk_bottle", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder().alwaysEat().drink().craftRemainder(Items.GLASS_BOTTLE).tooltip("milk_bottle", ChatFormatting.DARK_AQUA).hideEffects().build()));
    public static final DeferredItem<Item> SYRUP = ITEMS.register("syrup", () -> new CrockPotFoodItem(CrockPotFoodProperties.builder(1, 0.3F).drink().sound(SoundEvents.HONEY_DRINK).hideEffects().build()));
    public static final DeferredItem<Item> VOLT_GOAT_HORN = ITEMS.registerSimpleItem("volt_goat_horn");
    public static final DeferredItem<Item> MILKMADE_HAT = ITEMS.register("milkmade_hat", () -> new MilkmadeHatItem());
    public static final DeferredItem<Item> CREATIVE_MILKMADE_HAT = ITEMS.register("creative_milkmade_hat", CreativeMilkmadeHatItem::new);
    public static final DeferredItem<Item> GNAWS_COIN = ITEMS.register("gnaws_coin", GnawsCoinItem::new);

    public static final DeferredItem<Item> ASPARAGUS_SOUP = ITEMS.register("asparagus_soup", () -> new CrockPotFoodBlockItem(ModBlocks.ASPARAGUS_SOUP.get(), CrockPotFoodProperties.builder(4, 0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().removeEffect(MobEffects.WEAKNESS).removeEffect(MobEffects.DIG_SLOWDOWN).removeEffect(MobEffects.BLINDNESS).removeEffect(MobEffects.BAD_OMEN).build()));
    public static final DeferredItem<Item> AVAJ = ITEMS.register("avaj", () -> new CrockPotFoodBlockItem(ModBlocks.AVAJ.get(), CrockPotFoodProperties.builder(2, 3.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, (32 * 60 + 20) * 20, 2).rarity(Rarity.EPIC).build()));
    public static final DeferredItem<Item> BACON_EGGS = ITEMS.register("bacon_eggs", () -> new CrockPotFoodBlockItem(ModBlocks.BACON_EGGS.get(), CrockPotFoodProperties.builder(12, 0.8F).heal(4.0F).build()));
    public static final DeferredItem<Item> BONE_SOUP = ITEMS.register("bone_soup", () -> new CrockPotFoodBlockItem(ModBlocks.BONE_SOUP.get(), CrockPotFoodProperties.builder(10, 0.6F).effect(MobEffects.ABSORPTION, 2 * 60 * 20, 1).build()));
    public static final DeferredItem<Item> BONE_STEW = ITEMS.register("bone_stew", () -> new CrockPotFoodBlockItem(ModBlocks.BONE_STEW.get(), CrockPotFoodProperties.builder(20, 0.4F).duration(FoodUseDuration.SUPER_SLOW).effect(MobEffects.HEAL, 1, 1).build()));
    public static final DeferredItem<Item> BREAKFAST_SKILLET = ITEMS.register("breakfast_skillet", () -> new CrockPotFoodBlockItem(ModBlocks.BREAKFAST_SKILLET.get(), CrockPotFoodProperties.builder(8, 0.8F).build()));
    public static final DeferredItem<Item> BUNNY_STEW = ITEMS.register("bunny_stew", () -> new CrockPotFoodBlockItem(ModBlocks.BUNNY_STEW.get(), CrockPotFoodProperties.builder(6, 0.8F).effect(MobEffects.REGENERATION, 5 * 20).effect(ModEffects.WELL_FED, 2 * 60 * 20).build()));
    public static final DeferredItem<Item> CALIFORNIA_ROLL = ITEMS.register("california_roll", () -> new CrockPotFoodBlockItem(ModBlocks.CALIFORNIA_ROLL.get(), CrockPotFoodProperties.builder(10, 0.6F).heal(4.0F).effect(MobEffects.ABSORPTION, 60 * 20).build()));
    public static final DeferredItem<Item> CANDY = ITEMS.register("candy", CandyItem::new);
    public static final DeferredItem<Item> CEVICHE = ITEMS.register("ceviche", () -> new CrockPotFoodBlockItem(ModBlocks.CEVICHE.get(), CrockPotFoodProperties.builder(7, 0.7F).alwaysEat().effect(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 1).effect(MobEffects.ABSORPTION, 20 * 20, 1).build()));
    public static final DeferredItem<Item> FISH_STICKS = ITEMS.register("fish_sticks", () -> new CrockPotFoodBlockItem(ModBlocks.FISH_STICKS.get(), CrockPotFoodProperties.builder(7, 0.7F).effect(MobEffects.REGENERATION, 30 * 20).build()));
    public static final DeferredItem<Item> FISH_TACOS = ITEMS.register("fish_tacos", () -> new CrockPotFoodBlockItem(ModBlocks.FISH_TACOS.get(), CrockPotFoodProperties.builder(8, 0.9F).heal(2.0F).build()));
    public static final DeferredItem<Item> FLOWER_SALAD = ITEMS.register("flower_salad", FlowerSaladItem::new);
    public static final DeferredItem<Item> FROGGLE_BUNWICH = ITEMS.register("froggle_bunwich", () -> new CrockPotFoodBlockItem(ModBlocks.FROGGLE_BUNWICH.get(), CrockPotFoodProperties.builder(7, 0.8F).build()));
    public static final DeferredItem<Item> FRUIT_MEDLEY = ITEMS.register("fruit_medley", () -> new CrockPotFoodBlockItem(ModBlocks.FRUIT_MEDLEY.get(), CrockPotFoodProperties.builder(8, 0.4F).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).build()));
    public static final DeferredItem<Item> GAZPACHO = ITEMS.register("gazpacho", () -> new CrockPotFoodBlockItem(ModBlocks.GAZPACHO.get(), CrockPotFoodProperties.builder(6, 0.4F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.FIRE_RESISTANCE, 10 * 60 * 20).build()));
    public static final DeferredItem<Item> GLOW_BERRY_MOUSSE = ITEMS.register("glow_berry_mousse", () -> new CrockPotFoodBlockItem(ModBlocks.GLOW_BERRY_MOUSSE.get(), CrockPotFoodProperties.builder(6, 0.6F).duration(FoodUseDuration.FAST).effect(MobEffects.GLOWING, 10 * 20).build()));
    public static final DeferredItem<Item> HONEY_HAM = ITEMS.register("honey_ham", () -> new CrockPotFoodBlockItem(ModBlocks.HONEY_HAM.get(), CrockPotFoodProperties.builder(12, 0.8F).effect(MobEffects.REGENERATION, 20 * 20).effect(MobEffects.ABSORPTION, 60 * 20, 1).heal(6.0F).build()));
    public static final DeferredItem<Item> HONEY_NUGGETS = ITEMS.register("honey_nuggets", () -> new CrockPotFoodBlockItem(ModBlocks.HONEY_NUGGETS.get(), CrockPotFoodProperties.builder(8, 0.3F).effect(MobEffects.REGENERATION, 10 * 20).effect(MobEffects.ABSORPTION, 60 * 20).heal(4.0F).build()));
    public static final DeferredItem<Item> HOT_CHILI = ITEMS.register("hot_chili", () -> new CrockPotFoodBlockItem(ModBlocks.HOT_CHILI.get(), CrockPotFoodProperties.builder(9, 0.8F).effect(MobEffects.DAMAGE_BOOST, (60 + 30) * 20).effect(MobEffects.DIG_SPEED, (60 + 30) * 20).build()));
    public static final DeferredItem<Item> HOT_COCOA = ITEMS.register("hot_cocoa", () -> new CrockPotFoodBlockItem(ModBlocks.HOT_COCOA.get(), CrockPotFoodProperties.builder(2, 0.1F).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 8 * 60 * 20, 1).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).removeEffect(MobEffects.DIG_SLOWDOWN).build()));
    public static final DeferredItem<Item> ICE_CREAM = ITEMS.register("ice_cream", IceCreamItem::new);
    public static final DeferredItem<Item> ICED_TEA = ITEMS.register("iced_tea", () -> new CrockPotFoodBlockItem(ModBlocks.ICED_TEA.get(), CrockPotFoodProperties.builder(3, 0.1F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.JUMP, 5 * 60 * 20, 1).build()));
    public static final DeferredItem<Item> JAMMY_PRESERVES = ITEMS.register("jammy_preserves", () -> new CrockPotFoodBlockItem(ModBlocks.JAMMY_PRESERVES.get(), CrockPotFoodProperties.builder(6, 0.3F).duration(FoodUseDuration.FAST).build()));
    public static final DeferredItem<Item> KABOBS = ITEMS.register("kabobs", () -> new CrockPotFoodBlockItem(ModBlocks.KABOBS.get(), CrockPotFoodProperties.builder(7, 0.7F).build()));
    public static final DeferredItem<Item> MASHED_POTATOES = ITEMS.register("mashed_potatoes", () -> new CrockPotFoodBlockItem(ModBlocks.MASHED_POTATOES.get(), CrockPotFoodProperties.builder(9, 0.6F).effect(MobEffects.DAMAGE_RESISTANCE, 4 * 60 * 20).build()));
    public static final DeferredItem<Item> MEAT_BALLS = ITEMS.register("meat_balls", () -> new CrockPotFoodBlockItem(ModBlocks.MEAT_BALLS.get(), CrockPotFoodProperties.builder(9, 0.5F).build()));
    public static final DeferredItem<Item> MONSTER_LASAGNA = ITEMS.register("monster_lasagna", () -> new CrockPotFoodBlockItem(ModBlocks.MONSTER_LASAGNA.get(), CrockPotFoodProperties.builder(7, 0.2F).effect(MobEffects.HUNGER, 15 * 20).effect(MobEffects.POISON, 2 * 20).damage(ModDamageTypes.MONSTER_FOOD, 6.0F).build()));
    public static final DeferredItem<Item> MONSTER_TARTARE = ITEMS.register("monster_tartare", () -> new CrockPotFoodBlockItem(ModBlocks.MONSTER_TARTARE.get(), CrockPotFoodProperties.builder(8, 0.7F).effect(MobEffects.DAMAGE_BOOST, 2 * 60 * 20, 1).build()));
    public static final DeferredItem<Item> MOQUECA = ITEMS.register("moqueca", () -> new CrockPotFoodBlockItem(ModBlocks.MOQUECA.get(), CrockPotFoodProperties.builder(14, 0.7F).duration(FoodUseDuration.SLOW).effect(MobEffects.HEALTH_BOOST, (60 + 30) * 20, 2).heal(6.0F).build()));
    public static final DeferredItem<Item> MUSHY_CAKE = ITEMS.register("mushy_cake", () -> new CrockPotFoodBlockItem(ModBlocks.MUSHY_CAKE.get(), CrockPotFoodProperties.builder(6, 0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(ModEffects.WITHER_RESISTANCE, 60 * 20).build()));
    public static final DeferredItem<Item> NETHEROSIA = ITEMS.registerItem("netherosia", NetherosiaItem::new);
    public static final DeferredItem<Item> PEPPER_POPPER = ITEMS.register("pepper_popper", () -> new CrockPotFoodBlockItem(ModBlocks.PEPPER_POPPER.get(), CrockPotFoodProperties.builder(8, 0.8F).effect(MobEffects.DAMAGE_BOOST, 60 * 20, 1).build()));
    public static final DeferredItem<Item> PEROGIES = ITEMS.register("perogies", () -> new CrockPotFoodBlockItem(ModBlocks.PEROGIES.get(), CrockPotFoodProperties.builder(8, 0.8F).heal(6.0F).build()));
    public static final DeferredItem<Item> PLAIN_OMELETTE = ITEMS.register("plain_omelette", () -> new CrockPotFoodBlockItem(ModBlocks.PLAIN_OMELETTE.get(), CrockPotFoodProperties.builder(7, 0.6F).build()));
    public static final DeferredItem<Item> POTATO_SOUFFLE = ITEMS.register("potato_souffle", () -> new CrockPotFoodBlockItem(ModBlocks.POTATO_SOUFFLE.get(), CrockPotFoodProperties.builder(8, 0.7F).effect(MobEffects.DAMAGE_RESISTANCE, (60 + 30) * 20, 1).build()));
    public static final DeferredItem<Item> POTATO_TORNADO = ITEMS.register("potato_tornado", () -> new CrockPotFoodBlockItem(ModBlocks.POTATO_TORNADO.get(), CrockPotFoodProperties.builder(8, 0.6F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build()));
    public static final DeferredItem<Item> POW_CAKE = ITEMS.register("pow_cake", () -> new CrockPotFoodBlockItem(ModBlocks.POW_CAKE.get(), CrockPotFoodProperties.builder(2, 0.1F).alwaysEat().damage(ModDamageTypes.POW_CAKE, 1.0F).build()));
    public static final DeferredItem<Item> PUMPKIN_COOKIE = ITEMS.register("pumpkin_cookie", () -> new CrockPotFoodBlockItem(ModBlocks.PUMPKIN_COOKIE.get(), CrockPotFoodProperties.builder(10, 0.7F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build()));
    public static final DeferredItem<Item> RATATOUILLE = ITEMS.register("ratatouille", () -> new CrockPotFoodBlockItem(ModBlocks.RATATOUILLE.get(), CrockPotFoodProperties.builder(6, 0.4F).duration(FoodUseDuration.FAST).build()));
    public static final DeferredItem<Item> SALMON_SUSHI = ITEMS.register("salmon_sushi", () -> new CrockPotFoodBlockItem(ModBlocks.SALMON_SUSHI.get(), CrockPotFoodProperties.builder(5, 0.8F).duration(FoodUseDuration.FAST).heal(1.0F).build()));
    public static final DeferredItem<Item> SALSA = ITEMS.register("salsa", () -> new CrockPotFoodBlockItem(ModBlocks.SALSA.get(), CrockPotFoodProperties.builder(7, 0.8F).duration(FoodUseDuration.FAST).effect(MobEffects.DIG_SPEED, 6 * 60 * 20).build()));
    public static final DeferredItem<Item> SCOTCH_EGG = ITEMS.register("scotch_egg", () -> new CrockPotFoodBlockItem(ModBlocks.SCOTCH_EGG.get(), CrockPotFoodProperties.builder(8, 1.0F).build()));
    public static final DeferredItem<Item> SEAFOOD_GUMBO = ITEMS.register("seafood_gumbo", () -> new CrockPotFoodBlockItem(ModBlocks.SEAFOOD_GUMBO.get(), CrockPotFoodProperties.builder(9, 0.7F).effect(MobEffects.REGENERATION, 2 * 60 * 20).build()));
    public static final DeferredItem<Item> STUFFED_EGGPLANT = ITEMS.register("stuffed_eggplant", () -> new CrockPotFoodBlockItem(ModBlocks.STUFFED_EGGPLANT.get(), CrockPotFoodProperties.builder(7, 0.6F).duration(FoodUseDuration.FAST).heal(2.0F).build()));
    public static final DeferredItem<Item> SURF_N_TURF = ITEMS.register("surf_n_turf", () -> new CrockPotFoodBlockItem(ModBlocks.SURF_N_TURF.get(), CrockPotFoodProperties.builder(8, 1.2F).alwaysEat().effect(MobEffects.REGENERATION, 30 * 20, 1).heal(8.0F).build()));
    public static final DeferredItem<Item> TAFFY = ITEMS.register("taffy", () -> new CrockPotFoodBlockItem(ModBlocks.TAFFY.get(), CrockPotFoodProperties.builder(5, 0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(MobEffects.LUCK, 8 * 60 * 20).damage(ModDamageTypes.TAFFY, 1.0F).removeEffect(MobEffects.POISON).build()));
    public static final DeferredItem<Item> TEA = ITEMS.register("tea", () -> new CrockPotFoodBlockItem(ModBlocks.TEA.get(), CrockPotFoodProperties.builder(3, 0.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.DIG_SPEED, 5 * 60 * 20, 1).build()));
    public static final DeferredItem<Item> TROPICAL_BOUILLABAISSE = ITEMS.register("tropical_bouillabaisse", () -> new CrockPotFoodBlockItem(ModBlocks.TROPICAL_BOUILLABAISSE.get(), CrockPotFoodProperties.builder(7, 0.6F).alwaysEat().effect(ModEffects.OCEAN_AFFINITY, (2 * 60 + 30) * 20).build()));
    public static final DeferredItem<Item> TURKEY_DINNER = ITEMS.register("turkey_dinner", () -> new CrockPotFoodBlockItem(ModBlocks.TURKEY_DINNER.get(), CrockPotFoodProperties.builder(12, 0.8F).effect(MobEffects.HEALTH_BOOST, 3 * 60 * 20).build()));
    public static final DeferredItem<Item> VEG_STINGER = ITEMS.register("veg_stinger", () -> new CrockPotFoodBlockItem(ModBlocks.VEG_STINGER.get(), CrockPotFoodProperties.builder(6, 0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.NIGHT_VISION, 10 * 60 * 20).build()));
    public static final DeferredItem<Item> VOLT_GOAT_JELLY = ITEMS.register("volt_goat_jelly", () -> new CrockPotFoodBlockItem(ModBlocks.VOLT_GOAT_JELLY.get(), CrockPotFoodProperties.builder(4, 0.6F).alwaysEat().effect(ModEffects.CHARGE, 6 * 60 * 20).build()));
    public static final DeferredItem<Item> WATERMELON_ICLE = ITEMS.register("watermelon_icle", () -> new CrockPotFoodBlockItem(ModBlocks.WATERMELON_ICLE.get(), CrockPotFoodProperties.builder(5, 0.4F).duration(FoodUseDuration.FAST).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).effect(MobEffects.JUMP, 3 * 60 * 20).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).build()));
    public static final DeferredItem<Item> WET_GOOP = ITEMS.register("wet_goop", () -> new CrockPotFoodBlockItem(ModBlocks.WET_GOOP.get(), CrockPotFoodProperties.builder().duration(FoodUseDuration.SUPER_SLOW).alwaysEat().effect(MobEffects.CONFUSION, 10 * 20).tooltip("wet_goop", ChatFormatting.DARK_AQUA).build()));

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

    public static final DeferredItem<Item> VOLT_GOAT_SPAWN_EGG = ITEMS.register("volt_goat_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.VOLT_GOAT, 0x546596, 0xD1CB4F, new Item.Properties()));

    public static final Map<FoodCategory, DeferredItem<Item>> FOOD_CATEGORY_ITEMS = Util.make(new EnumMap<>(FoodCategory.class), map -> {
        for (FoodCategory category : FoodCategory.values()) {
            map.put(category, ITEMS.registerSimpleItem("food_category_" + category.name().toLowerCase()));
        }
    });
}
