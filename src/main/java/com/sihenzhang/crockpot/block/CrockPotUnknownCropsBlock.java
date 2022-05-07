package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.Random;

public class CrockPotUnknownCropsBlock extends AbstractCrockPotCropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    private static final VoxelShape[] SHAPE_BY_AGE = {
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D)
    };
    private static final TagKey<Block> UNKNOWN_CROPS_BLOCK_TAG = BlockTags.create(RLUtils.createRL("unknown_crops"));

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public int getMaxAge() {
        return 1;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        List<Block> unknownCropsBlocks = ForgeRegistries.BLOCKS.tags().getTag(UNKNOWN_CROPS_BLOCK_TAG).stream().toList();
        if (unknownCropsBlocks.isEmpty()) {
            return;
        }
        if (level.getRawBrightness(pos, 0) >= 9) {
            float growthChance = getGrowthSpeed(this, level, pos);
            if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                level.setBlock(pos, unknownCropsBlocks.get(level.random.nextInt(unknownCropsBlocks.size())).defaultBlockState(), 2);
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        List<Block> unknownCropsBlocks = ForgeRegistries.BLOCKS.tags().getTag(UNKNOWN_CROPS_BLOCK_TAG).stream().toList();
        if (unknownCropsBlocks.isEmpty()) {
            return;
        }
        Block block = unknownCropsBlocks.get(level.random.nextInt(unknownCropsBlocks.size()));
        int age = this.getBonemealAgeIncrease(level) - 1;
        if (block instanceof AbstractCrockPotDoubleCropBlock cropBlock) {
            int maxAge = cropBlock.getMaxGrowthAge(cropBlock.defaultBlockState());
            if (age > maxAge) {
                level.setBlock(pos, cropBlock.getStateForAge(maxAge), 2);
                if (level.isEmptyBlock(pos.above())) {
                    level.setBlock(pos.above(), cropBlock.getStateForAge(age), 2);
                }
            } else {
                level.setBlock(pos, cropBlock.getStateForAge(age), 2);
            }
        } else if (block instanceof CropBlock cropBlock) {
            level.setBlock(pos, cropBlock.getStateForAge(Math.min(age, cropBlock.getMaxAge())), 2);
        } else {
            level.setBlock(pos, block.defaultBlockState(), 2);
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CrockPotRegistry.unknownSeeds.get();
    }
}
