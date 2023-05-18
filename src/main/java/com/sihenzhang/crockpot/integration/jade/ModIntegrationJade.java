package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.*;

@WailaPlugin
public class ModIntegrationJade implements IWailaPlugin {
    public static final ResourceLocation CROCK_POT = RLUtils.createRL("crock_pot");

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CrockPotProvider.INSTANCE, CrockPotBlockEntity.class);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(CROCK_POT, true);
        registration.registerBlockComponent(CrockPotProvider.INSTANCE, /*TooltipPosition.BODY,*/ CrockPotBlock.class);
    }
}
