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
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("ALL")
public final class CrockPotRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrockPot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CrockPot.MOD_ID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CrockPot.MOD_ID);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, CrockPot.MOD_ID);

    // Effects
    public static final RegistryObject<CrockPotEffect> gnawsGift = MOB_EFFECTS.register("gnaws_gift", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x650808));
    public static final RegistryObject<CrockPotEffect> oceanAffinity = MOB_EFFECTS.register("ocean_affinity", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x15ddf4));
    public static final RegistryObject<MobEffect> wellFed = MOB_EFFECTS.register("well_fed", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0xda765b).addAttributeModifier(Attributes.ARMOR, "095FA141-E902-4BEF-99DB-DDC55213C07A", 1.0, AttributeModifier.Operation.ADDITION).addAttributeModifier(Attributes.ATTACK_DAMAGE, "5762F89C-8317-4021-B7EE-4DD93902941C", 1.0, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<CrockPotEffect> witherResistanceEffect = MOB_EFFECTS.register("wither_resistance", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x72008f));

    // Recipes
    public static final RegistryObject<CrockPotCookingRecipe.Serializer> crockPotCooking = RECIPE_SERIALIZERS.register("crock_pot_cooking", () -> new CrockPotCookingRecipe.Serializer());
    public static final RegistryObject<ExplosionCraftingRecipe.Serializer> explosionCrafting = RECIPE_SERIALIZERS.register("explosion_crafting", () -> new ExplosionCraftingRecipe.Serializer());
    public static final RegistryObject<FoodValuesDefinition.Serializer> foodValues = RECIPE_SERIALIZERS.register("food_values", () -> new FoodValuesDefinition.Serializer());
    public static final RegistryObject<PiglinBarteringRecipe.Serializer> piglinBartering = RECIPE_SERIALIZERS.register("piglin_bartering", () -> new PiglinBarteringRecipe.Serializer());

    // Pots
    public static final RegistryObject<AbstractCrockPotBlock> crockPotBasicBlock = BLOCKS.register("crock_pot_basic", () -> new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 0;
        }
    });
    public static final RegistryObject<CrockPotBlockItem> crockPotBasicBlockItem = ITEMS.register("crock_pot_basic", () -> new CrockPotBlockItem(crockPotBasicBlock.get()));
    public static final RegistryObject<AbstractCrockPotBlock> crockPotAdvancedBlock = BLOCKS.register("crock_pot_advanced", () -> new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 1;
        }
    });
    public static final RegistryObject<CrockPotBlockItem> crockPotAdvancedBlockItem = ITEMS.register("crock_pot_advanced", () -> new CrockPotBlockItem(crockPotAdvancedBlock.get()));
    public static final RegistryObject<AbstractCrockPotBlock> crockPotUltimateBlock = BLOCKS.register("crock_pot_ultimate", () -> new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 2;
        }
    });
    public static final RegistryObject<CrockPotBlockItem> crockPotUltimateBlockItem = ITEMS.register("crock_pot_ultimate", () -> new CrockPotBlockItem(crockPotUltimateBlock.get()));
    public static final RegistryObject<BlockEntityType<CrockPotBlockEntity>> crockPotBlockEntity = BLOCK_ENTITIES.register("crock_pot", () -> BlockEntityType.Builder.<CrockPotBlockEntity>of(CrockPotBlockEntity::new, crockPotBasicBlock.get(), crockPotAdvancedBlock.get(), crockPotUltimateBlock.get()).build(null));
    public static final RegistryObject<MenuType<CrockPotMenu>> crockPotMenu = CONTAINERS.register("crock_pot", () -> IForgeMenuType.<CrockPotMenu>create((windowId, inv, data) -> new CrockPotMenu(windowId, inv, (CrockPotBlockEntity) inv.player.level.getBlockEntity(data.readBlockPos()))));

    // Cage
