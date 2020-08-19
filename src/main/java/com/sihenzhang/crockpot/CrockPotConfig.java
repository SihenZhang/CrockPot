package com.sihenzhang.crockpot;

import net.minecraftforge.common.ForgeConfigSpec;

public final class CrockPotConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec.BooleanValue SPAWN_WITH_BOOK;
    public static ForgeConfigSpec.BooleanValue ASYNC_RECIPE_MATCHING;

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
        builder.pop();
        COMMON_CONFIG = builder.build();
    }
}
