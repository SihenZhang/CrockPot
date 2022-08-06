package com.sihenzhang.crockpot;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableSet;
import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.AbstractCrockPotBlock;
import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import com.sihenzhang.crockpot.block.CornBlock;
import com.sihenzhang.crockpot.block.UnknownCropsBlock;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.effect.CrockPotEffect;
import com.sihenzhang.crockpot.inventory.CrockPotMenu;
import com.sihenzhang.crockpot.item.*;
import com.sihenzhang.crockpot.item.food.*;
import com.sihenzhang.crockpot.loot.CrockPotUnknownSeedsDropModifier;
import com.sihenzhang.crockpot.recipe.CrockPotRecipeType;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.bartering.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvents;
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
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Set;
import java.util.function.Supplier;

public final class CrockPotRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrockPot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, CrockPot.MOD_ID);
    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.LOOT_MODIFIER_SERIALIZERS, CrockPot.MOD_ID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registry.RECIPE_TYPE_REGISTRY, CrockPot.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, CrockPot.MOD_ID);

    // Effects
    public static final RegistryObject<MobEffect> GNAWS_GIFT = MOB_EFFECTS.register("gnaws_gift", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x650808));
    public static final RegistryObject<MobEffect> OCEAN_AFFINITY = MOB_EFFECTS.register("ocean_affinity", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x15ddf4));
    public static final RegistryObject<MobEffect> WELL_FED = MOB_EFFECTS.register("well_fed", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0xda765b).addAttributeModifier(Attributes.ARMOR, "095FA141-E902-4BEF-99DB-DDC55213C07A", 1.0, AttributeModifier.Operation.ADDITION).addAttributeModifier(Attributes.ATTACK_DAMAGE, "5762F89C-8317-4021-B7EE-4DD93902941C", 1.0, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> WITHER_RESISTANCE = MOB_EFFECTS.register("wither_resistance", () -> new CrockPotEffect(MobEffectCategory.BENEFICIAL, 0x72008f));

    // Recipes
    public static final RegistryObject<RecipeType<CrockPotCookingRecipe>> CROCK_POT_COOKING_RECIPE_TYPE = RECIPE_TYPES.register("crock_pot_cooking", () -> new CrockPotRecipeType<>("crock_pot_cooking"));
    public static final RegistryObject<RecipeSerializer<CrockPotCookingRecipe>> CROCK_POT_COOKING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("crock_pot_cooking", CrockPotCookingRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<ExplosionCraftingRecipe>> EXPLOSION_CRAFTING_RECIPE_TYPE = RECIPE_TYPES.register("explosion_crafting", () -> new CrockPotRecipeType<>("explosion_crafting"));
    public static final RegistryObject<RecipeSerializer<ExplosionCraftingRecipe>> EXPLOSION_CRAFTING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("explosion_crafting", ExplosionCraftingRecipe.Serializer::new);
    public static final RegistryObject<RecipeType<FoodValuesDefinition>> FOOD_VALUES_RECIPE_TYPE = RECIPE_TYPES.register("food_values", () -> new CrockPotRecipeType<>("food_values"));
    public static final RegistryObject<RecipeSerializer<FoodValuesDefinition>> FOOD_VALUES_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("food_values", FoodValuesDefinition.Serializer::new);
    public static final RegistryObject<RecipeType<PiglinBarteringRecipe>> PIGLIN_BARTERING_RECIPE_TYPE = RECIPE_TYPES.register("piglin_bartering", () -> new CrockPotRecipeType<>("piglin_bartering"));
    public static final RegistryObject<RecipeSerializer<PiglinBarteringRecipe>> PIGLIN_BARTERING_RECIPE_SERIALIZER = RECIPE_SERIALIZERS.register("piglin_bartering", PiglinBarteringRecipe.Serializer::new);

    // Pots
    public static final RegistryObject<Block> BASIC_CROCK_POT_BLOCK = BLOCKS.register("crock_pot_basic", () -> new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 0;
        }
    });
    public static final RegistryObject<Item> BASIC_CROCK_POT_BLOCK_ITEM = ITEMS.register("crock_pot_basic", () -> new CrockPotBlockItem(BASIC_CROCK_POT_BLOCK.get()));
    public static final RegistryObject<Block> ADVANCED_CROCK_POT_BLOCK = BLOCKS.register("crock_pot_advanced", () -> new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 1;
        }
    });
    public static final RegistryObject<Item> ADVANCED_CROCK_POT_BLOCK_ITEM = ITEMS.register("crock_pot_advanced", () -> new CrockPotBlockItem(ADVANCED_CROCK_POT_BLOCK.get()));
    public static final RegistryObject<Block> ULTIMATE_CROCK_POT_BLOCK = BLOCKS.register("crock_pot_ultimate", () -> new AbstractCrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 2;
        }
    });
    public static final RegistryObject<Item> ULTIMATE_CROCK_POT_BLOCK_ITEM = ITEMS.register("crock_pot_ultimate", () -> new CrockPotBlockItem(ULTIMATE_CROCK_POT_BLOCK.get()));
    public static final RegistryObject<BlockEntityType<CrockPotBlockEntity>> CROCK_POT_BLOCK_ENTITY = BLOCK_ENTITIES.register("crock_pot", () -> BlockEntityType.Builder.of(CrockPotBlockEntity::new, BASIC_CROCK_POT_BLOCK.get(), ADVANCED_CROCK_POT_BLOCK.get(), ULTIMATE_CROCK_POT_BLOCK.get()).build(null));
    public static final RegistryObject<MenuType<CrockPotMenu>> CROCK_POT_MENU_TYPE = CONTAINERS.register("crock_pot", () -> IForgeMenuType.create((windowId, inv, data) -> new CrockPotMenu(windowId, inv, (CrockPotBlockEntity) inv.player.level.getBlockEntity(data.readBlockPos()))));

    // Cage
