package com.sihenzhang.crockpot.client.model.geom;

import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.client.model.geom.ModelLayerLocation;

public final class CrockPotModelLayers {
    public static final ModelLayerLocation MILKMADE_HAT = createLocation("milkmade_hat");

    private CrockPotModelLayers() {
    }

    private static ModelLayerLocation createLocation(String path, String model) {
        return new ModelLayerLocation(IdUtil.mod(path), model);
    }

    private static ModelLayerLocation createLocation(String path) {
        return createLocation(path, "main");
    }
}
