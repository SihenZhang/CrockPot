package com.sihenzhang.crockpot.integration.jade;

import com.sihenzhang.crockpot.block.BirdcageBlock;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.block.DryingRackBlock;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.block.entity.DryingRackBlockEntity;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.resources.Identifier;
import snownee.jade.api.BlockAccessor;
import snownee.jade.api.IWailaClientRegistration;
import snownee.jade.api.IWailaCommonRegistration;
import snownee.jade.api.IWailaPlugin;
import snownee.jade.api.JadeIds;
import snownee.jade.api.WailaPlugin;
import snownee.jade.api.config.IWailaConfig;

@WailaPlugin
public class ModIntegrationJade implements IWailaPlugin {
    public static final Identifier CROCK_POT = IdUtil.mod("crock_pot");
    public static final Identifier BIRDCAGE = IdUtil.mod("birdcage");
    public static final Identifier DRYING_RACK = IdUtil.mod("drying_rack");

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(CrockPotProvider.INSTANCE, CrockPotBlockEntity.class);
        // The upper half has no block entity, so register against the block itself.
        registration.registerBlockDataProvider(BirdcageProvider.INSTANCE, BirdcageBlock.class);
        registration.registerBlockDataProvider(DryingRackProvider.INSTANCE, DryingRackBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.addConfig(CROCK_POT, true);
        registration.addConfig(BIRDCAGE, true);
        registration.addConfig(DRYING_RACK, true);
        registration.registerBlockComponent(CrockPotClientProvider.INSTANCE, CrockPotBlock.class);
        registration.registerBlockComponent(BirdcageClientProvider.INSTANCE, BirdcageBlock.class);
        registration.registerBlockComponent(DryingRackClientProvider.INSTANCE, DryingRackBlock.class);
        registration.addTooltipCollectedCallback((box, accessor) -> {
            if (!(accessor instanceof BlockAccessor blockAccessor)) {
                return;
            }
            Identifier uid;
            if (blockAccessor.getBlock() instanceof CrockPotBlock) {
                uid = CROCK_POT;
            } else if (blockAccessor.getBlock() instanceof DryingRackBlock) {
                uid = DRYING_RACK;
            } else {
                return;
            }
            if (IWailaConfig.get().plugin().get(uid)
                    && accessor.getServerData().contains(uid.toString())) {
                // Replace the generic inventory display only when our custom data is available.
                box.getTooltip().remove(JadeIds.UNIVERSAL_ITEM_STORAGE);
            }
        });
    }
}
