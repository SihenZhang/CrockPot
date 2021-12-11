package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.AbstractCrockPotBlock;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.resources.ResourceLocation;

@WailaPlugin
public class ModIntegrationJade implements IWailaPlugin {
    public static final ResourceLocation CROCK_POT = new ResourceLocation(CrockPot.MOD_ID, "crock_pot");

    @Override
    public void register(IRegistrar registrar) {
        registrar.registerComponentProvider(CrockPotProvider.INSTANCE, TooltipPosition.BODY, AbstractCrockPotBlock.class);
        registrar.registerBlockDataProvider(CrockPotProvider.INSTANCE, CrockPotBlockEntity.class);
        registrar.addConfig(CROCK_POT, true);
    }
}
