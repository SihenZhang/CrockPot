package com.sihenzhang.crockpot.registry;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.container.CrockPotContainer;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import net.minecraft.block.Block;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
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
            new BlockItem(crockPotBlock.get(), new Item.Properties().group(ItemGroup.DECORATIONS)));
    public static RegistryObject<TileEntityType<CrockPotTileEntity>> crockPotTileEntity = TILES.register("crock_pot",
            () -> TileEntityType.Builder.create(CrockPotTileEntity::new, CrockPotRegistry.crockPotBlock.get()).build(null));
    public static RegistryObject<ContainerType<CrockPotContainer>> crockPotContainer = CONTAINERS.register("crock_pot",
            () -> IForgeContainerType.create((windowId, inv, data) -> {
                BlockPos pos = data.readBlockPos();
                TileEntity tileEntity = inv.player.world.getTileEntity(pos);
                return new CrockPotContainer(windowId, inv, (CrockPotTileEntity) Objects.requireNonNull(tileEntity));
            }));
}
