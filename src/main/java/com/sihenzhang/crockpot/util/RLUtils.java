package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.resources.ResourceLocation;

public final class RLUtils {
    private RLUtils() {
    }

    public static ResourceLocation createRL(String namespace, String path) {
        return ResourceLocation.fromNamespaceAndPath(namespace, path);
    }

    public static ResourceLocation createRL(String path) {
        return ResourceLocation.fromNamespaceAndPath(CrockPot.MOD_ID, path);
    }

    public static ResourceLocation createForgeRL(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }

    public static ResourceLocation createVanillaRL(String path) {
        return ResourceLocation.fromNamespaceAndPath("minecraft", path);
    }
}
