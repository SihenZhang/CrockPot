package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.AbstractCrockPotBlock;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import mcp.mobius.waila.api.*;
import net.minecraft.resources.ResourceLocation;

@WailaPlugin
public class ModIntegrationJade implements IWailaPlugin {
    public static final ResourceLocation CROCK_POT = new ResourceLocation(CrockPot.MOD_ID, "crock_pot");

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CrockPotProvider.INSTANCE, CrockPotBlockEntity.class);
        registration.addConfig(CROCK_POT, true);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerComponentProvider(CrockPotProvider.INSTANCE, TooltipPosition.BODY, AbstractCrockPotBlock.class);
    }
}
