package com.sihenzhang.crockpot.world;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.block.CrockPotDoubleCropsBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
public class CrockPotCropsFeature extends Feature<CrockPotCropsFeatureConfig> {
    public CrockPotCropsFeature(Codec<CrockPotCropsFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, CrockPotCropsFeatureConfig config) {
        int age = rand.nextInt(8);
        int dist = MathHelper.clamp(config.spreadRadius, 1, 8);
        boolean any = false;
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        for (int i = 0; i < config.tryCount && !any; i++) {
            int x = pos.getX() + MathHelper.nextInt(rand, -8, 8);
            int z = pos.getZ() + MathHelper.nextInt(rand, -8, 8);
            int y = reader.getHeight(Heightmap.Type.WORLD_SURFACE_WG, x, z);
            mutableBlockPos.setPos(x, y, z);
            BlockPos posDown = mutableBlockPos.down();
            BlockState stateDown = reader.getBlockState(posDown);
            if (reader.isAirBlock(mutableBlockPos) && (config.whitelist.isEmpty() || config.whitelist.contains(stateDown.getBlock()))) {
                reader.setBlockState(posDown, config.replacementBlock.getDefaultState(), 2);
                if (config.cropsBlock instanceof CrockPotDoubleCropsBlock && age > 3) {
                    reader.setBlockState(mutableBlockPos, config.cropsBlock.withAge(3), 2);
                    reader.setBlockState(mutableBlockPos.up(), config.cropsBlock.withAge(age), 2);
                } else {
                    reader.setBlockState(mutableBlockPos, config.cropsBlock.withAge(age), 2);
                }
                any = true;
            }
        }
        return any;
    }
}
