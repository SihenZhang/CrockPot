package com.sihenzhang.crockpot;

import net.minecraftforge.common.ForgeConfigSpec;

public final class CrockPotConfig {
    public static ForgeConfigSpec COMMON_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;

    public static ForgeConfigSpec.BooleanValue SPAWN_WITH_BOOK;
    public static ForgeConfigSpec.DoubleValue CROCK_POT_SPEED_MODIFIER;

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
