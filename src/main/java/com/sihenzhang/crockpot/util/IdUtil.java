package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.resources.Identifier;

public final class IdUtil {
    private IdUtil() {
    }

    public static Identifier of(String namespace, String path) {
        return Identifier.fromNamespaceAndPath(namespace, path);
    }

    public static Identifier mod(String path) {
        return Identifier.fromNamespaceAndPath(CrockPot.MOD_ID, path);
    }

    public static Identifier forge(String path) {
        return Identifier.fromNamespaceAndPath("forge", path);
    }

    public static Identifier neoforge(String path) {
        return Identifier.fromNamespaceAndPath("neoforge", path);
    }

    public static Identifier common(String path) {
        return Identifier.fromNamespaceAndPath("c", path);
    }

    public static Identifier mc(String path) {
        return Identifier.fromNamespaceAndPath("minecraft", path);
    }
}
