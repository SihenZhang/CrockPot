package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcjty.theoneprobe.api.*;
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
    public void addProbeInfo(ProbeMode probeMode, IProbeInfo iProbeInfo, PlayerEntity playerEntity, World world, BlockState blockState, IProbeHitData iProbeHitData) {
        TileEntity tileEntity = world.getTileEntity(iProbeHitData.getPos());
        if (tileEntity instanceof CrockPotTileEntity) {
            CrockPotTileEntity crockPotTileEntity = (CrockPotTileEntity) tileEntity;
            // Draw Progress
            int progress = (int) (crockPotTileEntity.getProcessTimeProgress() * 100);
            if (progress != 0F) {
                iProbeInfo.progress(progress, 100, iProbeInfo.defaultProgressStyle().suffix("%"));
            }
        }
    }
}