//    public static final Block birdcageBlock = register(BLOCKS, "birdcage", new BirdcageBlock());
//    public static final Item birdcageBlockItem = register(ITEMS, "birdcage", new BlockItem(CrockPotRegistry.birdcageBlock, new Item.Properties().group(CrockPot.ITEM_GROUP)));
//    public static final TileEntityType<BirdcageTileEntity> birdcageTileEntity = register(TILE_ENTITIES, "birdcage", TileEntityType.Builder.<BirdcageTileEntity>create(BirdcageTileEntity::new, birdcageBlock).build(null));
//    public static final EntityType<BirdcageEntity> birdcageEntity = register(ENTITIES, "birdcage", EntityType.Builder.<BirdcageEntity>create((entityType, world) -> new BirdcageEntity(world), EntityClassification.MISC).setCustomClientFactory((spawnEntity, world) -> new BirdcageEntity(world)).size(0.0F, 0.0F).build(CrockPot.MOD_ID + ":birdcage"));

    // Crops
    public static final RegistryObject<Block> UNKNOWN_CROPS_BLOCK = BLOCKS.register("unknown_crops", UnknownCropsBlock::new);
    public static final RegistryObject<Item> UNKNOWN_SEEDS = ITEMS.register("unknown_seeds", () -> new CrockPotSeedsItem(UNKNOWN_CROPS_BLOCK.get()));
    public static final RegistryObject<Block> ASPARAGUS_BLOCK = BLOCKS.register("asparaguses", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return ASPARAGUS_SEEDS.get();
        }
    });
    public static final RegistryObject<Item> ASPARAGUS_SEEDS = ITEMS.register("asparagus_seeds", () -> new CrockPotSeedsItem(ASPARAGUS_BLOCK.get()));
    public static final RegistryObject<Item> ASPARAGUS = ITEMS.register("asparagus", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Block> CORN_BLOCK = BLOCKS.register("corns", CornBlock::new);
    public static final RegistryObject<Item> CORN_SEEDS = ITEMS.register("corn_seeds", () -> new CrockPotSeedsItem(CORN_BLOCK.get()));
    public static final RegistryObject<Item> CORN = ITEMS.register("corn", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> POPCORN = ITEMS.register("popcorn", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.8F).duration(FoodUseDuration.FAST).hideEffects().build());
    public static final RegistryObject<Block> EGGPLANT_BLOCK = BLOCKS.register("eggplants", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return EGGPLANT_SEEDS.get();
        }
    });
    public static final RegistryObject<Item> EGGPLANT_SEEDS = ITEMS.register("eggplant_seeds", () -> new CrockPotSeedsItem(EGGPLANT_BLOCK.get()));
    public static final RegistryObject<Item> EGGPLANT = ITEMS.register("eggplant", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> COOKED_EGGPLANT = ITEMS.register("cooked_eggplant", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Block> GARLIC_BLOCK = BLOCKS.register("garlics", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return GARLIC_SEEDS.get();
        }
    });
    public static final RegistryObject<Item> GARLIC_SEEDS = ITEMS.register("garlic_seeds", () -> new CrockPotSeedsItem(GARLIC_BLOCK.get()));
    public static final RegistryObject<Item> GARLIC = ITEMS.register("garlic", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Block> ONION_BLOCK = BLOCKS.register("onions", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return ONION_SEEDS.get();
        }
    });
    public static final RegistryObject<Item> ONION_SEEDS = ITEMS.register("onion_seeds", () -> new CrockPotSeedsItem(ONION_BLOCK.get()));
    public static final RegistryObject<Item> ONION = ITEMS.register("onion", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Block> PEPPER_BLOCK = BLOCKS.register("peppers", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return PEPPER_SEEDS.get();
        }
    });
    public static final RegistryObject<Item> PEPPER_SEEDS = ITEMS.register("pepper_seeds", () -> new CrockPotSeedsItem(PEPPER_BLOCK.get()));
    public static final RegistryObject<Item> PEPPER = ITEMS.register("pepper", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).damage(CrockPotDamageSource.SPICY, 1).hideEffects().build());
    public static final RegistryObject<Block> TOMATO_BLOCK = BLOCKS.register("tomatoes", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return TOMATO_SEEDS.get();
        }
    });
    public static final RegistryObject<Item> TOMATO_SEEDS = ITEMS.register("tomato_seeds", () -> new CrockPotSeedsItem(TOMATO_BLOCK.get()));
    public static final RegistryObject<Item> TOMATO = ITEMS.register("tomato", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final Supplier<Set<Item>> SEEDS = Suppliers.memoize(() -> ImmutableSet.of(UNKNOWN_SEEDS.get(), ASPARAGUS_SEEDS.get(), CORN_SEEDS.get(), EGGPLANT_SEEDS.get(), GARLIC_SEEDS.get(), ONION_SEEDS.get(), PEPPER_SEEDS.get(), TOMATO_SEEDS.get()));
    public static final Supplier<Set<Item>> CROPS = Suppliers.memoize(() -> ImmutableSet.of(ASPARAGUS.get(), CORN.get(), EGGPLANT.get(), GARLIC.get(), ONION.get(), PEPPER.get(), TOMATO.get()));
    public static final Supplier<Set<Item>> COOKED_CROPS = Suppliers.memoize(() -> ImmutableSet.of(POPCORN.get(), COOKED_EGGPLANT.get()));

    // Materials
    public static final RegistryObject<Item> BLACKSTONE_DUST = ITEMS.register("blackstone_dust", () -> new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP)));
    public static final RegistryObject<Item> COLLECTED_DUST = ITEMS.register("collected_dust", CollectedDustItem::new);
    //    public static final Item birdEgg = register(ITEMS, "bird_egg", new BirdEggItem());
