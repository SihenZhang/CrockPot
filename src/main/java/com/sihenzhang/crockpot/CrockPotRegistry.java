package com.sihenzhang.crockpot;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.AbstractCrockPotBlock;
import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.CornBlock;
import com.sihenzhang.crockpot.block.CrockPotUnknownCropsBlock;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.effect.CrockPotEffect;
import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.item.*;
import com.sihenzhang.crockpot.item.food.*;
import com.sihenzhang.crockpot.levelgen.CrockPotCropsFeature;
import com.sihenzhang.crockpot.levelgen.CrockPotCropsFeatureConfig;
import com.sihenzhang.crockpot.loot.CrockPotUnknownSeedsDropModifier;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("ALL")
public final class CrockPotRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrockPot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CrockPot.MOD_ID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CrockPot.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CrockPot.MOD_ID);

    // Effects
    public static final MobEffect gnawsGift = register(MOB_EFFECTS, "gnaws_gift", new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x650808));
    public static final MobEffect oceanAffinity = register(MOB_EFFECTS, "ocean_affinity", new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x15ddf4));
    public static final MobEffect wellFed = register(MOB_EFFECTS, "well_fed", new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0xda765b).addAttributeModifier(Attributes.ARMOR, "095FA141-E902-4BEF-99DB-DDC55213C07A", 1.0, AttributeModifier.Operation.ADDITION).addAttributeModifier(Attributes.ATTACK_DAMAGE, "5762F89C-8317-4021-B7EE-4DD93902941C", 1.0, AttributeModifier.Operation.ADDITION));
    public static final MobEffect witherResistanceEffect = register(MOB_EFFECTS, "wither_resistance", new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x72008f));

    // Recipes
    public static final RecipeSerializer<CrockPotCookingRecipe> crockPotCooking = register(RECIPE_SERIALIZERS, "crock_pot_cooking", new CrockPotCookingRecipe.Serializer());
    public static final RecipeSerializer<ExplosionCraftingRecipe> explosionCrafting = register(RECIPE_SERIALIZERS, "explosion_crafting", new ExplosionCraftingRecipe.Serializer());
    public static final RecipeSerializer<FoodValuesDefinition> foodValues = register(RECIPE_SERIALIZERS, "food_values", new FoodValuesDefinition.Serializer());
    public static final RecipeSerializer<PiglinBarteringRecipe> piglinBartering = register(RECIPE_SERIALIZERS, "piglin_bartering", new PiglinBarteringRecipe.Serializer());

    // Pots
    public static final Block crockPotBasicBlock = register(BLOCKS, "crock_pot_basic", new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 0;
        }
    });
    public static final Item crockPotBasicBlockItem = register(ITEMS, "crock_pot_basic", new CrockPotBlockItem(crockPotBasicBlock));
    public static final Block crockPotAdvancedBlock = register(BLOCKS, "crock_pot_advanced", new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 1;
        }
    });
    public static final Item crockPotAdvancedBlockItem = register(ITEMS, "crock_pot_advanced", new CrockPotBlockItem(crockPotAdvancedBlock));
    public static final Block crockPotUltimateBlock = register(BLOCKS, "crock_pot_ultimate", new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 2;
        }
    });
    public static final Item crockPotUltimateBlockItem = register(ITEMS, "crock_pot_ultimate", new CrockPotBlockItem(crockPotUltimateBlock));
    public static final BlockEntityType<CrockPotBlockEntity> crockPotBlockEntity = register(BLOCK_ENTITIES, "crock_pot", BlockEntityType.Builder.<CrockPotBlockEntity>of(CrockPotBlockEntity::new, crockPotBasicBlock, crockPotAdvancedBlock, crockPotUltimateBlock).build(null));
    public static final MenuType<CrockPotMenu> crockPotMenu = register(CONTAINERS, "crock_pot", IForgeMenuType.<CrockPotMenu>create((windowId, inv, data) -> new CrockPotMenu(windowId, inv, (CrockPotBlockEntity) inv.player.level.getBlockEntity(data.readBlockPos()))));

    // Cage
