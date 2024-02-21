package com.sihenzhang.crockpot;

import net.minecraftforge.common.ForgeConfigSpec;

public final class CrockPotConfigs {
    public static final ForgeConfigSpec COMMON_CONFIG;
    public static final ForgeConfigSpec CLIENT_CONFIG;

    public static final ForgeConfigSpec.DoubleValue CROCK_POT_SPEED_MODIFIER;

    public static final ForgeConfigSpec.BooleanValue SHOW_FOOD_VALUES_TOOLTIP;
    public static final ForgeConfigSpec.BooleanValue SHOW_FOOD_EFFECTS_TOOLTIP;
    public static final ForgeConfigSpec.BooleanValue GNAWS_GIFT_HUNGER_OVERLAY;

    static {
        var commonBuilder = new ForgeConfigSpec.Builder();

        commonBuilder.comment("General settings").push("general");
        CROCK_POT_SPEED_MODIFIER = commonBuilder
                .comment("Set this value to change Crock Pot speed modifier. Higher tier Crock Pot will cook faster.\nactualCookingTime = cookingTime * (1.0 - crockPotSpeedModifier * potLevel)")
                .worldRestart()
                .defineInRange("crockPotSpeedModifier", 0.15, 0.0, 1.0);
        commonBuilder.pop();

        COMMON_CONFIG = commonBuilder.build();

        var clientBuilder = new ForgeConfigSpec.Builder();

        clientBuilder.comment("Client settings").push("client");
        SHOW_FOOD_VALUES_TOOLTIP = clientBuilder
                .comment("Set this to false will disable the food values tooltip.")
                .define("showFoodValuesTooltip", true);
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
