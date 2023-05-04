package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nonnull;

public final class CrockPotBlocks {
    private CrockPotBlocks() {
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CrockPot.MOD_ID);

    public static final RegistryObject<Block> BASIC_CROCK_POT = BLOCKS.register("crock_pot_basic", () -> new CrockPotBlock(0));
    public static final RegistryObject<Block> ADVANCED_CROCK_POT = BLOCKS.register("crock_pot_advanced", () -> new CrockPotBlock(1));
    public static final RegistryObject<Block> ULTIMATE_CROCK_POT = BLOCKS.register("crock_pot_ultimate", () -> new CrockPotBlock(2));

    public static final RegistryObject<Block> BIRDCAGE = BLOCKS.register("birdcage", BirdcageBlock::new);

    public static final RegistryObject<Block> UNKNOWN_CROPS = BLOCKS.register("unknown_crops", UnknownCropsBlock::new);
    public static final RegistryObject<Block> ASPARAGUS = BLOCKS.register("asparaguses", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.ASPARAGUS_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> CORN = BLOCKS.register("corns", CornBlock::new);
    public static final RegistryObject<Block> EGGPLANT = BLOCKS.register("eggplants", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.EGGPLANT_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> GARLIC = BLOCKS.register("garlics", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.GARLIC_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> ONION = BLOCKS.register("onions", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.ONION_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> PEPPER = BLOCKS.register("peppers", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.PEPPER_SEEDS.get();
        }
    });
    public static final RegistryObject<Block> TOMATO = BLOCKS.register("tomatoes", () -> new AbstractCrockPotCropBlock() {
        @Nonnull
        @Override
        protected ItemLike getBaseSeedId() {
            return CrockPotItems.TOMATO_SEEDS.get();
        }
    });
}
