package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ModBlockEntities {
    private ModBlockEntities() {
    }

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, CrockPot.MOD_ID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrockPotBlockEntity>> CROCK_POT_BLOCK_ENTITY = BLOCK_ENTITIES.register("crock_pot", () -> BlockEntityType.Builder.of(CrockPotBlockEntity::new, ModBlocks.CROCK_POT.get(), ModBlocks.PORTABLE_CROCK_POT.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BirdcageBlockEntity>> BIRDCAGE_BLOCK_ENTITY = BLOCK_ENTITIES.register("birdcage", () -> BlockEntityType.Builder.of(BirdcageBlockEntity::new, ModBlocks.BIRDCAGE.get()).build(null));
}
