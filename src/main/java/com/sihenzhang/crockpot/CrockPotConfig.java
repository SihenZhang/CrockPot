package com.sihenzhang.crockpot;

import net.minecraftforge.common.ForgeConfigSpec;

public final class CrockPotConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static ForgeConfigSpec.BooleanValue SPAWN_WITH_BOOK;
    public static ForgeConfigSpec.BooleanValue ASYNC_RECIPE_MATCHING;
    public static ForgeConfigSpec.IntValue ASYNC_RECIPE_MATCHING_POOL_SIZE;

    public static ForgeConfigSpec.BooleanValue ASPARAGUS_GENERATION;
    public static ForgeConfigSpec.IntValue ASPARAGUS_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue CORN_GENERATION;
    public static ForgeConfigSpec.IntValue CORN_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue EGGPLANT_GENERATION;
    public static ForgeConfigSpec.IntValue EGGPLANT_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue ONION_GENERATION;
    public static ForgeConfigSpec.IntValue ONION_GENERATION_CHANCE;
    public static ForgeConfigSpec.BooleanValue TOMATO_GENERATION;
    public static ForgeConfigSpec.IntValue TOMATO_GENERATION_CHANCE;

    static {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        builder.comment("General settings").push("general");
        SPAWN_WITH_BOOK = builder
                .comment("Set this to false to disable new players spawning with the Crock Pot Cookbook.")
                .worldRestart()
                .define("spawnWithBook", true);
        ASYNC_RECIPE_MATCHING = builder
                .comment("Set this to false to disable asynchronous crock pot recipe matching.")
                .worldRestart()
                .define("asyncRecipeMatching", true);
        ASYNC_RECIPE_MATCHING_POOL_SIZE = builder
                .comment("Set this value to change the thread pool size of asynchronous crock pot recipe matching.")
                .worldRestart()
                .defineInRange("asyncRecipeMatchingPoolSize", 1, 1, 16);
        builder.pop();

        builder.comment("World generation settings").push("worldgen");
        ASPARAGUS_GENERATION = builder
                .comment("Set this to false will disable asparagus world generation.")
                .worldRestart()
                .define("asparagusGeneration", true);
        ASPARAGUS_GENERATION_CHANCE = builder
                .comment("Set this value to change the chance of asparagus world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("asparagusGenerationChance", 16, 1, Integer.MAX_VALUE);
        CORN_GENERATION = builder
                .comment("Set this to false will disable corn world generation.")
                .worldRestart()
                .define("cornGeneration", true);
        CORN_GENERATION_CHANCE = builder
                .comment("Set this value to change the chance of corn world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("cornGenerationChance", 16, 1, Integer.MAX_VALUE);
        EGGPLANT_GENERATION = builder
                .comment("Set this to false will disable eggplant world generation.")
                .worldRestart()
                .define("eggplantGeneration", true);
        EGGPLANT_GENERATION_CHANCE = builder
                .comment("Set this value to change the chance of eggplant world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("eggplantGenerationChance", 16, 1, Integer.MAX_VALUE);
        ONION_GENERATION = builder
                .comment("Set this to false will disable onion world generation.")
                .worldRestart()
                .define("onionGeneration", true);
        ONION_GENERATION_CHANCE = builder
                .comment("Set this value to change the chance of onion world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("onionGenerationChance", 16, 1, Integer.MAX_VALUE);
        TOMATO_GENERATION = builder
                .comment("Set this to false will disable tomato world generation.")
                .worldRestart()
                .define("tomatoGeneration", true);
        TOMATO_GENERATION_CHANCE = builder
                .comment("Set this value to change the chance of tomato world generation. The higher value, the less generation.")
                .worldRestart()
                .defineInRange("tomatoGenerationChance", 16, 1, Integer.MAX_VALUE);
        builder.pop();

        COMMON_CONFIG = builder.build();
    }
}
