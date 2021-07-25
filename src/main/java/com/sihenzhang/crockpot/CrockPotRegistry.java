package com.sihenzhang.crockpot;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.CornBlock;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.block.CrockPotCropsBlock;
import com.sihenzhang.crockpot.block.CrockPotUnknownCropsBlock;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.item.*;
import com.sihenzhang.crockpot.item.food.*;
import com.sihenzhang.crockpot.loot.CrockPotUnknownSeedsDropModifier;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import com.sihenzhang.crockpot.world.CrockPotCropsFeature;
import com.sihenzhang.crockpot.world.CrockPotCropsFeatureConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.commons.lang3.EnumUtils;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("ALL")
public final class CrockPotRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrockPot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, CrockPot.MOD_ID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CrockPot.MOD_ID);

    // Effects
    public static final Effect witherResistanceEffect = register(EFFECTS, "wither_resistance", new Effect(EffectType.BENEFICIAL, 0x72008f) {
    });

    // Pots
    public static final Block crockPotBasicBlock = register(BLOCKS, "crock_pot_basic", new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 0;
        }
    });
    public static final Item crockPotBasicBlockItem = register(ITEMS, "crock_pot_basic", new CrockPotBlockItem(crockPotBasicBlock));
    public static final Block crockPotAdvancedBlock = register(BLOCKS, "crock_pot_advanced", new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 1;
        }
    });
    public static final Item crockPotAdvancedBlockItem = register(ITEMS, "crock_pot_advanced", new CrockPotBlockItem(crockPotAdvancedBlock));
    public static final Block crockPotUltimateBlock = register(BLOCKS, "crock_pot_ultimate", new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 2;
        }
    });
    public static final Item crockPotUltimateBlockItem = register(ITEMS, "crock_pot_ultimate", new CrockPotBlockItem(crockPotUltimateBlock));
    public static final TileEntityType<CrockPotTileEntity> crockPotTileEntity = register(TILE_ENTITIES, "crock_pot", TileEntityType.Builder.<CrockPotTileEntity>of(CrockPotTileEntity::new, crockPotBasicBlock, crockPotAdvancedBlock, crockPotUltimateBlock).build(null));
    public static final ContainerType<CrockPotContainer> crockPotContainer = register(CONTAINERS, "crock_pot", IForgeContainerType.<CrockPotContainer>create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntity tileEntity = inv.player.level.getBlockEntity(pos);
        return new CrockPotContainer(windowId, inv, (CrockPotTileEntity) Objects.requireNonNull(tileEntity));
    }));

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
    public static final Block asparagusBlock = register(BLOCKS, "asparaguses", new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getBaseSeedId() {
            return asparagusSeeds;
        }
    });
    public static final Item asparagusSeeds = register(ITEMS, "asparagus_seeds", new CrockPotSeedsItem(asparagusBlock));
    public static final Item asparagus = register(ITEMS, "asparagus", CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static final Block cornBlock = register(BLOCKS, "corns", new CornBlock());
    public static final Item cornSeeds = register(ITEMS, "corn_seeds", new CrockPotSeedsItem(cornBlock));
    public static final Item corn = register(ITEMS, "corn", CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static final Item popcorn = register(ITEMS, "popcorn", CrockPotFood.builder().hunger(3).saturation(0.8F).duration(FoodUseDuration.FAST).build());
    public static final Block eggplantBlock = register(BLOCKS, "eggplants", new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getBaseSeedId() {
            return eggplantSeeds;
        }
    });
    public static final Item eggplantSeeds = register(ITEMS, "eggplant_seeds", new CrockPotSeedsItem(eggplantBlock));
    public static final Item eggplant = register(ITEMS, "eggplant", CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static final Item cookedEggplant = register(ITEMS, "cooked_eggplant", CrockPotFood.builder().hunger(5).saturation(0.6F).build());
    public static final Block onionBlock = register(BLOCKS, "onions", new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getBaseSeedId() {
            return onionSeeds;
        }
    });
    public static final Item onionSeeds = register(ITEMS, "onion_seeds", new CrockPotSeedsItem(onionBlock));
    public static final Item onion = register(ITEMS, "onion", CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static final Block pepperBlock = register(BLOCKS, "peppers", new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getBaseSeedId() {
            return pepperSeeds;
        }
    });
    public static final Item pepperSeeds = register(ITEMS, "pepper_seeds", new CrockPotSeedsItem(pepperBlock));
    public static final Item pepper = register(ITEMS, "pepper", CrockPotFood.builder().hunger(3).saturation(0.6F).damage(CrockPotDamageSource.SPICY, 1).build());
    public static final Block tomatoBlock = register(BLOCKS, "tomatoes", new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getBaseSeedId() {
            return tomatoSeeds;
        }
    });
    public static final Item tomatoSeeds = register(ITEMS, "tomato_seeds", new CrockPotSeedsItem(tomatoBlock));
    public static final Item tomato = register(ITEMS, "tomato", CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static final Set<Item> seeds = ImmutableSet.of(unknownSeeds, asparagusSeeds, cornSeeds, eggplantSeeds, onionSeeds, pepperSeeds, tomatoSeeds);
    public static final Set<Item> crops = ImmutableSet.of(asparagus, corn, eggplant, onion, pepper, tomato);
    public static final Set<Item> cookedCrops = ImmutableSet.of(popcorn, cookedEggplant);

    // Materials
    public static final Item blackstoneDust = register(ITEMS, "blackstone_dust", new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP)));
    public static final Item collectedDust = register(ITEMS, "collected_dust", new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP)));
