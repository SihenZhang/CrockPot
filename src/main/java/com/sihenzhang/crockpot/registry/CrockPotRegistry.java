package com.sihenzhang.crockpot.registry;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.item.CrockPotBaseItemFood;
import com.sihenzhang.crockpot.item.MonsterLasagna;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
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

    public static RegistryObject<Block> crockPotBlock = BLOCKS.register("crock_pot", CrockPotBlock::new);
    public static RegistryObject<Item> crockPotBlockItem = ITEMS.register("crock_pot", () ->
            new BlockItem(crockPotBlock.get(), new Item.Properties().group(CrockPot.ITEM_GROUP)));
    public static RegistryObject<TileEntityType<CrockPotTileEntity>> crockPotTileEntity = TILES.register("crock_pot",
            () -> TileEntityType.Builder.create(CrockPotTileEntity::new, CrockPotRegistry.crockPotBlock.get()).build(null));
    public static RegistryObject<ContainerType<CrockPotContainer>> crockPotContainer = CONTAINERS.register("crock_pot",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity tileEntity = inv.player.world.getTileEntity(pos);
                return new CrockPotContainer(windowId, inv, (CrockPotTileEntity) Objects.requireNonNull(tileEntity));
            }));
    public static RegistryObject<Item> baconEggs = ITEMS.register("bacon_eggs", () -> new CrockPotBaseItemFood(12, 14.4F));
    public static RegistryObject<Item> meatBalls = ITEMS.register("meat_balls", () -> new CrockPotBaseItemFood(10, 6F));
    public static RegistryObject<Item> monsterLasagna = ITEMS.register("monster_lasagna", MonsterLasagna::new);
}