//    public static final Block birdcageBlock = register(BLOCKS, "birdcage", new BirdcageBlock());
//    public static final Item birdcageBlockItem = register(ITEMS, "birdcage", new BlockItem(CrockPotRegistry.birdcageBlock, new Item.Properties().group(CrockPot.ITEM_GROUP)));
//    public static final TileEntityType<BirdcageTileEntity> birdcageTileEntity = register(TILE_ENTITIES, "birdcage", TileEntityType.Builder.<BirdcageTileEntity>create(BirdcageTileEntity::new, birdcageBlock).build(null));
//    public static final EntityType<BirdcageEntity> birdcageEntity = register(ENTITIES, "birdcage", EntityType.Builder.<BirdcageEntity>create((entityType, world) -> new BirdcageEntity(world), EntityClassification.MISC).setCustomClientFactory((spawnEntity, world) -> new BirdcageEntity(world)).size(0.0F, 0.0F).build(CrockPot.MOD_ID + ":birdcage"));

    // Crops

    public static final RegistryObject<CrockPotUnknownCropsBlock> unknownCropsBlock = BLOCKS.register("unknown_crops", () -> new CrockPotUnknownCropsBlock());
    public static final RegistryObject<CrockPotUnknownSeedsItem> unknownSeeds = ITEMS.register("unknown_seeds", CrockPotUnknownSeedsItem::new);
    public static final RegistryObject<Feature<CrockPotCropsFeatureConfig>> cropsPatchFeature = FEATURES.register("crops_patch", () -> new CrockPotCropsFeature(CrockPotCropsFeatureConfig.CODEC));
    public static final RegistryObject<AbstractCrockPotCropBlock> asparagusBlock = BLOCKS.register("asparaguses", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return asparagusSeeds.get();
        }
    });
    public static final RegistryObject<CrockPotSeedsItem> asparagusSeeds = ITEMS.register("asparagus_seeds", () -> new CrockPotSeedsItem(asparagusBlock.get()));
    public static final RegistryObject<CrockPotFood> asparagus = ITEMS.register("asparagus", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<CornBlock> cornBlock = BLOCKS.register("corns", CornBlock::new);
    public static final RegistryObject<CrockPotSeedsItem> cornSeeds = ITEMS.register("corn_seeds", () -> new CrockPotSeedsItem(cornBlock.get()));
    public static final RegistryObject<CrockPotFood> corn = ITEMS.register("corn", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<CrockPotFood> popcorn = ITEMS.register("popcorn", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.8F).duration(FoodUseDuration.FAST).hideEffects().build());
    public static final RegistryObject<AbstractCrockPotCropBlock> eggplantBlock = BLOCKS.register("eggplants", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return eggplantSeeds.get();
        }
    });
    public static final RegistryObject<CrockPotSeedsItem> eggplantSeeds = ITEMS.register("eggplant_seeds", () -> new CrockPotSeedsItem(eggplantBlock.get()));
    public static final RegistryObject<CrockPotFood> eggplant = ITEMS.register("eggplant", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<CrockPotFood> cookedEggplant = ITEMS.register("cooked_eggplant", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<AbstractCrockPotCropBlock> onionBlock = BLOCKS.register("onions", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return onionSeeds.get();
        }
    });
    public static final RegistryObject<CrockPotSeedsItem> onionSeeds = ITEMS.register("onion_seeds", () -> new CrockPotSeedsItem(onionBlock.get()));
    public static final RegistryObject<CrockPotFood> onion = ITEMS.register("onion", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<AbstractCrockPotCropBlock> pepperBlock = BLOCKS.register("peppers", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return pepperSeeds.get();
        }
    });
    public static final RegistryObject<CrockPotSeedsItem> pepperSeeds = ITEMS.register("pepper_seeds", () -> new CrockPotSeedsItem(pepperBlock.get()));
    public static final RegistryObject<CrockPotFood> pepper = ITEMS.register("pepper", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).damage(CrockPotDamageSource.SPICY, 1).hideEffects().build());
    public static final RegistryObject<AbstractCrockPotCropBlock> tomatoBlock = BLOCKS.register("tomatoes", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return tomatoSeeds.get();
        }
    });
    public static final RegistryObject<CrockPotSeedsItem> tomatoSeeds = ITEMS.register("tomato_seeds", () -> new CrockPotSeedsItem(tomatoBlock.get()));
    public static final RegistryObject<CrockPotFood> tomato = ITEMS.register("tomato", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final Supplier<Set<Item>> seeds = () -> ImmutableSet.of(unknownSeeds.get(), asparagusSeeds.get(), cornSeeds.get(), eggplantSeeds.get(), onionSeeds.get(), pepperSeeds.get(), tomatoSeeds.get());
    public static final Supplier<Set<Item>> crops = () -> ImmutableSet.of(asparagus.get(), corn.get(), eggplant.get(), onion.get(), pepper.get(), tomato.get());
    public static final Supplier<Set<Item>> cookedCrops = () -> ImmutableSet.of(popcorn.get(), cookedEggplant.get());

    // Materials
    public static final RegistryObject<Item> blackstoneDust = ITEMS.register("blackstone_dust", () -> new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP)));
    public static final RegistryObject<CollectedDustItem> collectedDust = ITEMS.register("collected_dust", () -> new CollectedDustItem());
    //    public static final Item birdEgg = register(ITEMS, "bird_egg", new BirdEggItem());
