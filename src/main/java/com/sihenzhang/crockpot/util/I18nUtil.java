package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public final class I18nUtil {
    private I18nUtil() {
    }

    public static MutableComponent of(String prefix, String suffix) {
        return I18nUtil.of(prefix, CrockPot.MOD_ID, suffix);
    }

    public static MutableComponent of(String prefix, String suffix, Object... args) {
        return I18nUtil.of(prefix, CrockPot.MOD_ID, suffix, args);
    }

    public static MutableComponent of(String prefix, String modId, String suffix) {
        return Component.translatable(prefix + "." + modId + "." + suffix);
    }

    public static MutableComponent of(String prefix, String modId, String suffix, Object... args) {
        return Component.translatable(prefix + "." + modId + "." + suffix, args);
    }

    public static MutableComponent tooltip(String suffix) {
        return I18nUtil.of("tooltip", suffix);
    }

    public static MutableComponent tooltip(String suffix, Object... args) {
        return I18nUtil.of("tooltip", suffix, args);
    }

    public static MutableComponent integration(String modId, String suffix) {
        return I18nUtil.of("integration", modId + "." + suffix);
    }

    public static MutableComponent integration(String modId, String suffix, Object... args) {
        return I18nUtil.of("integration", modId + "." + suffix, args);
    }
}