//    public static final Item birdEgg = register(ITEMS, "bird_egg", new BirdEggItem());
//    public static final EntityType<BirdEggEntity> birdEggEntity = register(ENTITIES, "bird_egg", EntityType.Builder.<BirdEggEntity>create((entityType, world) -> new BirdEggEntity(world), EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).updateInterval(10).build(CrockPot.MOD_ID + ":bird_egg"));
    public static final Item cookedEgg = register(ITEMS, "cooked_egg", CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static final Item frogLegs = register(ITEMS, "frog_legs", CrockPotFood.builder().hunger(2).saturation(0.4F).build());
    public static final Item cookedFrogLegs = register(ITEMS, "cooked_frog_legs", CrockPotFood.builder().hunger(5).saturation(0.7F).build());
    public static final Item hoglinNose = register(ITEMS, "hoglin_nose", CrockPotFood.builder().hunger(3).saturation(0.2F).build());
    public static final Item cookedHoglinNose = register(ITEMS, "cooked_hoglin_nose", CrockPotFood.builder().hunger(8).saturation(0.7F).build());
    public static final Item milkBottle = register(ITEMS, "milk_bottle", CrockPotFood.builder().hunger(0).saturation(0.0F).setAlwaysEdible().setDrink().tooltip("milk_bottle").build());
    public static final Item syrup = register(ITEMS, "syrup", CrockPotFood.builder().hunger(1).saturation(0.3F).setDrink().build());
    public static final Item milkmadeHat = register(ITEMS, "milkmade_hat", new MilkmadeHatItem());
    public static final Item creativeMilkmadeHat = register(ITEMS, "creative_milkmade_hat", new CreativeMilkmadeHatItem());

    // Foods
    public static final Item asparagusSoup = register(ITEMS, "asparagus_soup", CrockPotFood.builder().hunger(4).saturation(0.3F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().removePotion(Effects.WEAKNESS).removePotion(Effects.DIG_SLOWDOWN).removePotion(Effects.BLINDNESS).removePotion(Effects.BAD_OMEN).build());
    public static final Item avaj = register(ITEMS, "avaj", CrockPotFood.builder().hunger(2).saturation(3.6F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.MOVEMENT_SPEED, (32 * 60 + 20) * 20, 2).setHidden().rarity(Rarity.EPIC).build());
    public static final Item baconEggs = register(ITEMS, "bacon_eggs", CrockPotFood.builder().hunger(12).saturation(0.8F).heal(4.0F).build());
    public static final Item boneSoup = register(ITEMS, "bone_soup", CrockPotFood.builder().hunger(10).saturation(0.6F).effect(Effects.ABSORPTION, 2 * 60 * 20, 1).build());
    public static final Item boneStew = register(ITEMS, "bone_stew", CrockPotFood.builder().hunger(20).saturation(0.4F).duration(FoodUseDuration.SUPER_SLOW).effect(Effects.HEAL, 1, 1).build());
    public static final Item californiaRoll = register(ITEMS, "california_roll", CrockPotFood.builder().hunger(10).saturation(0.6F).heal(4.0F).effect(Effects.ABSORPTION, 60 * 20).build());
    public static final Item candy = register(ITEMS, "candy", new Candy());
    public static final Item ceviche = register(ITEMS, "ceviche", CrockPotFood.builder().hunger(7).saturation(0.7F).setAlwaysEdible().effect(Effects.DAMAGE_RESISTANCE, 20 * 20, 1).effect(Effects.ABSORPTION, 20 * 20, 1).build());
    public static final Item fishSticks = register(ITEMS, "fish_sticks", CrockPotFood.builder().hunger(7).saturation(0.7F).effect(Effects.REGENERATION, 30 * 20).build());
    public static final Item fishTacos = register(ITEMS, "fish_tacos", CrockPotFood.builder().hunger(8).saturation(0.9F).heal(2.0F).build());
    public static final Item flowerSalad = register(ITEMS, "flower_salad", new FlowerSalad());
    public static final Item froggleBunwich = register(ITEMS, "froggle_bunwich", CrockPotFood.builder().hunger(7).saturation(0.8F).build());
    public static final Item fruitMedley = register(ITEMS, "fruit_medley", CrockPotFood.builder().hunger(8).saturation(0.4F).effect(Effects.MOVEMENT_SPEED, 3 * 60 * 20).build());
    public static final Item gazpacho = register(ITEMS, "gazpacho", CrockPotFood.builder().hunger(6).saturation(0.4F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.FIRE_RESISTANCE, 10 * 60 * 20).build());
    public static final Item honeyHam = register(ITEMS, "honey_ham", CrockPotFood.builder().hunger(12).saturation(0.8F).effect(Effects.REGENERATION, 20 * 20).effect(Effects.ABSORPTION, 60 * 20, 1).heal(6.0F).build());
    public static final Item honeyNuggets = register(ITEMS, "honey_nuggets", CrockPotFood.builder().hunger(8).saturation(0.3F).effect(Effects.REGENERATION, 10 * 20).effect(Effects.ABSORPTION, 60 * 20).heal(4.0F).build());
    public static final Item hotChili = register(ITEMS, "hot_chili", CrockPotFood.builder().hunger(9).saturation(0.8F).effect(Effects.DAMAGE_BOOST, (60 + 30) * 20).effect(Effects.DIG_SPEED, (60 + 30) * 20).build());
    public static final Item hotCocoa = register(ITEMS, "hot_cocoa", CrockPotFood.builder().hunger(2).saturation(0.1F).setAlwaysEdible().setDrink().effect(Effects.MOVEMENT_SPEED, 8 * 60 * 20, 1).removePotion(Effects.MOVEMENT_SLOWDOWN).removePotion(Effects.DIG_SLOWDOWN).build());
    public static final Item iceCream = register(ITEMS, "ice_cream", new IceCream());
    public static final Item icedTea = register(ITEMS, "iced_tea", CrockPotFood.builder().hunger(3).saturation(0.1F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(Effects.JUMP, 5 * 60 * 20, 1).build());
    public static final Item jammyPreserves = register(ITEMS, "jammy_preserves", CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).build());
    public static final Item kabobs = register(ITEMS, "kabobs", CrockPotFood.builder().hunger(7).saturation(0.7F).build());
    public static final Item meatBalls = register(ITEMS, "meat_balls", CrockPotFood.builder().hunger(9).saturation(0.5F).build());
    public static final Item monsterLasagna = register(ITEMS, "monster_lasagna", CrockPotFood.builder().hunger(7).saturation(0.2F).effect(Effects.HUNGER, 15 * 20).effect(Effects.POISON, 2 * 20).damage(CrockPotDamageSource.MONSTER_FOOD, 6.0F).build());
    public static final Item monsterTartare = register(ITEMS, "monster_tartare", CrockPotFood.builder().hunger(8).saturation(0.7F).effect(Effects.DAMAGE_BOOST, 2 * 60 * 20, 1).build());
    public static final Item moqueca = register(ITEMS, "moqueca", CrockPotFood.builder().hunger(14).saturation(0.7F).duration(FoodUseDuration.SLOW).effect(Effects.HEALTH_BOOST, (60 + 30) * 20, 2).heal(6.0F).build());
    public static final Item mushyCake = register(ITEMS, "mushy_cake", CrockPotFood.builder().hunger(6).saturation(0.4F).duration(FoodUseDuration.FAST).setAlwaysEdible().effect(CrockPotRegistry.witherResistanceEffect, 60 * 20).build());
    public static final Item netherosia = register(ITEMS, "netherosia", new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP)));
    public static final Item pepperPopper = register(ITEMS, "pepper_popper", CrockPotFood.builder().hunger(8).saturation(0.8F).effect(Effects.DAMAGE_BOOST, 60 * 20, 1).build());
    public static final Item perogies = register(ITEMS, "perogies", CrockPotFood.builder().hunger(8).saturation(0.8F).heal(6.0F).build());
    public static final Item potatoSouffle = register(ITEMS, "potato_souffle", CrockPotFood.builder().hunger(8).saturation(0.7F).effect(Effects.DAMAGE_RESISTANCE, (60 + 30) * 20, 1).build());
    public static final Item potatoTornado = register(ITEMS, "potato_tornado", CrockPotFood.builder().hunger(8).saturation(0.6F).duration(FoodUseDuration.FAST).removePotion(Effects.HUNGER).build());
    public static final Item powCake = register(ITEMS, "pow_cake", CrockPotFood.builder().hunger(2).saturation(0.1F).setAlwaysEdible().damage(CrockPotDamageSource.POW_CAKE, 1.0F).build());
    public static final Item pumpkinCookie = register(ITEMS, "pumpkin_cookie", CrockPotFood.builder().hunger(10).saturation(0.7F).duration(FoodUseDuration.FAST).removePotion(Effects.HUNGER).build());
    public static final Item ratatouille = register(ITEMS, "ratatouille", CrockPotFood.builder().hunger(6).saturation(0.4F).duration(FoodUseDuration.FAST).build());
    public static final Item salsa = register(ITEMS, "salsa", CrockPotFood.builder().hunger(7).saturation(0.8F).duration(FoodUseDuration.FAST).effect(Effects.DIG_SPEED, 6 * 60 * 20).build());
    public static final Item seafoodGumbo = register(ITEMS, "seafood_gumbo", CrockPotFood.builder().hunger(9).saturation(0.7F).effect(Effects.REGENERATION, 2 * 60 * 20).build());
    public static final Item stuffedEggplant = register(ITEMS, "stuffed_eggplant", CrockPotFood.builder().hunger(7).saturation(0.6F).duration(FoodUseDuration.FAST).heal(2.0F).build());
    public static final Item surfNTurf = register(ITEMS, "surf_n_turf", CrockPotFood.builder().hunger(8).saturation(1.2F).setAlwaysEdible().effect(Effects.REGENERATION, 30 * 20, 1).heal(8.0F).build());
    public static final Item taffy = register(ITEMS, "taffy", CrockPotFood.builder().hunger(5).saturation(0.4F).duration(FoodUseDuration.FAST).setAlwaysEdible().effect(Effects.LUCK, 8 * 60 * 20).damage(CrockPotDamageSource.TAFFY, 1.0F).removePotion(Effects.POISON).build());
    public static final Item tea = register(ITEMS, "tea", CrockPotFood.builder().hunger(3).saturation(0.6F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(Effects.DIG_SPEED, 5 * 60 * 20, 1).build());
    public static final Item tropicalBouillabaisse = register(ITEMS, "tropical_bouillabaisse", CrockPotFood.builder().hunger(7).saturation(0.6F).setAlwaysEdible().effect(Effects.DOLPHINS_GRACE, 5 * 60 * 20).effect(Effects.WATER_BREATHING, 5 * 60 * 20).build());
    public static final Item turkeyDinner = register(ITEMS, "turkey_dinner", CrockPotFood.builder().hunger(12).saturation(0.8F).effect(Effects.HEALTH_BOOST, 3 * 60 * 20).build());
    public static final Item vegStinger = register(ITEMS, "veg_stinger", CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.NIGHT_VISION, 10 * 60 * 20).build());
    public static final Item watermelonIcle = register(ITEMS, "watermelon_icle", CrockPotFood.builder().hunger(5).saturation(0.4F).duration(FoodUseDuration.FAST).effect(Effects.MOVEMENT_SPEED, 3 * 60 * 20).effect(Effects.JUMP, 3 * 60 * 20).removePotion(Effects.MOVEMENT_SLOWDOWN).build());
    public static final Item wetGoop = register(ITEMS, "wet_goop", CrockPotFood.builder().hunger(0).saturation(0.0F).duration(FoodUseDuration.SUPER_SLOW).setAlwaysEdible().effect(Effects.CONFUSION, 10 * 20).tooltip("wet_goop", TextFormatting.ITALIC, TextFormatting.GRAY).build());

    // Food Categories
    public static final Map<FoodCategory, Item> foodCategoryItems = new EnumMap<FoodCategory, Item>(FoodCategory.class) {{
        for (FoodCategory category : EnumUtils.getEnumList(FoodCategory.class)) {
            put(category, register(ITEMS, "food_category_" + category.name().toLowerCase(), new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP))));
        }
    }};

    private static <T extends IForgeRegistryEntry<T>, E extends T> E register(final DeferredRegister<T> register, final String name, final E entry) {
        register.register(name, () -> entry);
        return entry;
    }
}
