package com.sihenzhang.crockpot.world;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.block.CrockPotCropsBlock;
import net.minecraft.block.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;

public class CrockPotFeatures {
    public static final Feature<CrockPotCropsFeatureConfig> CROCK_POT_CROPS_PATCH = new CrockPotCropsFeature(CrockPotCropsFeatureConfig.CODEC);

    public static final CrockPotCropsFeatureConfig ASPARAGUS_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.asparagusBlock.get()).build();
    public static final CrockPotCropsFeatureConfig CORN_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.cornBlock.get()).build();
    public static final CrockPotCropsFeatureConfig EGGPLANT_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.eggplantBlock.get()).build();
    public static final CrockPotCropsFeatureConfig ONION_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.onionBlock.get()).build();
    public static final CrockPotCropsFeatureConfig PEPPER_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.pepperBlock.get()).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT)).build();
    public static final CrockPotCropsFeatureConfig TOMATO_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.tomatoBlock.get()).build();

    public static final ConfiguredFeature<?, ?> PATCH_ASPARAGUS = register("patch_asparagus", CROCK_POT_CROPS_PATCH.withConfiguration(ASPARAGUS_PATCH_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(CrockPotConfig.ASPARAGUS_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_CORN = register("patch_corn", CROCK_POT_CROPS_PATCH.withConfiguration(CORN_PATCH_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(CrockPotConfig.CORN_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_EGGPLANT = register("patch_eggplant", CROCK_POT_CROPS_PATCH.withConfiguration(EGGPLANT_PATCH_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(CrockPotConfig.EGGPLANT_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_ONION = register("patch_onion", CROCK_POT_CROPS_PATCH.withConfiguration(ONION_PATCH_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(CrockPotConfig.ONION_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_PEPPER = register("patch_pepper", CROCK_POT_CROPS_PATCH.withConfiguration(PEPPER_PATCH_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(CrockPotConfig.PEPPER_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_TOMATO = register("patch_tomato", CROCK_POT_CROPS_PATCH.withConfiguration(TOMATO_PATCH_CONFIG).withPlacement(Features.Placements.PATCH_PLACEMENT).chance(CrockPotConfig.TOMATO_GENERATION_CHANCE.get()));

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CrockPot.MOD_ID, key), configuredFeature);
    }

    public static void registerFeature(RegistryEvent.Register<Feature<?>> event) {
        event.getRegistry().register(CROCK_POT_CROPS_PATCH.setRegistryName(CrockPot.MOD_ID, "crops_patch"));
    }

    public static void onBiomeLoad(BiomeLoadingEvent event) {
        if (CrockPotConfig.ENABLE_WORLD_GENERATION.get()) {
            Biome.Category category = event.getCategory();
            BiomeGenerationSettingsBuilder builder = event.getGeneration();

            if (CrockPotConfig.ASPARAGUS_GENERATION.get() && (event.getName().getPath().startsWith("dark_forest") || category == Biome.Category.SWAMP)) {
                builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_ASPARAGUS);
            }
            if (CrockPotConfig.CORN_GENERATION.get() && category == Biome.Category.FOREST) {
                builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_CORN);
            }
            if (CrockPotConfig.EGGPLANT_GENERATION.get() && (category == Biome.Category.PLAINS || category == Biome.Category.FOREST)) {
                builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_EGGPLANT);
            }
            if (CrockPotConfig.ONION_GENERATION.get() && category == Biome.Category.SAVANNA) {
                builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_ONION);
            }
            if (CrockPotConfig.PEPPER_GENERATION.get() && (category == Biome.Category.SAVANNA || category == Biome.Category.MESA)) {
                builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_PEPPER);
            }
            if (CrockPotConfig.TOMATO_GENERATION.get() && category == Biome.Category.PLAINS) {
                builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_TOMATO);
            }
        }
    }
}
