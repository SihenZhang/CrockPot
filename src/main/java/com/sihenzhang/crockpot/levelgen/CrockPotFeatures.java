package com.sihenzhang.crockpot.levelgen;

import com.google.common.collect.ImmutableSet;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class CrockPotFeatures {
    public static final CrockPotCropsFeatureConfig ASPARAGUS_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((AbstractCrockPotCropBlock) CrockPotRegistry.asparagusBlock).build();
    public static final CrockPotCropsFeatureConfig CORN_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((AbstractCrockPotCropBlock) CrockPotRegistry.cornBlock).build();
    public static final CrockPotCropsFeatureConfig EGGPLANT_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((AbstractCrockPotCropBlock) CrockPotRegistry.eggplantBlock).build();
    public static final CrockPotCropsFeatureConfig ONION_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((AbstractCrockPotCropBlock) CrockPotRegistry.onionBlock).build();
    public static final CrockPotCropsFeatureConfig PEPPER_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((AbstractCrockPotCropBlock) CrockPotRegistry.pepperBlock).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT)).build();
    public static final CrockPotCropsFeatureConfig TOMATO_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((AbstractCrockPotCropBlock) CrockPotRegistry.tomatoBlock).build();

    public static final PlacedFeature PATCH_ASPARAGUS = PlacementUtils.register("patch_asparagus", CrockPotRegistry.cropsPatchFeature.configured(ASPARAGUS_PATCH_CONFIG).placed(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.ASPARAGUS_GENERATION_CHANCE.get())));
    public static final PlacedFeature PATCH_CORN = PlacementUtils.register("patch_corn", CrockPotRegistry.cropsPatchFeature.configured(CORN_PATCH_CONFIG).placed(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.CORN_GENERATION_CHANCE.get())));
    public static final PlacedFeature PATCH_EGGPLANT = PlacementUtils.register("patch_eggplant", CrockPotRegistry.cropsPatchFeature.configured(EGGPLANT_PATCH_CONFIG).placed(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.EGGPLANT_GENERATION_CHANCE.get())));
    public static final PlacedFeature PATCH_ONION = PlacementUtils.register("patch_onion", CrockPotRegistry.cropsPatchFeature.configured(ONION_PATCH_CONFIG).placed(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.ONION_GENERATION_CHANCE.get())));
    public static final PlacedFeature PATCH_PEPPER = PlacementUtils.register("patch_pepper", CrockPotRegistry.cropsPatchFeature.configured(PEPPER_PATCH_CONFIG).placed(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.PEPPER_GENERATION_CHANCE.get())));
    public static final PlacedFeature PATCH_TOMATO = PlacementUtils.register("patch_tomato", CrockPotRegistry.cropsPatchFeature.configured(TOMATO_PATCH_CONFIG).placed(PlacementUtils.HEIGHTMAP_WORLD_SURFACE, RarityFilter.onAverageOnceEvery(CrockPotConfig.TOMATO_GENERATION_CHANCE.get())));

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
