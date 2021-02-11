package com.sihenzhang.crockpot;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.crockpot.base.CrockPotDamageSource;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.CornBlock;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.block.CrockPotCropsBlock;
import com.sihenzhang.crockpot.block.CrockPotUnknownCropsBlock;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.item.CrockPotBlockItem;
import com.sihenzhang.crockpot.item.CrockPotFoodCategoryItem;
import com.sihenzhang.crockpot.item.CrockPotSeedsItem;
import com.sihenzhang.crockpot.item.CrockPotUnknownSeedsItem;
import com.sihenzhang.crockpot.item.food.*;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.item.Rarity;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.commons.lang3.EnumUtils;

import javax.annotation.Nonnull;
import java.util.EnumMap;
import java.util.Objects;
import java.util.Set;

@SuppressWarnings("ALL")
public final class CrockPotRegistry {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CrockPot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);

    // Pots
    public static RegistryObject<Block> crockPotBasicBlock = BLOCKS.register("crock_pot_basic", () -> new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 0;
        }
    });
    public static RegistryObject<Item> crockPotBasicBlockItem = ITEMS.register("crock_pot_basic", () -> new CrockPotBlockItem(crockPotBasicBlock.get()));
    public static RegistryObject<Block> crockPotAdvancedBlock = BLOCKS.register("crock_pot_advanced", () -> new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 1;
        }
    });
    public static RegistryObject<Item> crockPotAdvancedBlockItem = ITEMS.register("crock_pot_advanced", () -> new CrockPotBlockItem(crockPotAdvancedBlock.get()));
    public static RegistryObject<Block> crockPotUltimateBlock = BLOCKS.register("crock_pot_ultimate", () -> new CrockPotBlock() {
        @Override
        public int getPotLevel() {
            return 2;
        }
    });
    public static RegistryObject<Item> crockPotUltimateBlockItem = ITEMS.register("crock_pot_ultimate", () -> new CrockPotBlockItem(crockPotUltimateBlock.get()));
    public static RegistryObject<TileEntityType<CrockPotTileEntity>> crockPotTileEntity = TILES.register("crock_pot", () -> TileEntityType.Builder.create(CrockPotTileEntity::new, crockPotBasicBlock.get(), crockPotAdvancedBlock.get(), crockPotUltimateBlock.get()).build(null));
    public static RegistryObject<ContainerType<CrockPotContainer>> crockPotContainer = CONTAINERS.register("crock_pot", () -> IForgeContainerType.create((windowId, inv, data) -> {
        BlockPos pos = data.readBlockPos();
        TileEntity tileEntity = inv.player.world.getTileEntity(pos);
        return new CrockPotContainer(windowId, inv, (CrockPotTileEntity) Objects.requireNonNull(tileEntity));
    }));

    // Crops
    public static RegistryObject<Block> unknownCrops = BLOCKS.register("unknown_crops", CrockPotUnknownCropsBlock::new);
    public static RegistryObject<Item> unknownSeeds = ITEMS.register("unknown_seeds", CrockPotUnknownSeedsItem::new);
    public static RegistryObject<Block> asparagusBlock = BLOCKS.register("asparaguses", () -> new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getSeedsItem() {
            return asparagusSeeds.get();
        }
    });
    public static RegistryObject<Item> asparagusSeeds = ITEMS.register("asparagus_seeds", () -> new CrockPotSeedsItem(asparagusBlock.get()));
    public static RegistryObject<Item> asparagus = ITEMS.register("asparagus", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static RegistryObject<Block> cornBlock = BLOCKS.register("corns", CornBlock::new);
    public static RegistryObject<Item> cornSeeds = ITEMS.register("corn_seeds", () -> new CrockPotSeedsItem(cornBlock.get()));
    public static RegistryObject<Item> corn = ITEMS.register("corn", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static RegistryObject<Item> popcorn = ITEMS.register("popcorn", () -> CrockPotFood.builder().hunger(3).saturation(0.8F).duration(FoodUseDuration.FAST).build());
    public static RegistryObject<Block> eggplantBlock = BLOCKS.register("eggplants", () -> new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getSeedsItem() {
            return eggplantSeeds.get();
        }
    });
    public static RegistryObject<Item> eggplantSeeds = ITEMS.register("eggplant_seeds", () -> new CrockPotSeedsItem(eggplantBlock.get()));
    public static RegistryObject<Item> eggplant = ITEMS.register("eggplant", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static RegistryObject<Item> cookedEggplant = ITEMS.register("cooked_eggplant", () -> CrockPotFood.builder().hunger(5).saturation(0.6F).build());
    public static RegistryObject<Block> onionBlock = BLOCKS.register("onions", () -> new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getSeedsItem() {
            return onionSeeds.get();
        }
    });
    public static RegistryObject<Item> onionSeeds = ITEMS.register("onion_seeds", () -> new CrockPotSeedsItem(onionBlock.get()));
    public static RegistryObject<Item> onion = ITEMS.register("onion", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static RegistryObject<Block> pepperBlock = BLOCKS.register("peppers", () -> new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getSeedsItem() {
            return pepperSeeds.get();
        }
    });
    public static RegistryObject<Item> pepperSeeds = ITEMS.register("pepper_seeds", () -> new CrockPotSeedsItem(pepperBlock.get()));
    public static RegistryObject<Item> pepper = ITEMS.register("pepper", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).damage(CrockPotDamageSource.SPICY, 1).build());
    public static RegistryObject<Block> tomatoBlock = BLOCKS.register("tomatoes", () -> new CrockPotCropsBlock() {
        @Nonnull
        @Override
        protected IItemProvider getSeedsItem() {
            return tomatoSeeds.get();
        }
    });
    public static RegistryObject<Item> tomatoSeeds = ITEMS.register("tomato_seeds", () -> new CrockPotSeedsItem(tomatoBlock.get()));
    public static RegistryObject<Item> tomato = ITEMS.register("tomato", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).build());
    public static Set<RegistryObject<Item>> seeds = ImmutableSet.of(unknownSeeds, asparagusSeeds, cornSeeds, eggplantSeeds, onionSeeds, pepperSeeds, tomatoSeeds);
    public static Set<RegistryObject<Item>> crops = ImmutableSet.of(asparagus, corn, eggplant, onion, pepper, tomato);
    public static Set<RegistryObject<Item>> cookedCrops = ImmutableSet.of(popcorn, cookedEggplant);

    // Materials
    public static RegistryObject<Item> milkBottle = ITEMS.register("milk_bottle", () -> CrockPotFood.builder().hunger(0).saturation(0.0F).setAlwaysEdible().setDrink().tooltip("milk_bottle").build());
    public static RegistryObject<Item> syrup = ITEMS.register("syrup", () -> CrockPotFood.builder().hunger(1).saturation(0.3F).setDrink().build());

    // Foods
    public static RegistryObject<Item> asparagusSoup = ITEMS.register("asparagus_soup", () -> CrockPotFood.builder().hunger(4).saturation(0.3F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().removePotion(Effects.WEAKNESS).removePotion(Effects.MINING_FATIGUE).removePotion(Effects.BLINDNESS).removePotion(Effects.BAD_OMEN).build());
    public static RegistryObject<Item> avaj = ITEMS.register("avaj", () -> CrockPotFood.builder().hunger(2).saturation(3.6F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.SPEED, (32 * 60 + 20) * 20, 2).setHidden().rarity(Rarity.EPIC).build());
    public static RegistryObject<Item> baconEggs = ITEMS.register("bacon_eggs", () -> CrockPotFood.builder().hunger(12).saturation(0.8F).heal(4.0F).build());
    public static RegistryObject<Item> boneSoup = ITEMS.register("bone_soup", () -> CrockPotFood.builder().hunger(10).saturation(0.6F).effect(Effects.ABSORPTION, 2 * 60 * 20, 1).build());
    public static RegistryObject<Item> boneStew = ITEMS.register("bone_stew", () -> CrockPotFood.builder().hunger(20).saturation(0.4F).duration(FoodUseDuration.SUPER_SLOW).effect(Effects.INSTANT_HEALTH, 1, 1).build());
    public static RegistryObject<Item> californiaRoll = ITEMS.register("california_roll", () -> CrockPotFood.builder().hunger(10).saturation(0.6F).heal(4.0F).effect(Effects.ABSORPTION, 60 * 20).build());
    public static RegistryObject<Item> candy = ITEMS.register("candy", Candy::new);
    public static RegistryObject<Item> ceviche = ITEMS.register("ceviche", () -> CrockPotFood.builder().hunger(7).saturation(0.7F).setAlwaysEdible().effect(Effects.RESISTANCE, 20 * 20, 1).effect(Effects.ABSORPTION, 20 * 20, 1).build());
    public static RegistryObject<Item> fishSticks = ITEMS.register("fish_sticks", () -> CrockPotFood.builder().hunger(7).saturation(0.7F).effect(Effects.REGENERATION, 30 * 20).build());
    public static RegistryObject<Item> fishTacos = ITEMS.register("fish_tacos", () -> CrockPotFood.builder().hunger(8).saturation(0.9F).heal(2.0F).build());
    public static RegistryObject<Item> flowerSalad = ITEMS.register("flower_salad", FlowerSalad::new);
    public static RegistryObject<Item> fruitMedley = ITEMS.register("fruit_medley", () -> CrockPotFood.builder().hunger(8).saturation(0.4F).effect(Effects.SPEED, 3 * 60 * 20).build());
    public static RegistryObject<Item> gazpacho = ITEMS.register("gazpacho", () -> CrockPotFood.builder().hunger(6).saturation(0.4F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.FIRE_RESISTANCE, 10 * 60 * 20).build());
    public static RegistryObject<Item> honeyHam = ITEMS.register("honey_ham", () -> CrockPotFood.builder().hunger(12).saturation(0.8F).effect(Effects.REGENERATION, 20 * 20).effect(Effects.ABSORPTION, 60 * 20, 1).heal(6.0F).build());
    public static RegistryObject<Item> honeyNuggets = ITEMS.register("honey_nuggets", () -> CrockPotFood.builder().hunger(8).saturation(0.3F).effect(Effects.REGENERATION, 10 * 20).effect(Effects.ABSORPTION, 60 * 20).heal(4.0F).build());
    public static RegistryObject<Item> hotChili = ITEMS.register("hot_chili", () -> CrockPotFood.builder().hunger(9).saturation(0.8F).effect(Effects.STRENGTH, (60 + 30) * 20).effect(Effects.HASTE, (60 + 30) * 20).build());
    public static RegistryObject<Item> hotCocoa = ITEMS.register("hot_cocoa", () -> CrockPotFood.builder().hunger(2).saturation(0.1F).setAlwaysEdible().setDrink().effect(Effects.SPEED, 8 * 60 * 20, 1).removePotion(Effects.SLOWNESS).removePotion(Effects.MINING_FATIGUE).build());
    public static RegistryObject<Item> iceCream = ITEMS.register("ice_cream", IceCream::new);
    public static RegistryObject<Item> icedTea = ITEMS.register("iced_tea", () -> CrockPotFood.builder().hunger(3).saturation(0.1F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.SPEED, 10 * 60 * 20, 1).effect(Effects.JUMP_BOOST, 5 * 60 * 20, 1).build());
    public static RegistryObject<Item> jammyPreserves = ITEMS.register("jammy_preserves", () -> CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).build());
    public static RegistryObject<Item> kabobs = ITEMS.register("kabobs", () -> CrockPotFood.builder().hunger(7).saturation(0.7F).build());
    public static RegistryObject<Item> meatBalls = ITEMS.register("meat_balls", () -> CrockPotFood.builder().hunger(9).saturation(0.5F).build());
    public static RegistryObject<Item> monsterLasagna = ITEMS.register("monster_lasagna", () -> CrockPotFood.builder().hunger(7).saturation(0.2F).effect(Effects.HUNGER, 15 * 20).effect(Effects.POISON, 2 * 20).damage(CrockPotDamageSource.MONSTER_FOOD, 6.0F).build());
    public static RegistryObject<Item> monsterTartare = ITEMS.register("monster_tartare", () -> CrockPotFood.builder().hunger(8).saturation(0.7F).effect(Effects.STRENGTH, 2 * 60 * 20, 1).build());
    public static RegistryObject<Item> moqueca = ITEMS.register("moqueca", () -> CrockPotFood.builder().hunger(14).saturation(0.7F).duration(FoodUseDuration.SLOW).effect(Effects.HEALTH_BOOST, (60 + 30) * 20, 2).heal(6.0F).build());
    public static RegistryObject<Item> pepperPopper = ITEMS.register("pepper_popper", () -> CrockPotFood.builder().hunger(8).saturation(0.8F).effect(Effects.STRENGTH, 60 * 20, 1).build());
    public static RegistryObject<Item> perogies = ITEMS.register("perogies", () -> CrockPotFood.builder().hunger(8).saturation(0.8F).heal(6.0F).build());
    public static RegistryObject<Item> potatoSouffle = ITEMS.register("potato_souffle", () -> CrockPotFood.builder().hunger(8).saturation(0.7F).effect(Effects.RESISTANCE, (60 + 30) * 20, 1).build());
    public static RegistryObject<Item> potatoTornado = ITEMS.register("potato_tornado", () -> CrockPotFood.builder().hunger(8).saturation(0.6F).duration(FoodUseDuration.FAST).removePotion(Effects.HUNGER).build());
    public static RegistryObject<Item> powCake = ITEMS.register("pow_cake", () -> CrockPotFood.builder().hunger(2).saturation(0.1F).setAlwaysEdible().damage(CrockPotDamageSource.POW_CAKE, 1.0F).build());
    public static RegistryObject<Item> pumpkinCookie = ITEMS.register("pumpkin_cookie", () -> CrockPotFood.builder().hunger(10).saturation(0.7F).duration(FoodUseDuration.FAST).removePotion(Effects.HUNGER).build());
    public static RegistryObject<Item> ratatouille = ITEMS.register("ratatouille", () -> CrockPotFood.builder().hunger(6).saturation(0.4F).duration(FoodUseDuration.FAST).build());
    public static RegistryObject<Item> salsa = ITEMS.register("salsa", () -> CrockPotFood.builder().hunger(7).saturation(0.8F).duration(FoodUseDuration.FAST).effect(Effects.HASTE, 6 * 60 * 20).build());
    public static RegistryObject<Item> seafoodGumbo = ITEMS.register("seafood_gumbo", () -> CrockPotFood.builder().hunger(9).saturation(0.7F).effect(Effects.REGENERATION, 2 * 60 * 20, 1).build());
    public static RegistryObject<Item> stuffedEggplant = ITEMS.register("stuffed_eggplant", () -> CrockPotFood.builder().hunger(7).saturation(0.6F).duration(FoodUseDuration.FAST).heal(2.0F).build());
    public static RegistryObject<Item> surfNTurf = ITEMS.register("surf_n_turf", () -> CrockPotFood.builder().hunger(8).saturation(1.2F).setAlwaysEdible().effect(Effects.REGENERATION, 30 * 20, 1).heal(8.0F).build());
    public static RegistryObject<Item> taffy = ITEMS.register("taffy", () -> CrockPotFood.builder().hunger(5).saturation(0.4F).duration(FoodUseDuration.FAST).setAlwaysEdible().effect(Effects.LUCK, 8 * 60 * 20).damage(CrockPotDamageSource.TAFFY, 1.0F).removePotion(Effects.POISON).build());
    public static RegistryObject<Item> tea = ITEMS.register("tea", () -> CrockPotFood.builder().hunger(3).saturation(0.6F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.SPEED, 10 * 60 * 20, 1).effect(Effects.HASTE, 5 * 60 * 20, 1).build());
    public static RegistryObject<Item> tropicalBouillabaisse = ITEMS.register("tropical_bouillabaisse", () -> CrockPotFood.builder().hunger(7).saturation(0.6F).setAlwaysEdible().effect(Effects.DOLPHINS_GRACE, 5 * 60 * 20).effect(Effects.WATER_BREATHING, 5 * 60 * 20).build());
    public static RegistryObject<Item> turkeyDinner = ITEMS.register("turkey_dinner", () -> CrockPotFood.builder().hunger(12).saturation(0.8F).effect(Effects.HEALTH_BOOST, 3 * 60 * 20).build());
    public static RegistryObject<Item> vegStinger = ITEMS.register("veg_stinger", () -> CrockPotFood.builder().hunger(6).saturation(0.3F).duration(FoodUseDuration.FAST).setAlwaysEdible().setDrink().effect(Effects.NIGHT_VISION, 10 * 60 * 20).build());
    public static RegistryObject<Item> watermelonIcle = ITEMS.register("watermelon_icle", () -> CrockPotFood.builder().hunger(5).saturation(0.4F).duration(FoodUseDuration.FAST).effect(Effects.SPEED, 3 * 60 * 20).effect(Effects.JUMP_BOOST, 3 * 60 * 20).removePotion(Effects.SLOWNESS).build());
    public static RegistryObject<Item> wetGoop = ITEMS.register("wet_goop", () -> CrockPotFood.builder().hunger(0).saturation(0.0F).duration(FoodUseDuration.SUPER_SLOW).setAlwaysEdible().effect(Effects.NAUSEA, 10 * 20).tooltip("wet_goop", TextFormatting.ITALIC, TextFormatting.GRAY).build());

    // Food Categories
    public static EnumMap<FoodCategory, RegistryObject<Item>> foodCategoryItems = new EnumMap<FoodCategory, RegistryObject<Item>>(FoodCategory.class) {{
        for (FoodCategory category : EnumUtils.getEnumList(FoodCategory.class)) {
            put(category, ITEMS.register("food_category_" + category.name().toLowerCase(), CrockPotFoodCategoryItem::new));
        }
    }};
}
