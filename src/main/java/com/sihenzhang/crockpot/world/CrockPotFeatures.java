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
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class CrockPotFeatures {
    public static final CrockPotCropsFeatureConfig ASPARAGUS_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.asparagusBlock).build();
    public static final CrockPotCropsFeatureConfig CORN_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.cornBlock).build();
    public static final CrockPotCropsFeatureConfig EGGPLANT_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.eggplantBlock).build();
    public static final CrockPotCropsFeatureConfig ONION_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.onionBlock).build();
    public static final CrockPotCropsFeatureConfig PEPPER_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.pepperBlock).whitelist(ImmutableSet.of(Blocks.GRASS_BLOCK, Blocks.COARSE_DIRT)).build();
    public static final CrockPotCropsFeatureConfig TOMATO_PATCH_CONFIG = CrockPotCropsFeatureConfig.builder((CrockPotCropsBlock) CrockPotRegistry.tomatoBlock).build();

    public static final ConfiguredFeature<?, ?> PATCH_ASPARAGUS = register("patch_asparagus", CrockPotRegistry.cropsPatchFeature.configured(ASPARAGUS_PATCH_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(CrockPotConfig.ASPARAGUS_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_CORN = register("patch_corn", CrockPotRegistry.cropsPatchFeature.configured(CORN_PATCH_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(CrockPotConfig.CORN_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_EGGPLANT = register("patch_eggplant", CrockPotRegistry.cropsPatchFeature.configured(EGGPLANT_PATCH_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(CrockPotConfig.EGGPLANT_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_ONION = register("patch_onion", CrockPotRegistry.cropsPatchFeature.configured(ONION_PATCH_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(CrockPotConfig.ONION_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_PEPPER = register("patch_pepper", CrockPotRegistry.cropsPatchFeature.configured(PEPPER_PATCH_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(CrockPotConfig.PEPPER_GENERATION_CHANCE.get()));
    public static final ConfiguredFeature<?, ?> PATCH_TOMATO = register("patch_tomato", CrockPotRegistry.cropsPatchFeature.configured(TOMATO_PATCH_CONFIG).decorated(Features.Placements.HEIGHTMAP_DOUBLE_SQUARE).chance(CrockPotConfig.TOMATO_GENERATION_CHANCE.get()));

    private static <FC extends IFeatureConfig> ConfiguredFeature<FC, ?> register(String key, ConfiguredFeature<FC, ?> configuredFeature) {
        return Registry.register(WorldGenRegistries.CONFIGURED_FEATURE, new ResourceLocation(CrockPot.MOD_ID, key), configuredFeature);
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onBiomeLoading(BiomeLoadingEvent event) {
        if (CrockPotConfig.ENABLE_WORLD_GENERATION.get()) {
            Biome.Category category = event.getCategory();
            BiomeGenerationSettingsBuilder builder = event.getGeneration();

            if (CrockPotConfig.ASPARAGUS_GENERATION.get() && (event.getName().getPath().startsWith("dark_forest") || category == Biome.Category.SWAMP)) {
                builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_ASPARAGUS);
            }
            if (CrockPotConfig.CORN_GENERATION.get() && category == Biome.Category.FOREST) {
                builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_CORN);
            }
            if (CrockPotConfig.EGGPLANT_GENERATION.get() && (category == Biome.Category.PLAINS || category == Biome.Category.FOREST)) {
                builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_EGGPLANT);
            }
            if (CrockPotConfig.ONION_GENERATION.get() && category == Biome.Category.SAVANNA) {
                builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_ONION);
            }
            if (CrockPotConfig.PEPPER_GENERATION.get() && (category == Biome.Category.SAVANNA || category == Biome.Category.MESA)) {
                builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_PEPPER);
            }
            if (CrockPotConfig.TOMATO_GENERATION.get() && category == Biome.Category.PLAINS) {
                builder.addFeature(GenerationStage.Decoration.VEGETAL_DECORATION, PATCH_TOMATO);
            }
        }
    }
}