//    public static final EntityType<BirdEggEntity> birdEggEntity = register(ENTITIES, "bird_egg", EntityType.Builder.<BirdEggEntity>create((entityType, world) -> new BirdEggEntity(world), EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).updateInterval(10).build(CrockPot.MOD_ID + ":bird_egg"));
    public static final RegistryObject<CrockPotFood> cookedEgg = ITEMS.register("cooked_egg", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<CrockPotFood> frogLegs = ITEMS.register("frog_legs", () -> CrockPotFood.builder().nutrition(2).saturationMod(0.4F).meat().hideEffects().build());
    public static final RegistryObject<CrockPotFood> cookedFrogLegs = ITEMS.register("cooked_frog_legs", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.7F).meat().hideEffects().build());
    public static final RegistryObject<CrockPotFood> hoglinNose = ITEMS.register("hoglin_nose", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.2F).meat().hideEffects().build());
    public static final RegistryObject<CrockPotFood> cookedHoglinNose = ITEMS.register("cooked_hoglin_nose", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.7F).meat().hideEffects().build());
    public static final RegistryObject<CrockPotFood> milkBottle = ITEMS.register("milk_bottle", () -> CrockPotFood.builder().nutrition(0).saturationMod(0.0F).alwaysEat().drink().tooltip("milk_bottle", ChatFormatting.DARK_AQUA).hideEffects().build());
    public static final RegistryObject<CrockPotFood> syrup = ITEMS.register("syrup", () -> CrockPotFood.builder().nutrition(1).saturationMod(0.3F).drink().hideEffects().build());
    public static final RegistryObject<MilkmadeHatItem> milkmadeHat = ITEMS.register("milkmade_hat", () -> new MilkmadeHatItem());
    public static final RegistryObject<CreativeMilkmadeHatItem> creativeMilkmadeHat = ITEMS.register("creative_milkmade_hat", () -> new CreativeMilkmadeHatItem());
    public static final RegistryObject<GnawsCoinItem> gnawsCoin = ITEMS.register("gnaws_coin", () -> new GnawsCoinItem());

    // Foods
    public static final RegistryObject<CrockPotFood> asparagusSoup = ITEMS.register("asparagus_soup", () -> CrockPotFood.builder().nutrition(4).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().removeEffect(MobEffects.WEAKNESS).removeEffect(MobEffects.DIG_SLOWDOWN).removeEffect(MobEffects.BLINDNESS).removeEffect(MobEffects.BAD_OMEN).build());
    public static final RegistryObject<CrockPotFood> avaj = ITEMS.register("avaj", () -> CrockPotFood.builder().nutrition(2).saturationMod(3.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, (32 * 60 + 20) * 20, 2).hide().rarity(Rarity.EPIC).build());
    public static final RegistryObject<CrockPotFood> baconEggs = ITEMS.register("bacon_eggs", () -> CrockPotFood.builder().nutrition(12).saturationMod(0.8F).heal(4.0F).build());
    public static final RegistryObject<CrockPotFood> boneSoup = ITEMS.register("bone_soup", () -> CrockPotFood.builder().nutrition(10).saturationMod(0.6F).effect(MobEffects.ABSORPTION, 2 * 60 * 20, 1).build());
    public static final RegistryObject<CrockPotFood> boneStew = ITEMS.register("bone_stew", () -> CrockPotFood.builder().nutrition(20).saturationMod(0.4F).duration(FoodUseDuration.SUPER_SLOW).effect(MobEffects.HEAL, 1, 1).build());
    public static final RegistryObject<CrockPotFood> bunnyStew = ITEMS.register("bunny_stew", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.8F).effect(MobEffects.REGENERATION, 5 * 20).effect(CrockPotRegistry.wellFed, 2 * 60 * 20).build());
    public static final RegistryObject<CrockPotFood> californiaRoll = ITEMS.register("california_roll", () -> CrockPotFood.builder().nutrition(10).saturationMod(0.6F).heal(4.0F).effect(MobEffects.ABSORPTION, 60 * 20).build());
    public static final RegistryObject<Candy> candy = ITEMS.register("candy", () -> new Candy());
    public static final RegistryObject<CrockPotFood> ceviche = ITEMS.register("ceviche", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.7F).alwaysEat().effect(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 1).effect(MobEffects.ABSORPTION, 20 * 20, 1).build());
    public static final RegistryObject<CrockPotFood> fishSticks = ITEMS.register("fish_sticks", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.7F).effect(MobEffects.REGENERATION, 30 * 20).build());
    public static final RegistryObject<CrockPotFood> fishTacos = ITEMS.register("fish_tacos", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.9F).heal(2.0F).build());
    public static final RegistryObject<FlowerSalad> flowerSalad = ITEMS.register("flower_salad", () -> new FlowerSalad());
    public static final RegistryObject<CrockPotFood> froggleBunwich = ITEMS.register("froggle_bunwich", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.8F).build());
    public static final RegistryObject<CrockPotFood> fruitMedley = ITEMS.register("fruit_medley", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.4F).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).build());
    public static final RegistryObject<CrockPotFood> gazpacho = ITEMS.register("gazpacho", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.FIRE_RESISTANCE, 10 * 60 * 20).build());
    public static final RegistryObject<CrockPotFood> honeyHam = ITEMS.register("honey_ham", () -> CrockPotFood.builder().nutrition(12).saturationMod(0.8F).effect(MobEffects.REGENERATION, 20 * 20).effect(MobEffects.ABSORPTION, 60 * 20, 1).heal(6.0F).build());
    public static final RegistryObject<CrockPotFood> honeyNuggets = ITEMS.register("honey_nuggets", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.3F).effect(MobEffects.REGENERATION, 10 * 20).effect(MobEffects.ABSORPTION, 60 * 20).heal(4.0F).build());
    public static final RegistryObject<CrockPotFood> hotChili = ITEMS.register("hot_chili", () -> CrockPotFood.builder().nutrition(9).saturationMod(0.8F).effect(MobEffects.DAMAGE_BOOST, (60 + 30) * 20).effect(MobEffects.DIG_SPEED, (60 + 30) * 20).build());
    public static final RegistryObject<CrockPotFood> hotCocoa = ITEMS.register("hot_cocoa", () -> CrockPotFood.builder().nutrition(2).saturationMod(0.1F).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 8 * 60 * 20, 1).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).removeEffect(MobEffects.DIG_SLOWDOWN).build());
    public static final RegistryObject<IceCream> iceCream = ITEMS.register("ice_cream", () -> new IceCream());
    public static final RegistryObject<CrockPotFood> icedTea = ITEMS.register("iced_tea", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.1F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.JUMP, 5 * 60 * 20, 1).build());
    public static final RegistryObject<CrockPotFood> jammyPreserves = ITEMS.register("jammy_preserves", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).build());
    public static final RegistryObject<CrockPotFood> kabobs = ITEMS.register("kabobs", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.7F).build());
    public static final RegistryObject<CrockPotFood> meatBalls = ITEMS.register("meat_balls", () -> CrockPotFood.builder().nutrition(9).saturationMod(0.5F).build());
    public static final RegistryObject<CrockPotFood> monsterLasagna = ITEMS.register("monster_lasagna", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.2F).effect(MobEffects.HUNGER, 15 * 20).effect(MobEffects.POISON, 2 * 20).damage(CrockPotDamageSource.MONSTER_FOOD, 6.0F).build());
    public static final RegistryObject<CrockPotFood> monsterTartare = ITEMS.register("monster_tartare", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.7F).effect(MobEffects.DAMAGE_BOOST, 2 * 60 * 20, 1).build());
    public static final RegistryObject<CrockPotFood> moqueca = ITEMS.register("moqueca", () -> CrockPotFood.builder().nutrition(14).saturationMod(0.7F).duration(FoodUseDuration.SLOW).effect(MobEffects.HEALTH_BOOST, (60 + 30) * 20, 2).heal(6.0F).build());
    public static final RegistryObject<CrockPotFood> mushyCake = ITEMS.register("mushy_cake", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(CrockPotRegistry.witherResistanceEffect, 60 * 20).build());
    public static final RegistryObject<NetherosiaItem> netherosia = ITEMS.register("netherosia", () -> new NetherosiaItem());
    public static final RegistryObject<CrockPotFood> pepperPopper = ITEMS.register("pepper_popper", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.8F).effect(MobEffects.DAMAGE_BOOST, 60 * 20, 1).build());
    public static final RegistryObject<CrockPotFood> perogies = ITEMS.register("perogies", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.8F).heal(6.0F).build());
    public static final RegistryObject<Item> potatoSouffle = ITEMS.register("potato_souffle", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.7F).effect(MobEffects.DAMAGE_RESISTANCE, (60 + 30) * 20, 1).build());
    public static final RegistryObject<Item> potatoTornado = ITEMS.register("potato_tornado", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.6F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build());
    //    public static final Block powCakeBlock = register(BLOCKS, "pow_cake", new PowCakeBlock());
    public static final RegistryObject<Item> powCake = ITEMS.register("pow_cake", () -> CrockPotFood.builder().nutrition(2).saturationMod(0.1F).alwaysEat().damage(CrockPotDamageSource.POW_CAKE, 1.0F).build());
    public static final RegistryObject<Item> pumpkinCookie = ITEMS.register("pumpkin_cookie", () -> CrockPotFood.builder().nutrition(10).saturationMod(0.7F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build());
    public static final RegistryObject<Item> ratatouille = ITEMS.register("ratatouille", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).build());
    public static final RegistryObject<Item> salmonSushi = ITEMS.register("salmon_sushi", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.8F).duration(FoodUseDuration.FAST).heal(1.0F).build());
    public static final RegistryObject<Item> salsa = ITEMS.register("salsa", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.8F).duration(FoodUseDuration.FAST).effect(MobEffects.DIG_SPEED, 6 * 60 * 20).build());
    public static final RegistryObject<Item> seafoodGumbo = ITEMS.register("seafood_gumbo", () -> CrockPotFood.builder().nutrition(9).saturationMod(0.7F).effect(MobEffects.REGENERATION, 2 * 60 * 20).build());
    public static final RegistryObject<Item> stuffedEggplant = ITEMS.register("stuffed_eggplant", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.6F).duration(FoodUseDuration.FAST).heal(2.0F).build());
    public static final RegistryObject<Item> surfNTurf = ITEMS.register("surf_n_turf", () -> CrockPotFood.builder().nutrition(8).saturationMod(1.2F).alwaysEat().effect(MobEffects.REGENERATION, 30 * 20, 1).heal(8.0F).build());
    public static final RegistryObject<Item> taffy = ITEMS.register("taffy", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(MobEffects.LUCK, 8 * 60 * 20).damage(CrockPotDamageSource.TAFFY, 1.0F).removeEffect(MobEffects.POISON).build());
    public static final RegistryObject<Item> tea = ITEMS.register("tea", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.DIG_SPEED, 5 * 60 * 20, 1).build());
    public static final RegistryObject<Item> tropicalBouillabaisse = ITEMS.register("tropical_bouillabaisse", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.6F).alwaysEat().effect(CrockPotRegistry.oceanAffinity, (2 * 60 + 30) * 20).build());
    public static final RegistryObject<Item> turkeyDinner = ITEMS.register("turkey_dinner", () -> CrockPotFood.builder().nutrition(12).saturationMod(0.8F).effect(MobEffects.HEALTH_BOOST, 3 * 60 * 20).build());
    public static final RegistryObject<Item> vegStinger = ITEMS.register("veg_stinger", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.NIGHT_VISION, 10 * 60 * 20).build());
    public static final RegistryObject<Item> watermelonIcle = ITEMS.register("watermelon_icle", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.4F).duration(FoodUseDuration.FAST).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).effect(MobEffects.JUMP, 3 * 60 * 20).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).build());
    public static final RegistryObject<Item> wetGoop = ITEMS.register("wet_goop", () -> CrockPotFood.builder().nutrition(0).saturationMod(0.0F).duration(FoodUseDuration.SUPER_SLOW).alwaysEat().effect(MobEffects.CONFUSION, 10 * 20).tooltip("wet_goop", ChatFormatting.DARK_AQUA).build());

    // Food Categories
    public static final EnumMap<FoodCategory, RegistryObject<Item>> foodCategoryItems = Util.make(new EnumMap<FoodCategory, RegistryObject<Item>>(FoodCategory.class), map -> {
        for (FoodCategory category : FoodCategory.values()) {
            map.put(category, ITEMS.register("food_category_" + category.name().toLowerCase(), () -> new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP))));
        }
    });

//    private static <T extends IForgeRegistryEntry<T>, E extends T> E register(final DeferredRegister<T> register, final String name, final E entry) {
//        register.register(name, () -> entry);
//        return entry;
//    }
}
