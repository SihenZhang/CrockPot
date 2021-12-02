package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.util.ResourceLocation;

@WailaPlugin
public class ModIntegrationJade implements IWailaPlugin {
    public static final ResourceLocation CROCK_POT = new ResourceLocation(CrockPot.MOD_ID, "crock_pot");

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(CrockPotProvider.INSTANCE, TooltipPosition.BODY, CrockPotBlock.class);
        registrar.registerBlockDataProvider(CrockPotProvider.INSTANCE, CrockPotBlock.class);
        registrar.addConfig(CROCK_POT, true);
    }
}
