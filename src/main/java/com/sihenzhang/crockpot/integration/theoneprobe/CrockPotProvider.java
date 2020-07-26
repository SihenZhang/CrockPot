package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.tile.CrockPotTileEntity;
import mcjty.theoneprobe.api.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.StringTextComponent;
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
            if (crockPotTileEntity.isProcessing()) {
                // Draw Progress
                float progress = crockPotTileEntity.getProcessTimeProgress();
                if (progress != 0F) {
                    iProbeInfo.progress((int) (progress * 100), 100, iProbeInfo.defaultProgressStyle().suffix("%"));
                }
                // Draw Output
                ItemStack output = crockPotTileEntity.getCurrentRecipe().getResult();
                if (probeMode == ProbeMode.EXTENDED && !output.isEmpty()) {
                    iProbeInfo.text(new StringTextComponent("Output: "))
                            .horizontal(iProbeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(output);
                }
            }
        }
    }
}
