package com.sihenzhang.crockpot.client.model.geom;

import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class CrockPotModelLayers {
    public static final ModelLayerLocation MILKMADE_HAT = createLocation("milkmade_hat");
    public static final ModelLayerLocation VOLT_GOAT = createLocation("volt_goat");
    public static final ModelLayerLocation VOLT_GOAT_ARMOR = createLocation("volt_goat", "armor");

    private static ModelLayerLocation createLocation(String pPath, String pModel) {
        return new ModelLayerLocation(RLUtils.createRL(pPath), pModel);
    }

    private static ModelLayerLocation createLocation(String pPath) {
        return createLocation(pPath, "main");
    }
}
