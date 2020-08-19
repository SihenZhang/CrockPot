package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class CrockPotProvider implements IProbeInfoProvider {
    @Override
    public String getID() {
        return CrockPot.MOD_ID + ":crock_pot";
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
        TileEntity tileEntity = world.getTileEntity(data.getPos());
        if (tileEntity instanceof CrockPotTileEntity) {
            CrockPotTileEntity crockPotTileEntity = (CrockPotTileEntity) tileEntity;
            if (crockPotTileEntity.isProcessing()) {
                // Draw Progress
                float progress = crockPotTileEntity.getProcessTimeProgress();
                if (progress != 0F) {
                    probeInfo.progress((int) (progress * 100), 100, probeInfo.defaultProgressStyle().suffix("%"));
                }
            }
        }
    }
}
