package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.DeferredHolder;

public final class CrockPotBlockEntities {
    private CrockPotBlockEntities() {
    }

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CrockPot.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrockPotBlockEntity>> CROCK_POT_BLOCK_ENTITY = BLOCK_ENTITIES.register("crock_pot", () -> new BlockEntityType<>(CrockPotBlockEntity::new, ModBlocks.CROCK_POT.get(), ModBlocks.PORTABLE_CROCK_POT.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BirdcageBlockEntity>> BIRDCAGE_BLOCK_ENTITY = BLOCK_ENTITIES.register("birdcage", () -> new BlockEntityType<>(BirdcageBlockEntity::new, ModBlocks.BIRDCAGE.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingRackBlockEntity>> DRYING_RACK_BLOCK_ENTITY = BLOCK_ENTITIES.register("drying_rack", () -> new BlockEntityType<>(DryingRackBlockEntity::new, ModBlocks.DRYING_RACK.get()));

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.Item.BLOCK, CROCK_POT_BLOCK_ENTITY.get(), CrockPotBlockEntity::getItemHandlerForSide);
    }
}
