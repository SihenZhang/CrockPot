package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class I18nUtils {
    private I18nUtils() {
    }

    public static MutableComponent createComponent(String prefix, String suffix) {
        return I18nUtils.createComponent(prefix, CrockPot.MOD_ID, suffix);
    }

    public static MutableComponent createComponent(String prefix, String suffix, Object... args) {
        return I18nUtils.createComponent(prefix, CrockPot.MOD_ID, suffix, args);
    }

    public static MutableComponent createComponent(String prefix, String modId, String suffix) {
        return Component.translatable(prefix + "." + modId + "." + suffix);
    }

    public static MutableComponent createComponent(String prefix, String modId, String suffix, Object... args) {
        return Component.translatable(prefix + "." + modId + "." + suffix, args);
    }

    public static MutableComponent createTooltipComponent(String suffix) {
        return I18nUtils.createComponent("tooltip", suffix);
    }

    public static MutableComponent createTooltipComponent(String suffix, Object... args) {
        return I18nUtils.createComponent("tooltip", suffix, args);
    }

    public static MutableComponent createIntegrationComponent(String modId, String suffix) {
        return I18nUtils.createComponent("integration", modId + "." + suffix);
    }

    public static MutableComponent createIntegrationComponent(String modId, String suffix, Object... args) {
        return I18nUtils.createComponent("integration", modId + "." + suffix, args);
    }
}
