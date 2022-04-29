package com.sihenzhang.crockpot.levelgen;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class CrockPotFeatures {
    public static final CrockPotCropsFeatureConfig ASPARAGUS_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder(CrockPotRegistry.asparagusBlock.get()).build();
    public static final CrockPotCropsFeatureConfig CORN_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder(CrockPotRegistry.cornBlock.get()).build();
    public static final CrockPotCropsFeatureConfig EGGPLANT_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder(CrockPotRegistry.eggplantBlock.get()).build();
    public static final CrockPotCropsFeatureConfig ONION_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder(CrockPotRegistry.onionBlock.get()).build();
    public static final CrockPotCropsFeatureConfig PEPPER_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder(CrockPotRegistry.pepperBlock.get()).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT)).build();
    public static final CrockPotCropsFeatureConfig TOMATO_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder(CrockPotRegistry.tomatoBlock.get()).build();

    public static Holder<ConfiguredFeature<CrockPotCropsFeatureConfig, ?>> ASPARAGUS_FEATURE = FeatureUtils.register("feature_asparagus", CrockPotRegistry.cropsPatchFeature.get(), ASPARAGUS_PATCH_CONFIG);
    public static Holder<ConfiguredFeature<CrockPotCropsFeatureConfig, ?>> CORN_FEATURE = FeatureUtils.register("feature_corn", CrockPotRegistry.cropsPatchFeature.get(), CORN_PATCH_CONFIG);
    public static Holder<ConfiguredFeature<CrockPotCropsFeatureConfig, ?>> EGGPLANT_FEATURE = FeatureUtils.register("feature_eggplant", CrockPotRegistry.cropsPatchFeature.get(), EGGPLANT_PATCH_CONFIG);
    public static Holder<ConfiguredFeature<CrockPotCropsFeatureConfig, ?>> ONION_FEATURE = FeatureUtils.register("onion", CrockPotRegistry.cropsPatchFeature.get(), ONION_PATCH_CONFIG);
    public static Holder<ConfiguredFeature<CrockPotCropsFeatureConfig, ?>> PEPPER_FEATURE = FeatureUtils.register("pepper_eggplant", CrockPotRegistry.cropsPatchFeature.get(), PEPPER_PATCH_CONFIG);
    public static Holder<ConfiguredFeature<CrockPotCropsFeatureConfig, ?>> TOMATO_FEATURE = FeatureUtils.register("tomato_eggplant", CrockPotRegistry.cropsPatchFeature.get(), TOMATO_PATCH_CONFIG);

    public static final Holder<PlacedFeature> PATCH_ASPARAGUS = PlacementUtils.register("patch_asparagus", ASPARAGUS_FEATURE, PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.ASPARAGUS_GENERATION_CHANCE.get()));
    public static final Holder<PlacedFeature> PATCH_CORN = PlacementUtils.register("patch_corn", CORN_FEATURE, PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.CORN_GENERATION_CHANCE.get()));
    public static final Holder<PlacedFeature> PATCH_EGGPLANT = PlacementUtils.register("patch_eggplant", EGGPLANT_FEATURE, PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.EGGPLANT_GENERATION_CHANCE.get()));
    public static final Holder<PlacedFeature> PATCH_ONION = PlacementUtils.register("patch_onion", ONION_FEATURE, PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.ONION_GENERATION_CHANCE.get()));
    public static final Holder<PlacedFeature> PATCH_PEPPER = PlacementUtils.register("patch_pepper", PEPPER_FEATURE, PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.PEPPER_GENERATION_CHANCE.get()));
    public static final Holder<PlacedFeature> PATCH_TOMATO = PlacementUtils.register("patch_tomato", TOMATO_FEATURE, PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.TOMATO_GENERATION_CHANCE.get()));

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (CrockPotConfig.ENABLE_WORLD_GENERATION.get()) {
            Biome.BiomeCategory category = event.getCategory();
            BiomeGenerationSettingsBuilder builder = event.getGeneration();

            if (CrockPotConfig.ASPARAGUS_GENERATION.get() && (event.getName().getPath().startsWith("dark_forest") || category == Biome.BiomeCategory.SWAMP)) {
                builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_ASPARAGUS);
            }
            if (CrockPotConfig.CORN_GENERATION.get() && category == Biome.BiomeCategory.FOREST) {
                builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_CORN);
            }
            if (CrockPotConfig.EGGPLANT_GENERATION.get() && (category == Biome.BiomeCategory.PLAINS || category == Biome.BiomeCategory.FOREST)) {
                builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_EGGPLANT);
            }
            if (CrockPotConfig.ONION_GENERATION.get() && category == Biome.BiomeCategory.SAVANNA) {
                builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_ONION);
            }
            if (CrockPotConfig.PEPPER_GENERATION.get() && (category == Biome.BiomeCategory.SAVANNA || category == Biome.BiomeCategory.MESA)) {
                builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_PEPPER);
            }
            if (CrockPotConfig.TOMATO_GENERATION.get() && category == Biome.BiomeCategory.PLAINS) {
                builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, PATCH_TOMATO);
            }
        }
    }
}