//    public static final EntityType<BirdEggEntity> birdEggEntity = register(ENTITIES, "bird_egg", EntityType.Builder.<BirdEggEntity>create((entityType, world) -> new BirdEggEntity(world), EntityClassification.MISC).size(0.25F, 0.25F).trackingRange(4).updateInterval(10).build(CrockPot.MOD_ID + ":bird_egg"));
    public static final RegistryObject<Item> COOKED_EGG = ITEMS.register("cooked_egg", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).hideEffects().build());
    public static final RegistryObject<Item> FROG_LEGS = ITEMS.register("frog_legs", () -> CrockPotFood.builder().nutrition(2).saturationMod(0.4F).meat().hideEffects().build());
    public static final RegistryObject<Item> COOKED_FROG_LEGS = ITEMS.register("cooked_frog_legs", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.7F).meat().hideEffects().build());
    public static final RegistryObject<Item> HOGLIN_NOSE = ITEMS.register("hoglin_nose", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.2F).meat().hideEffects().build());
    public static final RegistryObject<Item> COOKED_HOGLIN_NOSE = ITEMS.register("cooked_hoglin_nose", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.7F).meat().hideEffects().build());
    public static final RegistryObject<Item> MILK_BOTTLE = ITEMS.register("milk_bottle", () -> CrockPotFood.builder().nutrition(0).saturationMod(0.0F).alwaysEat().drink().tooltip("milk_bottle", ChatFormatting.DARK_AQUA).hideEffects().build());
    public static final RegistryObject<Item> SYRUP = ITEMS.register("syrup", () -> CrockPotFood.builder().nutrition(1).saturationMod(0.3F).drink().eatingSound(SoundEvents.HONEY_DRINK).hideEffects().build());
    public static final RegistryObject<Item> MILKMADE_HAT = ITEMS.register("milkmade_hat", MilkmadeHatItem::new);
    public static final RegistryObject<Item> CREATIVE_MILKMADE_HAT = ITEMS.register("creative_milkmade_hat", CreativeMilkmadeHatItem::new);
    public static final RegistryObject<Item> GNAWS_COIN = ITEMS.register("gnaws_coin", GnawsCoinItem::new);

    // Foods
    public static final RegistryObject<Item> ASPARAGUS_SOUP = ITEMS.register("asparagus_soup", () -> CrockPotFood.builder().nutrition(4).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().removeEffect(MobEffects.WEAKNESS).removeEffect(MobEffects.DIG_SLOWDOWN).removeEffect(MobEffects.BLINDNESS).removeEffect(MobEffects.BAD_OMEN).build());
    public static final RegistryObject<Item> AVAJ = ITEMS.register("avaj", () -> CrockPotFood.builder().nutrition(2).saturationMod(3.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, (32 * 60 + 20) * 20, 2).hide().rarity(Rarity.EPIC).build());
    public static final RegistryObject<Item> BACON_EGGS = ITEMS.register("bacon_eggs", () -> CrockPotFood.builder().nutrition(12).saturationMod(0.8F).heal(4.0F).build());
    public static final RegistryObject<Item> BONE_SOUP = ITEMS.register("bone_soup", () -> CrockPotFood.builder().nutrition(10).saturationMod(0.6F).effect(MobEffects.ABSORPTION, 2 * 60 * 20, 1).build());
    public static final RegistryObject<Item> BONE_STEW = ITEMS.register("bone_stew", () -> CrockPotFood.builder().nutrition(20).saturationMod(0.4F).duration(FoodUseDuration.SUPER_SLOW).effect(MobEffects.HEAL, 1, 1).build());
    public static final RegistryObject<Item> BUNNY_STEW = ITEMS.register("bunny_stew", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.8F).effect(MobEffects.REGENERATION, 5 * 20).effect(CrockPotRegistry.WELL_FED, 2 * 60 * 20).build());
    public static final RegistryObject<Item> CALIFORNIA_ROLL = ITEMS.register("california_roll", () -> CrockPotFood.builder().nutrition(10).saturationMod(0.6F).heal(4.0F).effect(MobEffects.ABSORPTION, 60 * 20).build());
    public static final RegistryObject<Item> CANDY = ITEMS.register("candy", Candy::new);
    public static final RegistryObject<Item> CEVICHE = ITEMS.register("ceviche", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.7F).alwaysEat().effect(MobEffects.DAMAGE_RESISTANCE, 20 * 20, 1).effect(MobEffects.ABSORPTION, 20 * 20, 1).build());
    public static final RegistryObject<Item> FISH_STICKS = ITEMS.register("fish_sticks", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.7F).effect(MobEffects.REGENERATION, 30 * 20).build());
    public static final RegistryObject<Item> FISH_TACOS = ITEMS.register("fish_tacos", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.9F).heal(2.0F).build());
    public static final RegistryObject<Item> FLOWER_SALAD = ITEMS.register("flower_salad", FlowerSalad::new);
    public static final RegistryObject<Item> FROGGLE_BUNWICH = ITEMS.register("froggle_bunwich", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.8F).build());
    public static final RegistryObject<Item> FRUIT_MEDLEY = ITEMS.register("fruit_medley", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.4F).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).build());
    public static final RegistryObject<Item> GAZPACHO = ITEMS.register("gazpacho", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.FIRE_RESISTANCE, 10 * 60 * 20).build());
    public static final RegistryObject<Item> HONEY_HAM = ITEMS.register("honey_ham", () -> CrockPotFood.builder().nutrition(12).saturationMod(0.8F).effect(MobEffects.REGENERATION, 20 * 20).effect(MobEffects.ABSORPTION, 60 * 20, 1).heal(6.0F).build());
    public static final RegistryObject<Item> HONEY_NUGGETS = ITEMS.register("honey_nuggets", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.3F).effect(MobEffects.REGENERATION, 10 * 20).effect(MobEffects.ABSORPTION, 60 * 20).heal(4.0F).build());
    public static final RegistryObject<Item> HOT_CHILI = ITEMS.register("hot_chili", () -> CrockPotFood.builder().nutrition(9).saturationMod(0.8F).effect(MobEffects.DAMAGE_BOOST, (60 + 30) * 20).effect(MobEffects.DIG_SPEED, (60 + 30) * 20).build());
    public static final RegistryObject<Item> HOT_COCOA = ITEMS.register("hot_cocoa", () -> CrockPotFood.builder().nutrition(2).saturationMod(0.1F).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 8 * 60 * 20, 1).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).removeEffect(MobEffects.DIG_SLOWDOWN).build());
    public static final RegistryObject<Item> ICE_CREAM = ITEMS.register("ice_cream", IceCream::new);
    public static final RegistryObject<Item> ICED_TEA = ITEMS.register("iced_tea", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.1F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.JUMP, 5 * 60 * 20, 1).build());
    public static final RegistryObject<Item> JAMMY_PRESERVES = ITEMS.register("jammy_preserves", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).build());
    public static final RegistryObject<Item> KABOBS = ITEMS.register("kabobs", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.7F).build());
    public static final RegistryObject<Item> MASHED_POTATOES = ITEMS.register("mashed_potatoes", () -> CrockPotFood.builder().nutrition(9).saturationMod(0.6F).effect(MobEffects.DAMAGE_RESISTANCE, 4 * 60 * 20).build());
    public static final RegistryObject<Item> MEAT_BALLS = ITEMS.register("meat_balls", () -> CrockPotFood.builder().nutrition(9).saturationMod(0.5F).build());
    public static final RegistryObject<Item> MONSTER_LASAGNA = ITEMS.register("monster_lasagna", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.2F).effect(MobEffects.HUNGER, 15 * 20).effect(MobEffects.POISON, 2 * 20).damage(CrockPotDamageSource.MONSTER_FOOD, 6.0F).build());
    public static final RegistryObject<Item> MONSTER_TARTARE = ITEMS.register("monster_tartare", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.7F).effect(MobEffects.DAMAGE_BOOST, 2 * 60 * 20, 1).build());
    public static final RegistryObject<Item> MOQUECA = ITEMS.register("moqueca", () -> CrockPotFood.builder().nutrition(14).saturationMod(0.7F).duration(FoodUseDuration.SLOW).effect(MobEffects.HEALTH_BOOST, (60 + 30) * 20, 2).heal(6.0F).build());
    public static final RegistryObject<Item> MUSHY_CAKE = ITEMS.register("mushy_cake", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(CrockPotRegistry.WITHER_RESISTANCE, 60 * 20).build());
    public static final RegistryObject<Item> NETHEROSIA = ITEMS.register("netherosia", NetherosiaItem::new);
    public static final RegistryObject<Item> PEPPER_POPPER = ITEMS.register("pepper_popper", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.8F).effect(MobEffects.DAMAGE_BOOST, 60 * 20, 1).build());
    public static final RegistryObject<Item> PEROGIES = ITEMS.register("perogies", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.8F).heal(6.0F).build());
    public static final RegistryObject<Item> POTATO_SOUFFLE = ITEMS.register("potato_souffle", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.7F).effect(MobEffects.DAMAGE_RESISTANCE, (60 + 30) * 20, 1).build());
    public static final RegistryObject<Item> POTATO_TORNADO = ITEMS.register("potato_tornado", () -> CrockPotFood.builder().nutrition(8).saturationMod(0.6F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build());
    //    public static final Block powCakeBlock = register(BLOCKS, "pow_cake", new PowCakeBlock());
    public static final RegistryObject<Item> POW_CAKE = ITEMS.register("pow_cake", () -> CrockPotFood.builder().nutrition(2).saturationMod(0.1F).alwaysEat().damage(CrockPotDamageSource.POW_CAKE, 1.0F).build());
    public static final RegistryObject<Item> PUMPKIN_COOKIE = ITEMS.register("pumpkin_cookie", () -> CrockPotFood.builder().nutrition(10).saturationMod(0.7F).duration(FoodUseDuration.FAST).removeEffect(MobEffects.HUNGER).build());
    public static final RegistryObject<Item> RATATOUILLE = ITEMS.register("ratatouille", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.4F).duration(FoodUseDuration.FAST).build());
    public static final RegistryObject<Item> SALMON_SUSHI = ITEMS.register("salmon_sushi", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.8F).duration(FoodUseDuration.FAST).heal(1.0F).build());
    public static final RegistryObject<Item> SALSA = ITEMS.register("salsa", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.8F).duration(FoodUseDuration.FAST).effect(MobEffects.DIG_SPEED, 6 * 60 * 20).build());
    public static final RegistryObject<Item> SEAFOOD_GUMBO = ITEMS.register("seafood_gumbo", () -> CrockPotFood.builder().nutrition(9).saturationMod(0.7F).effect(MobEffects.REGENERATION, 2 * 60 * 20).build());
    public static final RegistryObject<Item> STUFFED_EGGPLANT = ITEMS.register("stuffed_eggplant", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.6F).duration(FoodUseDuration.FAST).heal(2.0F).build());
    public static final RegistryObject<Item> SURF_N_TURF = ITEMS.register("surf_n_turf", () -> CrockPotFood.builder().nutrition(8).saturationMod(1.2F).alwaysEat().effect(MobEffects.REGENERATION, 30 * 20, 1).heal(8.0F).build());
    public static final RegistryObject<Item> TAFFY = ITEMS.register("taffy", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.4F).duration(FoodUseDuration.FAST).alwaysEat().effect(MobEffects.LUCK, 8 * 60 * 20).damage(CrockPotDamageSource.TAFFY, 1.0F).removeEffect(MobEffects.POISON).build());
    public static final RegistryObject<Item> TEA = ITEMS.register("tea", () -> CrockPotFood.builder().nutrition(3).saturationMod(0.6F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.MOVEMENT_SPEED, 10 * 60 * 20, 1).effect(MobEffects.DIG_SPEED, 5 * 60 * 20, 1).build());
    public static final RegistryObject<Item> TROPICAL_BOUILLABAISSE = ITEMS.register("tropical_bouillabaisse", () -> CrockPotFood.builder().nutrition(7).saturationMod(0.6F).alwaysEat().effect(CrockPotRegistry.OCEAN_AFFINITY, (2 * 60 + 30) * 20).build());
    public static final RegistryObject<Item> TURKEY_DINNER = ITEMS.register("turkey_dinner", () -> CrockPotFood.builder().nutrition(12).saturationMod(0.8F).effect(MobEffects.HEALTH_BOOST, 3 * 60 * 20).build());
    public static final RegistryObject<Item> VEG_STINGER = ITEMS.register("veg_stinger", () -> CrockPotFood.builder().nutrition(6).saturationMod(0.3F).duration(FoodUseDuration.FAST).alwaysEat().drink().effect(MobEffects.NIGHT_VISION, 10 * 60 * 20).build());
    public static final RegistryObject<Item> WATERMELON_ICLE = ITEMS.register("watermelon_icle", () -> CrockPotFood.builder().nutrition(5).saturationMod(0.4F).duration(FoodUseDuration.FAST).effect(MobEffects.MOVEMENT_SPEED, 3 * 60 * 20).effect(MobEffects.JUMP, 3 * 60 * 20).removeEffect(MobEffects.MOVEMENT_SLOWDOWN).build());
    public static final RegistryObject<Item> WET_GOOP = ITEMS.register("wet_goop", () -> CrockPotFood.builder().nutrition(0).saturationMod(0.0F).duration(FoodUseDuration.SUPER_SLOW).alwaysEat().effect(MobEffects.CONFUSION, 10 * 20).tooltip("wet_goop", ChatFormatting.DARK_AQUA).build());

    // Food Categories
    public static final EnumMap<FoodCategory, RegistryObject<Item>> FOOD_CATEGORY_ITEMS = Util.make(new EnumMap<>(FoodCategory.class), map -> {
        for (FoodCategory category : FoodCategory.values()) {
            map.put(category, ITEMS.register("food_category_" + category.name().toLowerCase(), () -> new Item(new Item.Properties().tab(CrockPot.ITEM_GROUP))));
        }
    });

    // Loot Modifiers
    static {
        LOOT_MODIFIER_SERIALIZERS.register("unknown_seeds_drop", CrockPotUnknownSeedsDropModifier.Serializer::new);
    }
}
