package com.sihenzhang.crockpot;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class Config {
    public static final ModConfigSpec COMMON_SPEC;
    public static final ModConfigSpec CLIENT_SPEC;

    public static final ModConfigSpec.DoubleValue CROCK_POT_SPEED_MODIFIER;

    public static final ModConfigSpec.BooleanValue SHOW_FOOD_VALUES_TOOLTIP;
    public static final ModConfigSpec.BooleanValue SHOW_FOOD_EFFECTS_TOOLTIP;
    public static final ModConfigSpec.BooleanValue GNAWS_GIFT_HUNGER_OVERLAY;

    private Config() {
    }

    static {
        var commonBuilder = new ModConfigSpec.Builder();
        commonBuilder.comment("General settings").push("general");
        CROCK_POT_SPEED_MODIFIER = commonBuilder
                .comment("Set this value to change Crock Pot speed modifier. Higher tier Crock Pot will cook faster.")
                .worldRestart()
                .defineInRange("crockPotSpeedModifier", 0.15, 0.0, 1.0);
        commonBuilder.pop();
        COMMON_SPEC = commonBuilder.build();

        var clientBuilder = new ModConfigSpec.Builder();
        clientBuilder.comment("Client settings").push("client");
        SHOW_FOOD_VALUES_TOOLTIP = clientBuilder
                .comment("Set this to false to disable the food values tooltip.")
                .define("showFoodValuesTooltip", true);
        SHOW_FOOD_EFFECTS_TOOLTIP = clientBuilder
                .comment("Set this to false to disable the food effect tooltip.")
                .define("showFoodEffectsTooltip", true);
        GNAWS_GIFT_HUNGER_OVERLAY = clientBuilder
                .comment("Set this to false to disable the special hunger bar overlay when the player has Gnaw's Gift effect.")
                .define("gnawsGiftHungerOverlay", true);
        clientBuilder.pop();
        CLIENT_SPEC = clientBuilder.build();
    }
}
