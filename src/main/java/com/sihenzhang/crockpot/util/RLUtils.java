package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.resources.ResourceLocation;

public class RLUtils {
    public static ResourceLocation createRL(String namespace, String path) {
        return new ResourceLocation(namespace, path);
    }

    public static ResourceLocation createRL(String path) {
        return new ResourceLocation(CrockPot.MOD_ID, path);
    }

    public static ResourceLocation createForgeRL(String path) {
        return new ResourceLocation("forge", path);
    }

    public static ResourceLocation createVanillaRL(String path) {
        return new ResourceLocation("minecraft", path);
    }
}
