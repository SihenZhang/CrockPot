package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.item.ModItems;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;

public final class ModIntegrationCurios {
    public static final String MOD_ID = "curios";

    private ModIntegrationCurios() {
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerItem(CuriosCapability.ITEM, (stack, context) -> new GnawsCoinCurio(stack), ModItems.GNAWS_COIN.get());
        event.registerItem(CuriosCapability.ITEM, (stack, context) -> new MilkmadeHatCurio(stack),
                ModItems.MILKMADE_HAT.get(), ModItems.CREATIVE_MILKMADE_HAT.get());
    }
}
