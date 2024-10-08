package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.resources.ResourceLocation;

public final class RLUtils {
    private RLUtils() {
    }

    public static ResourceLocation create(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation mod(String path) {
        return ResourceLocation.fromNamespaceAndPath(CrockPot.MOD_ID, path);
    }

    public static ResourceLocation forge(String path) {
        return ResourceLocation.fromNamespaceAndPath("forge", path);
    }

    public static ResourceLocation neoforge(String path) {
        return ResourceLocation.fromNamespaceAndPath("neoforge", path);
    }

    public static ResourceLocation common(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    public static ResourceLocation vanilla(String path) {
        return ResourceLocation.withDefaultNamespace(path);
    }
}