//    public static final Block birdcageBlock = register(BLOCKS, "birdcage", new BirdcageBlock());
//    public static final Item birdcageBlockItem = register(ITEMS, "birdcage", new BlockItem(CrockPotRegistry.birdcageBlock, new Item.Properties().group(CrockPot.ITEM_GROUP)));
//    public static final TileEntityType<BirdcageTileEntity> birdcageTileEntity = register(TILE_ENTITIES, "birdcage", TileEntityType.Builder.<BirdcageTileEntity>create(BirdcageTileEntity::new, birdcageBlock).build(null));
//    public static final EntityType<BirdcageEntity> birdcageEntity = register(ENTITIES, "birdcage", EntityType.Builder.<BirdcageEntity>create((entityType, world) -> new BirdcageEntity(world), EntityClassification.MISC).setCustomClientFactory((spawnEntity, world) -> new BirdcageEntity(world)).size(0.0F, 0.0F).build(CrockPot.MOD_ID + ":birdcage"));

    // Crops
    public static final GlobalLootModifierSerializer<CrockPotUnknownSeedsDropModifier> unknownSeedsDropModifier = register(LOOT_MODIFIER_SERIALIZERS, "unknown_seeds_drop", new CrockPotUnknownSeedsDropModifier.Serializer());
    public static final Block unknownCropsBlock = register(BLOCKS, "unknown_crops", new CrockPotUnknownCropsBlock());
    public static final Item unknownSeeds = register(ITEMS, "unknown_seeds", new CrockPotUnknownSeedsItem());
    public static final Feature<CrockPotCropsFeatureConfig> cropsPatchFeature = register(FEATURES, "crops_patch", new CrockPotCropsFeature(CrockPotCropsFeatureConfig.CODEC));
    public static final Block asparagusBlock = register(BLOCKS, "asparaguses", new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return asparagusSeeds;
        }
    });
    public static final Item asparagusSeeds = register(ITEMS, "asparagus_seeds", new CrockPotSeedsItem(asparagusBlock));
    public static final Item asparagus = register(ITEMS, "asparagus", CrockPotFood.builder().nutrition(3).saturationMod(0.6F).build());
    public static final Block cornBlock = register(BLOCKS, "corns", new CornBlock());
    public static final Item cornSeeds = register(ITEMS, "corn_seeds", new CrockPotSeedsItem(cornBlock));
    public static final Item corn = register(ITEMS, "corn", CrockPotFood.builder().nutrition(3).saturationMod(0.6F).build());
    public static final Item popcorn = register(ITEMS, "popcorn", CrockPotFood.builder().nutrition(3).saturationMod(0.8F).duration(FoodUseDuration.FAST).build());
    public static final Block eggplantBlock = register(BLOCKS, "eggplants", new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return eggplantSeeds;
        }
    });
    public static final Item eggplantSeeds = register(ITEMS, "eggplant_seeds", new CrockPotSeedsItem(eggplantBlock));
    public static final Item eggplant = register(ITEMS, "eggplant", CrockPotFood.builder().nutrition(3).saturationMod(0.6F).build());
    public static final Item cookedEggplant = register(ITEMS, "cooked_eggplant", CrockPotFood.builder().nutrition(5).saturationMod(0.6F).build());
    public static final Block onionBlock = register(BLOCKS, "onions", new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return onionSeeds;
        }
    });
    public static final Item onionSeeds = register(ITEMS, "onion_seeds", new CrockPotSeedsItem(onionBlock));
    public static final Item onion = register(ITEMS, "onion", CrockPotFood.builder().nutrition(3).saturationMod(0.6F).build());
    public static final Block pepperBlock = register(BLOCKS, "peppers", new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return pepperSeeds;
        }
    });
    public static final Item pepperSeeds = register(ITEMS, "pepper_seeds", new CrockPotSeedsItem(pepperBlock));
    public static final Item pepper = register(ITEMS, "pepper", CrockPotFood.builder().nutrition(3).saturationMod(0.6F).damage(CrockPotDamageSource.SPICY, 1).build());
    public static final Block tomatoBlock = register(BLOCKS, "tomatoes", new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return tomatoSeeds;
        }
    });
    public static final Item tomatoSeeds = register(ITEMS, "tomato_seeds", new CrockPotSeedsItem(tomatoBlock));
    public static final Item tomato = register(ITEMS, "tomato", CrockPotFood.builder().nutrition(3).saturationMod(0.6F).build());
    public static final Set<Item> seeds = ImmutableSet.of(unknownSeeds, asparagusSeeds, cornSeeds, eggplantSeeds, onionSeeds, pepperSeeds, tomatoSeeds);
    public static final Set<Item> crops = ImmutableSet.of(asparagus, corn, eggplant, onion, pepper, tomato);
    public static final Set<Item> cookedCrops = ImmutableSet.of(popcorn, cookedEggplant);

    // Materials
    public static final Item blackstoneDust = register(ITEMS, "blackstone_dust", new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP)));
    public static final Item collectedDust = register(ITEMS, "collected_dust", new CollectedDustItem());
    //    public static final Item birdEgg = register(ITEMS, "bird_egg", new BirdEggItem());
