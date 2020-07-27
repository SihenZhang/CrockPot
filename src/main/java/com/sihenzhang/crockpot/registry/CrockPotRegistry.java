package com.sihenzhang.crockpot.registry;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlock;
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

public class CrockPotRegistry {
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

    // Foods
    public static RegistryObject<Item> baconEggs = ITEMS.register("bacon_eggs", () -> new CrockPotBaseItemFood(12, 19.2F));
    public static RegistryObject<Item> boneStew = ITEMS.register("bone_stew", () -> new CrockPotSlowItemFood(20, 4F, () -> new EffectInstance(Effects.INSTANT_HEALTH, 20)));
    public static RegistryObject<Item> fishSticks = ITEMS.register("fish_sticks", () -> new CrockPotBaseItemFood(8, 9.6F, () -> new EffectInstance(Effects.INSTANT_HEALTH, 20, 1)));
    public static RegistryObject<Item> fruitMedley = ITEMS.register("fruit_medley", () -> new CrockPotBaseItemFood(8, 9.6F, () -> new EffectInstance(Effects.HASTE, 3 * 60 * 20)));
    public static RegistryObject<Item> honeyHam = ITEMS.register("honey_ham", () -> new CrockPotBaseItemFood(12, 14.4F, () -> new EffectInstance(Effects.REGENERATION, 30 * 20)));
    public static RegistryObject<Item> honeyNuggets = ITEMS.register("honey_nuggets", () -> new CrockPotBaseItemFood(6, 3.6F, () -> new EffectInstance(Effects.REGENERATION, 5 * 20)));
    public static RegistryObject<Item> hotChili = ITEMS.register("hot_chili", () -> new CrockPotBaseItemFood(7, 8.4F, () -> new EffectInstance(Effects.HASTE, 90 * 20, 1)));
    public static RegistryObject<Item> iceCream = ITEMS.register("ice_cream", IceCream::new);
    public static RegistryObject<Item> jammyPreserves = ITEMS.register("jammy_preserves", () -> new CrockPotBaseItemFood(6, 3.6F));
    public static RegistryObject<Item> kabobs = ITEMS.register("kabobs", () -> new CrockPotBaseItemFood(7, 4.2F));
    public static RegistryObject<Item> meatBalls = ITEMS.register("meat_balls", () -> new CrockPotBaseItemFood(10, 6.0F));
    public static RegistryObject<Item> monsterLasagna = ITEMS.register("monster_lasagna", MonsterLasagna::new);
    public static RegistryObject<Item> perogies = ITEMS.register("perogies", () -> new CrockPotBaseItemFood(8, 12.8F, () -> new EffectInstance(Effects.REGENERATION, 10 * 20, 1)));
    public static RegistryObject<Item> potatoTornado = ITEMS.register("potato_tornado", () -> new CrockPotFastItemFood(7, 8.4F));
    public static RegistryObject<Item> pumpkinCookie = ITEMS.register("pumpkin_cookie", PumpkinCookie::new);
    public static RegistryObject<Item> ratatouille = ITEMS.register("ratatouille", () -> new CrockPotFastItemFood(6, 7.2F));
    public static RegistryObject<Item> taffy = ITEMS.register("taffy", Taffy::new);
    public static RegistryObject<Item> turkeyDinner = ITEMS.register("turkey_dinner", () -> new CrockPotBaseItemFood(12, 19.2F, () -> new EffectInstance(Effects.RESISTANCE, 60 * 20)));
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
