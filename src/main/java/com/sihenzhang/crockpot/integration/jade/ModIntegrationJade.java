package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.BirdcageBlock;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.WailaPlugin;

@WailaPlugin
public class ModIntegrationJade implements IWailaPlugin {
    public static final ResourceLocation CROCK_POT = RLUtils.createRL("crock_pot");
    public static final ResourceLocation BIRDCAGE = RLUtils.createRL("birdcage");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CrockPotProvider.INSTANCE, CrockPotBlockEntity.class);
        registration.registerBlockDataProvider(BirdcageProvider.INSTANCE, BirdcageBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(CROCK_POT, true);
        registration.addConfig(BIRDCAGE, true);
        registration.registerBlockComponent(CrockPotProvider.INSTANCE, CrockPotBlock.class);
        registration.registerBlockComponent(BirdcageProvider.INSTANCE, BirdcageBlock.class);
    }
}
