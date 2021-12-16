package com.sihenzhang.crockpot;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public final class CrockPotConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue SPAWN_WITH_BOOK;
    public static ForgeConfigSpec.DoubleValue CROCK_POT_SPEED_MODIFIER;
    public static ForgeConfigSpec.BooleanValue ENABLE_UNKNOWN_SEEDS;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> UNKNOWN_SEEDS_CROPS_LIST;
    public static final List<String> DEFAULT_CROPS_LIST = Arrays.asList(
            "crockpot:asparagus_seeds", "crockpot:corn_seeds", "crockpot:eggplant_seeds", "crockpot:onion_seeds",
            "crockpot:pepper_seeds", "crockpot:tomato_seeds"
    );

    public static ForgeConfigSpec.BooleanValue ENABLE_WORLD_GENERATION;
    public static ForgeConfigSpec.BooleanValue ASPARAGUS_GENERATION;
    public static ForgeConfigSpec.IntValue ASPARAGUS_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue CORN_GENERATION;
    public static ForgeConfigSpec.IntValue CORN_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue EGGPLANT_GENERATION;
    public static ForgeConfigSpec.IntValue EGGPLANT_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue ONION_GENERATION;
    public static ForgeConfigSpec.IntValue ONION_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue PEPPER_GENERATION;
    public static ForgeConfigSpec.IntValue PEPPER_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue TOMATO_GENERATION;
    public static ForgeConfigSpec.IntValue TOMATO_GENERATION_CHANCE;

    public static ForgeConfigSpec.BooleanValue SHOW_FOOD_EFFECTS_TOOLTIP;
    public static ForgeConfigSpec.BooleanValue GNAWS_GIFT_HUNGER_OVERLAY;

    static {
        ForgeConfigSpec.Builder commonBuilder = new ForgeConfigSpec.Builder();

        commonBuilder.comment("General settings").push("general");
        SPAWN_WITH_BOOK = commonBuilder
                .comment("Set this to false to disable new players spawning with the Crock Pot Cookbook.")
                .worldRestart()
                .define("spawnWithBook", true);
        CROCK_POT_SPEED_MODIFIER = commonBuilder
                .comment("Set this value to change Crock Pot speed modifier. Higher tier Crock Pot will cook faster.\nactualCookingTime = cookingTime * (1.0 - crockPotSpeedModifier * potLevel)")
                .worldRestart()
                .defineInRange("crockPotSpeedModifier", 0.15, 0.0, 1.0);
        ENABLE_UNKNOWN_SEEDS = commonBuilder
                .comment("Set this to false to disable Unknown Seeds.\nThis is a way to obtain crops in the mod and we strongly recommend that set it to true.\nPlease make sure there are other ways to obtain crops in the mod if set it to false.")
                .worldRestart()
                .define("enableUnknownSeeds", true);
        UNKNOWN_SEEDS_CROPS_LIST = commonBuilder
                .comment("Define the crops list that Unknown Crops will be converted into.\nBoth the seed item for the crop and the crop block itself are acceptable.")
                .worldRestart()
                .defineList("unknownCropsList", DEFAULT_CROPS_LIST, o -> o instanceof String && ResourceLocation.tryParse((String) o) != null);
        commonBuilder.pop();

        commonBuilder.comment("World generation settings").push("worldgen");
        ENABLE_WORLD_GENERATION = commonBuilder
                .comment("Set this to false will disable all world generation, even if specific world generation is enabled.\nThis is another way to obtain crops in the mod.\nPlease make sure there are other ways to obtain crops in the mod if set it to false.")
                .worldRestart()
                .define("enableWorldGeneration", false);
        ASPARAGUS_GENERATION = commonBuilder
                .comment("Set this to false will disable asparagus world generation.")
                .worldRestart()
                .define("asparagusGeneration", true);
        ASPARAGUS_GENERATION_CHANCE = commonBuilder
                .comment("Set this value to change the chance of asparagus world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("asparagusGenerationChance", 16, 1, Integer.MAX_VALUE);
        CORN_GENERATION = commonBuilder
                .comment("Set this to false will disable corn world generation.")
                .worldRestart()
                .define("cornGeneration", true);
        CORN_GENERATION_CHANCE = commonBuilder
                .comment("Set this value to change the chance of corn world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("cornGenerationChance", 16, 1, Integer.MAX_VALUE);
        EGGPLANT_GENERATION = commonBuilder
                .comment("Set this to false will disable eggplant world generation.")
                .worldRestart()
                .define("eggplantGeneration", true);
        EGGPLANT_GENERATION_CHANCE = commonBuilder
                .comment("Set this value to change the chance of eggplant world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("eggplantGenerationChance", 16, 1, Integer.MAX_VALUE);
        ONION_GENERATION = commonBuilder
                .comment("Set this to false will disable onion world generation.")
                .worldRestart()
                .define("onionGeneration", true);
        ONION_GENERATION_CHANCE = commonBuilder
                .comment("Set this value to change the chance of onion world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("onionGenerationChance", 16, 1, Integer.MAX_VALUE);
        PEPPER_GENERATION = commonBuilder
                .comment("Set this to false will disable pepper world generation.")
                .worldRestart()
                .define("pepperGeneration", true);
        PEPPER_GENERATION_CHANCE = commonBuilder
                .comment("Set this value to change the chance of pepper world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("pepperGenerationChance", 16, 1, Integer.MAX_VALUE);
        TOMATO_GENERATION = commonBuilder
                .comment("Set this to false will disable tomato world generation.")
                .worldRestart()
                .define("tomatoGeneration", true);
        TOMATO_GENERATION_CHANCE = commonBuilder
                .comment("Set this value to change the chance of tomato world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("tomatoGenerationChance", 16, 1, Integer.MAX_VALUE);
        commonBuilder.pop();

        COMMON_CONFIG = commonBuilder.build();

        ForgeConfigSpec.Builder clientBuilder = new ForgeConfigSpec.Builder();

        clientBuilder.comment("Client settings").push("client");
        SHOW_FOOD_EFFECTS_TOOLTIP = clientBuilder
                .comment("Set this to false will disable the food effect tooltip.")
                .define("showFoodEffectsTooltip", true);
        GNAWS_GIFT_HUNGER_OVERLAY = clientBuilder
                .comment("Set this to false will disable the special hunger bar overlay when the player has Gnaw's Gift effect.")
                .define("gnawsGiftHungerOverlay", true);
        clientBuilder.pop();

        CLIENT_CONFIG = clientBuilder.build();
    }
}
