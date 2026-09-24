package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class CrockPotBlockEntities {
    private CrockPotBlockEntities() {
    }

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CrockPot.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrockPotBlockEntity>> CROCK_POT_BLOCK_ENTITY = BLOCK_ENTITIES.register("crock_pot", () -> BlockEntityType.Builder.of(CrockPotBlockEntity::new, CrockPotBlocks.CROCK_POT.get(), CrockPotBlocks.PORTABLE_CROCK_POT.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BirdcageBlockEntity>> BIRDCAGE_BLOCK_ENTITY = BLOCK_ENTITIES.register("birdcage", () -> BlockEntityType.Builder.of(BirdcageBlockEntity::new, CrockPotBlocks.BIRDCAGE.get()).build(null));

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, CROCK_POT_BLOCK_ENTITY.get(), CrockPotBlockEntity::getItemHandler);
    }
}
