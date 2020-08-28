package com.sihenzhang.crockpot.registry;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.*;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.item.*;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

@SuppressWarnings("ALL")
public final class CrockPotRegistry {
    public static final DeferredRegister<Item> ITEMS = new DeferredRegister<>(ForgeRegistries.ITEMS, CrockPot.MOD_ID);
    public static final DeferredRegister<Block> BLOCKS = new DeferredRegister<>(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);
    public static final DeferredRegister<TileEntityType<?>> TILES = new DeferredRegister<>(ForgeRegistries.TILE_ENTITIES, CrockPot.MOD_ID);
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, CrockPot.MOD_ID);

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
    public static RegistryObject<Block> asparagusBlock = BLOCKS.register("asparaguses", AsparagusBlock::new);
    public static RegistryObject<Item> asparagus = ITEMS.register("asparagus", () -> new CrockPotCropsBlockItem(asparagusBlock.get(), 3, 0.6F));
    public static RegistryObject<Block> cornBlock = BLOCKS.register("corns", CornBlock::new);
    public static RegistryObject<Item> cornSeeds = ITEMS.register("corn_seeds", () -> new CrockPotCropsBlockItem(cornBlock.get()));
    public static RegistryObject<Item> corn = ITEMS.register("corn", () -> new CrockPotBaseItemFood(1, 0.3F));
    public static RegistryObject<Item> popcorn = ITEMS.register("popcorn", () -> new CrockPotBaseItemFood(3, 0.3F, 16));
    public static RegistryObject<Block> onionBlock = BLOCKS.register("onions", OnionBlock::new);
    public static RegistryObject<Item> onion = ITEMS.register("onion", () -> new CrockPotCropsBlockItem(onionBlock.get(), 3, 0.6F));
    public static RegistryObject<Block> tomatoBlock = BLOCKS.register("tomatoes", TomatoBlock::new);
    public static RegistryObject<Item> tomatoSeeds = ITEMS.register("tomato_seeds", () -> new CrockPotCropsBlockItem(tomatoBlock.get()));
    public static RegistryObject<Item> tomato = ITEMS.register("tomato", () -> new CrockPotBaseItemFood(3, 0.6F));

    // Materials
    public static RegistryObject<Item> milkBottle = ITEMS.register("milk_bottle", MilkBottle::new);
    public static RegistryObject<Item> syrup = ITEMS.register("syrup", () -> new CrockPotBaseItemFood(1, 0.1F, true));

    // Foods
    public static RegistryObject<Item> asparagusSoup = ITEMS.register("asparagus_soup", () -> new CrockPotBaseItemFood(5, 0.3F, () -> new EffectInstance(Effects.SPEED, 60 * 20), () -> new EffectInstance(Effects.HASTE, 60 * 20), 24, true));
    public static RegistryObject<Item> avaj = ITEMS.register("avaj", Avaj::new);
    public static RegistryObject<Item> baconEggs = ITEMS.register("bacon_eggs", () -> new CrockPotBaseItemFood(12, 0.8F));
    public static RegistryObject<Item> boneSoup = ITEMS.register("bone_soup", () -> new CrockPotBaseItemFood(8, 0.3F, () -> new EffectInstance(Effects.ABSORPTION, 60 * 20), 40));
    public static RegistryObject<Item> boneStew = ITEMS.register("bone_stew", () -> new CrockPotBaseItemFood(20, 0.1F, () -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 1), 48));
    public static RegistryObject<Item> californiaRoll = ITEMS.register("california_roll", () -> new CrockPotBaseItemFood(8, 0.6F, () -> new EffectInstance(Effects.DOLPHINS_GRACE, 5 * 20)));
    public static RegistryObject<Item> candy = ITEMS.register("candy", Candy::new);
    public static RegistryObject<Item> ceviche = ITEMS.register("ceviche", () -> new CrockPotAlwaysEdibleItemFood(6, 0.1F, () -> new EffectInstance(Effects.HASTE, 2 * 60 * 20), () -> new EffectInstance(Effects.NIGHT_VISION, 2 * 60 * 20)));
    public static RegistryObject<Item> fishSticks = ITEMS.register("fish_sticks", () -> new CrockPotBaseItemFood(7, 0.6F, () -> new EffectInstance(Effects.INSTANT_HEALTH, 1)));
    public static RegistryObject<Item> fishTacos = ITEMS.register("fish_tacos", () -> new CrockPotBaseItemFood(8, 0.8F));
    public static RegistryObject<Item> flowerSalad = ITEMS.register("flower_salad", FlowerSalad::new);
    public static RegistryObject<Item> fruitMedley = ITEMS.register("fruit_medley", () -> new CrockPotBaseItemFood(8, 0.6F, () -> new EffectInstance(Effects.HASTE, 3 * 60 * 20, 1)));
    public static RegistryObject<Item> gazpacho = ITEMS.register("gazpacho", () -> new CrockPotBaseItemFood(6, 0.3F, true));
    public static RegistryObject<Item> honeyHam = ITEMS.register("honey_ham", () -> new CrockPotBaseItemFood(12, 0.6F, () -> new EffectInstance(Effects.REGENERATION, 5 * 20, 1)));
    public static RegistryObject<Item> honeyNuggets = ITEMS.register("honey_nuggets", () -> new CrockPotBaseItemFood(8, 0.3F, () -> new EffectInstance(Effects.REGENERATION, 5 * 20)));
    public static RegistryObject<Item> hotChili = ITEMS.register("hot_chili", () -> new CrockPotBaseItemFood(8, 0.6F, () -> new EffectInstance(Effects.STRENGTH, 60 * 20), () -> new EffectInstance(Effects.HASTE, 60 * 20)));
    public static RegistryObject<Item> hotCocoa = ITEMS.register("hot_cocoa", () -> new CrockPotAlwaysEdibleItemFood(2, 0.1F, () -> new EffectInstance(Effects.SPEED, 4 * 60 * 20, 1), 24, true));
    public static RegistryObject<Item> iceCream = ITEMS.register("ice_cream", IceCream::new);
    public static RegistryObject<Item> icedTea = ITEMS.register("iced_tea", () -> new CrockPotAlwaysEdibleItemFood(3, 0.1F, () -> new EffectInstance(Effects.SPEED, (60 + 30) * 20, 1), () -> new EffectInstance(Effects.HASTE, (60 + 30) * 20), 24, true));
    public static RegistryObject<Item> jammyPreserves = ITEMS.register("jammy_preserves", () -> new CrockPotBaseItemFood(6, 0.3F));
    public static RegistryObject<Item> kabobs = ITEMS.register("kabobs", () -> new CrockPotBaseItemFood(7, 0.3F));
    public static RegistryObject<Item> meatBalls = ITEMS.register("meat_balls", () -> new CrockPotBaseItemFood(10, 0.3F));
    public static RegistryObject<Item> monsterLasagna = ITEMS.register("monster_lasagna", MonsterLasagna::new);
    public static RegistryObject<Item> monsterTartare = ITEMS.register("monster_tartare", MonsterTartare::new);
    public static RegistryObject<Item> moqueca = ITEMS.register("moqueca", () -> new CrockPotBaseItemFood(17, 0.6F, () -> new EffectInstance(Effects.RESISTANCE, 30 * 20, 1), () -> new EffectInstance(Effects.REGENERATION, 5 * 20, 1), 40));
    public static RegistryObject<Item> perogies = ITEMS.register("perogies", () -> new CrockPotBaseItemFood(8, 0.8F, () -> new EffectInstance(Effects.REGENERATION, 10 * 20, 1)));
    public static RegistryObject<Item> potatoSouffle = ITEMS.register("potato_souffle", PotatoSouffle::new);
    public static RegistryObject<Item> potatoTornado = ITEMS.register("potato_tornado", PotatoTornado::new);
    public static RegistryObject<Item> powCake = ITEMS.register("pow_cake", PowCake::new);
    public static RegistryObject<Item> pumpkinCookie = ITEMS.register("pumpkin_cookie", PumpkinCookie::new);
    public static RegistryObject<Item> ratatouille = ITEMS.register("ratatouille", () -> new CrockPotBaseItemFood(6, 0.6F, 24));
    public static RegistryObject<Item> salsa = ITEMS.register("salsa", Salsa::new);
    public static RegistryObject<Item> seafoodGumbo = ITEMS.register("seafood_gumbo", () -> new CrockPotBaseItemFood(8, 0.6F, () -> new EffectInstance(Effects.INSTANT_HEALTH, 1, 1), 24));
    public static RegistryObject<Item> surfNTurf = ITEMS.register("surf_n_turf", () -> new CrockPotBaseItemFood(7, 1.2F, () -> new EffectInstance(Effects.REGENERATION, 5 * 20, 1), () -> new EffectInstance(Effects.ABSORPTION, 10 * 20, 1)));
    public static RegistryObject<Item> taffy = ITEMS.register("taffy", Taffy::new);
    public static RegistryObject<Item> tea = ITEMS.register("tea", () -> new CrockPotAlwaysEdibleItemFood(3, 0.6F, () -> new EffectInstance(Effects.SPEED, (60 + 30) * 20, 1), () -> new EffectInstance(Effects.REGENERATION, 20 * 20), 24, true));
    public static RegistryObject<Item> tropicalBouillabaisse = ITEMS.register("tropical_bouillabaisse", () -> new CrockPotAlwaysEdibleItemFood(7, 0.6F, () -> new EffectInstance(Effects.SPEED, 3 * 60 * 20), () -> new EffectInstance(Effects.DOLPHINS_GRACE, 3 * 60 * 20)));
    public static RegistryObject<Item> turkeyDinner = ITEMS.register("turkey_dinner", () -> new CrockPotBaseItemFood(12, 0.8F, () -> new EffectInstance(Effects.RESISTANCE, 60 * 20)));
    public static RegistryObject<Item> vegStinger = ITEMS.register("veg_stinger", () -> new CrockPotAlwaysEdibleItemFood(6, 0.3F, () -> new EffectInstance(Effects.FIRE_RESISTANCE, 5 * 60 * 20), 24, true));
    public static RegistryObject<Item> watermelonIcle = ITEMS.register("watermelon_icle", WatermelonIcle::new);
    public static RegistryObject<Item> wetGoop = ITEMS.register("wet_goop", WetGoop::new);

    // Ingredients
    public static RegistryObject<Item> ingredientMeat = ITEMS.register("ingredient_meat", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientMonster = ITEMS.register("ingredient_monster", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientFish = ITEMS.register("ingredient_fish", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientEgg = ITEMS.register("ingredient_egg", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientFruit = ITEMS.register("ingredient_fruit", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientVeggie = ITEMS.register("ingredient_veggie", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientDairy = ITEMS.register("ingredient_dairy", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientSweetener = ITEMS.register("ingredient_sweetener", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientFrozen = ITEMS.register("ingredient_frozen", CrockPotIngredientItem::new);
    public static RegistryObject<Item> ingredientInedible = ITEMS.register("ingredient_inedible", CrockPotIngredientItem::new);
}
