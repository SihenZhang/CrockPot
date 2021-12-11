package com.sihenzhang.crockpot.levelgen;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.block.AbstractCrockPotDoubleCropBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Random;

public class CrockPotCropsFeature extends Feature<CrockPotCropsFeatureConfig> {
    public CrockPotCropsFeature(Codec<CrockPotCropsFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<CrockPotCropsFeatureConfig> context) {
        WorldGenLevel level = context.level();
        Random rand = context.random();
        CrockPotCropsFeatureConfig config = context.config();
        BlockPos pos = context.origin();

        boolean any = false;
        int age = rand.nextInt(8);
        int dist = Mth.clamp(config.spreadRadius, 1, 8);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int i = 0; i < config.tryCount && !any; i++) {
            int x = pos.getX() + Mth.nextInt(rand, -8, 8);
            int z = pos.getZ() + Mth.nextInt(rand, -8, 8);
            int y = level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z);
            mutableBlockPos.set(x, y, z);
            BlockPos posDown = mutableBlockPos.below();
            BlockState stateDown = level.getBlockState(posDown);
            if (level.isEmptyBlock(mutableBlockPos) && (config.whitelist.isEmpty() || config.whitelist.contains(stateDown.getBlock()))) {
                level.setBlock(posDown, config.replacementBlock.defaultBlockState(), 2);
                if (config.cropsBlock instanceof AbstractCrockPotDoubleCropBlock && age > 3) {
                    level.setBlock(mutableBlockPos, config.cropsBlock.getStateForAge(3), 2);
                    level.setBlock(mutableBlockPos.above(), config.cropsBlock.getStateForAge(age), 2);
                } else {
                    level.setBlock(mutableBlockPos, config.cropsBlock.getStateForAge(age), 2);
                }
                any = true;
            }
        }
        return any;
    }
}
