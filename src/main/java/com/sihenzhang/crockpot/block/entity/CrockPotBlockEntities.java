package com.sihenzhang.crockpot.block.entity;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrockPotBlockEntities {
    private CrockPotBlockEntities() {
    }

    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CrockPot.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrockPotBlockEntity>> CROCK_POT_BLOCK_ENTITY = BLOCK_ENTITIES.register("crock_pot", () -> BlockEntityType.Builder.of(CrockPotBlockEntity::new, CrockPotBlocks.BASIC_CROCK_POT.get(), CrockPotBlocks.ADVANCED_CROCK_POT.get(), CrockPotBlocks.ULTIMATE_CROCK_POT.get()).build(null));
    public static final RegistryObject<BlockEntityType<BirdcageBlockEntity>> BIRDCAGE_BLOCK_ENTITY = BLOCK_ENTITIES.register("birdcage", () -> BlockEntityType.Builder.of(BirdcageBlockEntity::new, CrockPotBlocks.BIRDCAGE.get()).build(null));
}
