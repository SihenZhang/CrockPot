package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.block.BirdcageBlock;
import com.sihenzhang.crockpot.block.entity.BirdcageBlockEntity;
import com.sihenzhang.crockpot.util.RLUtils;
import mcjty.theoneprobe.api.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Function;

public class BirdcageProbeInfoProvider implements IProbeInfoProvider, Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerProvider(this);
        return null;
    }

    @Override
    public ResourceLocation getID() {
        return RLUtils.createRL("birdcage");
    }

    @Override
    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level level, BlockState blockState, IProbeHitData data) {
        if (blockState.getBlock() instanceof BirdcageBlock birdcageBlock) {
            if (birdcageBlock.getBlockEntity(level, data.getPos(), blockState) instanceof BirdcageBlockEntity birdcageBlockEntity) {
                var outputBuffer = birdcageBlockEntity.getOutputBuffer();
                for (var output : outputBuffer) {
                    var remainTime = output.getSecond() - level.getGameTime();
                    var progress = (float) (BirdcageBlockEntity.OUTPUT_COOLDOWN - remainTime) / (float) BirdcageBlockEntity.OUTPUT_COOLDOWN;
                    probeInfo.horizontal(probeInfo.defaultLayoutStyle().alignment(ElementAlignment.ALIGN_CENTER))
                            .item(output.getFirst())
                            .progress((int) (progress * 100), 100, probeInfo.defaultProgressStyle().suffix(Component.literal("%")));
                }
            }
        }
    }
}
