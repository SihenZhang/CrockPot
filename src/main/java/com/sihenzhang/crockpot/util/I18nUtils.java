package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.network.chat.TranslatableComponent;

public final class I18nUtils {
    private I18nUtils() {
    }

    public static TranslatableComponent createComponent(String prefix, String suffix) {
        return I18nUtils.createComponent(prefix, CrockPot.MOD_ID, suffix);
    }

    public static TranslatableComponent createComponent(String prefix, String suffix, Object... args) {
        return I18nUtils.createComponent(prefix, CrockPot.MOD_ID, suffix, args);
    }

    public static TranslatableComponent createComponent(String prefix, String modId, String suffix) {
        return new TranslatableComponent(prefix + "." + modId + "." + suffix);
    }

    public static TranslatableComponent createComponent(String prefix, String modId, String suffix, Object... args) {
        return new TranslatableComponent(prefix + "." + modId + "." + suffix, args);
    }

    public static TranslatableComponent createIntegrationComponent(String modId, String suffix) {
        return I18nUtils.createComponent("integration", CrockPot.MOD_ID, modId + "." + suffix);
    }

    public static TranslatableComponent createIntegrationComponent(String modId, String suffix, Object... args) {
        return I18nUtils.createComponent("integration", CrockPot.MOD_ID, modId + "." + suffix, args);
    }
}