//    public static final EntityType<BirdEggEntity> birdEggEntity = register(ENTITIES, "bird_egg", EntityType.Builder.<BirdEggEntity>create((entityType, world) -> new BirdEggEntity(world), EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).updateInterval(10).build(CrockPot.MOD_ID + ":bird_egg"));
    public static final Item cookedEgg = register(ITEMS, "cooked_egg", CrockPotFood.builder().nutrition(3).saturationMod(0.6F).build());
    public static final Item frogLegs = register(ITEMS, "frog_legs", CrockPotFood.builder().nutrition(2).saturationMod(0.4F).meat().build());
    public static final Item cookedFrogLegs = register(ITEMS, "cooked_frog_legs", CrockPotFood.builder().nutrition(5).saturationMod(0.7F).meat().build());
    public static final Item hoglinNose = register(ITEMS, "hoglin_nose", CrockPotFood.builder().nutrition(3).saturationMod(0.2F).meat().build());
    public static final Item cookedHoglinNose = register(ITEMS, "cooked_hoglin_nose", CrockPotFood.builder().nutrition(8).saturationMod(0.7F).meat().build());
    public static final Item milkBottle = register(ITEMS, "milk_bottle", CrockPotFood.builder().nutrition(0).saturationMod(0.0F).alwaysEat().drink().tooltip("milk_bottle").build());
    public static final Item syrup = register(ITEMS, "syrup", CrockPotFood.builder().nutrition(1).saturationMod(0.3F).drink().build());
    public static final Item milkmadeHat = register(ITEMS, "milkmade_hat", new MilkmadeHatItem());
    public static final Item creativeMilkmadeHat = register(ITEMS, "creative_milkmade_hat", new CreativeMilkmadeHatItem());
    public static final Item gnawsCoin = register(ITEMS, "gnaws_coin", new GnawsCoinItem());

    // Foods
    public static final Item asparagusSoup = register(ITEMS, "asparagus_soup", CrockPotFood.builder().nutrition(4).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().removeEffect(MobEffects.WEAKNESS).removeEffect(MobEffects.DIG_SLOWDOWN).removeEffect(MobEffects.BLINDNESS).removeEffect(MobEffects.BAD_OMEN).build());
    public static final Item avaj = register(ITEMS, "avaj", CrockPotFood.builder().nutrition(2).saturationMod(3.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, (32 * 60 + 20) * 20, 2).hide().rarity(Rarity.EPIC).build());
    public static final Item baconEggs = register(ITEMS, "bacon_eggs", CrockPotFood.builder().nutrition(12).saturationMod(0.8F).heal(4.0F).build());
    public static final Item boneSoup = register(ITEMS, "bone_soup", CrockPotFood.builder().nutrition(10).saturationMod(0.6F).effect(MobEffects.ABSORPTION, 2 * 60 * 20, 1).build());
    public static final Item boneStew = register(ITEMS, "bone_stew", CrockPotFood.builder().nutrition(20).saturationMod(0.4F).duration(FoodUseDuration.SUPER_SLOW).effect(MobEffects.HEAL, 1, 1).build());
    public static final Item bunnyStew = register(ITEMS, "bunny_stew", CrockPotFood.builder().nutrition(6).saturationMod(0.8F).effect(MobEffects.REGENERATION, 5 * 20).effect(CrockPotRegistry.wellFed, 3 * 60 * 20).build());
    public static final Item californiaRoll = register(ITEMS, "california_roll", CrockPotFood.builder().nutrition(10).saturationMod(0.6F).heal(4.0F).effect(MobEffects.ABSORPTION, 60 * 20).build());
    public static final Item candy = register(ITEMS, "candy", new Candy());
    public static final Item ceviche = register(ITEMS, "ceviche", CrockPotFood.builder().nutrition(7).saturationMod(0.7F).alwaysEat().effect(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 1).effect(MobEffects.ABSORPTION, 20 * 20, 1).build());
    public static final Item fishSticks = register(ITEMS, "fish_sticks", CrockPotFood.builder().nutrition(7).saturationMod(0.7F).effect(MobEffects.REGENERATION, 30 * 20).build());
    public static final Item fishTacos = register(ITEMS, "fish_tacos", CrockPotFood.builder().nutrition(8).saturationMod(0.9F).heal(2.0F).build());
    public static final Item flowerSalad = register(ITEMS, "flower_salad", new FlowerSalad());
    public static final Item froggleBunwich = register(ITEMS, "froggle_bunwich", CrockPotFood.builder().nutrition(7).saturationMod(0.8F).build());
    public static final Item fruitMedley = register(ITEMS, "fruit_medley", CrockPotFood.builder().nutrition(8).saturationMod(0.4F).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).build());
    public static final Item gazpacho = register(ITEMS, "gazpacho", CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.FIRE_RESISTANCE, 10 * 60 * 20).build());
    public static final Item honeyHam = register(ITEMS, "honey_ham", CrockPotFood.builder().nutrition(12).saturationMod(0.8F).effect(MobEffects.REGENERATION, 20 * 20).effect(MobEffects.ABSORPTION, 60 * 20, 1).heal(6.0F).build());
    public static final Item honeyNuggets = register(ITEMS, "honey_nuggets", CrockPotFood.builder().nutrition(8).saturationMod(0.3F).effect(MobEffects.REGENERATION, 10 * 20).effect(MobEffects.ABSORPTION, 60 * 20).heal(4.0F).build());
    public static final Item hotChili = register(ITEMS, "hot_chili", CrockPotFood.builder().nutrition(9).saturationMod(0.8F).effect(MobEffects.DAMAGE_BOOST, (60 + 30) * 20).effect(MobEffects.DIG_SPEED, (60 + 30) * 20).build());
    public static final Item hotCocoa = register(ITEMS, "hot_cocoa", CrockPotFood.builder().nutrition(2).saturationMod(0.1F).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 8 * 60 * 20, 1).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).removeEffect(MobEffects.DIG_SLOWDOWN).build());
    public static final Item iceCream = register(ITEMS, "ice_cream", new IceCream());
    public static final Item icedTea = register(ITEMS, "iced_tea", CrockPotFood.builder().nutrition(3).saturationMod(0.1F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.JUMP, 5 * 60 * 20, 1).build());
    public static final Item jammyPreserves = register(ITEMS, "jammy_preserves", CrockPotFood.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).build());
    public static final Item kabobs = register(ITEMS, "kabobs", CrockPotFood.builder().nutrition(7).saturationMod(0.7F).build());
    public static final Item meatBalls = register(ITEMS, "meat_balls", CrockPotFood.builder().nutrition(9).saturationMod(0.5F).build());
    public static final Item monsterLasagna = register(ITEMS, "monster_lasagna", CrockPotFood.builder().nutrition(7).saturationMod(0.2F).effect(MobEffects.HUNGER, 15 * 20).effect(MobEffects.POISON, 2 * 20).damage(CrockPotDamageSource.MONSTER_FOOD, 6.0F).build());
    public static final Item monsterTartare = register(ITEMS, "monster_tartare", CrockPotFood.builder().nutrition(8).saturationMod(0.7F).effect(MobEffects.DAMAGE_BOOST, 2 * 60 * 20, 1).build());
    public static final Item moqueca = register(ITEMS, "moqueca", CrockPotFood.builder().nutrition(14).saturationMod(0.7F).duration(FoodUseDuration.SLOW).effect(MobEffects.HEALTH_BOOST, (60 + 30) * 20, 2).heal(6.0F).build());
    public static final Item mushyCake = register(ITEMS, "mushy_cake", CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(CrockPotRegistry.witherResistanceEffect, 60 * 20).build());
    public static final Item netherosia = register(ITEMS, "netherosia", new NetherosiaItem());
    public static final Item pepperPopper = register(ITEMS, "pepper_popper", CrockPotFood.builder().nutrition(8).saturationMod(0.8F).effect(MobEffects.DAMAGE_BOOST, 60 * 20, 1).build());
    public static final Item perogies = register(ITEMS, "perogies", CrockPotFood.builder().nutrition(8).saturationMod(0.8F).heal(6.0F).build());
    public static final Item potatoSouffle = register(ITEMS, "potato_souffle", CrockPotFood.builder().nutrition(8).saturationMod(0.7F).effect(MobEffects.DAMAGE_RESISTANCE, (60 + 30) * 20, 1).build());
    public static final Item potatoTornado = register(ITEMS, "potato_tornado", CrockPotFood.builder().nutrition(8).saturationMod(0.6F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build());
    //    public static final Block powCakeBlock = register(BLOCKS, "pow_cake", new PowCakeBlock());
    public static final Item powCake = register(ITEMS, "pow_cake", CrockPotFood.builder().nutrition(2).saturationMod(0.1F).alwaysEat().damage(CrockPotDamageSource.POW_CAKE, 1.0F).build());
    public static final Item pumpkinCookie = register(ITEMS, "pumpkin_cookie", CrockPotFood.builder().nutrition(10).saturationMod(0.7F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build());
    public static final Item ratatouille = register(ITEMS, "ratatouille", CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).build());
    public static final Item salmonSushi = register(ITEMS, "salmon_sushi", CrockPotFood.builder().nutrition(5).saturationMod(0.8F).duration(FoodUseDuration.FAST).heal(1.0F).build());
    public static final Item salsa = register(ITEMS, "salsa", CrockPotFood.builder().nutrition(7).saturationMod(0.8F).duration(FoodUseDuration.FAST).effect(MobEffects.DIG_SPEED, 6 * 60 * 20).build());
    public static final Item seafoodGumbo = register(ITEMS, "seafood_gumbo", CrockPotFood.builder().nutrition(9).saturationMod(0.7F).effect(MobEffects.REGENERATION, 2 * 60 * 20).build());
    public static final Item stuffedEggplant = register(ITEMS, "stuffed_eggplant", CrockPotFood.builder().nutrition(7).saturationMod(0.6F).duration(FoodUseDuration.FAST).heal(2.0F).build());
    public static final Item surfNTurf = register(ITEMS, "surf_n_turf", CrockPotFood.builder().nutrition(8).saturationMod(1.2F).alwaysEat().effect(MobEffects.REGENERATION, 30 * 20, 1).heal(8.0F).build());
    public static final Item taffy = register(ITEMS, "taffy", CrockPotFood.builder().nutrition(5).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(MobEffects.LUCK, 8 * 60 * 20).damage(CrockPotDamageSource.TAFFY, 1.0F).removeEffect(MobEffects.POISON).build());
    public static final Item tea = register(ITEMS, "tea", CrockPotFood.builder().nutrition(3).saturationMod(0.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.DIG_SPEED, 5 * 60 * 20, 1).build());
    public static final Item tropicalBouillabaisse = register(ITEMS, "tropical_bouillabaisse", CrockPotFood.builder().nutrition(7).saturationMod(0.6F).alwaysEat().effect(CrockPotRegistry.oceanAffinity, (2 * 60 + 30) * 20).build());
    public static final Item turkeyDinner = register(ITEMS, "turkey_dinner", CrockPotFood.builder().nutrition(12).saturationMod(0.8F).effect(MobEffects.HEALTH_BOOST, 3 * 60 * 20).build());
    public static final Item vegStinger = register(ITEMS, "veg_stinger", CrockPotFood.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.NIGHT_VISION, 10 * 60 * 20).build());
    public static final Item watermelonIcle = register(ITEMS, "watermelon_icle", CrockPotFood.builder().nutrition(5).saturationMod(0.4F).duration(FoodUseDuration.FAST).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).effect(MobEffects.JUMP, 3 * 60 * 20).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).build());
    public static final Item wetGoop = register(ITEMS, "wet_goop", CrockPotFood.builder().nutrition(0).saturationMod(0.0F).duration(FoodUseDuration.SUPER_SLOW).alwaysEat().effect(MobEffects.CONFUSION, 10 * 20).tooltip("wet_goop", ChatFormatting.ITALIC, ChatFormatting.GRAY).build());

    // Food Categories
    public static final Map<FoodCategory, Item> foodCategoryItems = Util.make(new EnumMap<FoodCategory, Item>(FoodCategory.class), map -> {
        for (FoodCategory category : FoodCategory.values()) {
            map.put(category, register(ITEMS, "food_category_" + category.name().toLowerCase(), new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP))));
        }
    });

    private static <T extends IForgeRegistryEntry<T>, E extends T> E register(final DeferredRegister<T> register, final String name, final E entry) {
        register.register(name, () -> entry);
        return entry;
    }
}
