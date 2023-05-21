package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
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

public class UnknownCropsBlock extends AbstractCrockPotCropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_1;
    private static final VoxelShape[] SHAPE_BY_AGE = {
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D)
    };

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
    @SuppressWarnings("deprecation")
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        var unknownCropsBlocks = ForgeRegistries.BLOCKS.tags().getTag(CrockPotBlockTags.UNKNOWN_CROPS).stream().toList();
        if (unknownCropsBlocks.isEmpty()) {
            return;
        }
        if (level.getRawBrightness(pos, 0) >= 9) {
            var growthChance = getGrowthSpeed(this, level, pos);
            if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                level.setBlock(pos, unknownCropsBlocks.get(level.random.nextInt(unknownCropsBlocks.size())).defaultBlockState(), Block.UPDATE_CLIENTS);
                ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        var unknownCropsBlocks = ForgeRegistries.BLOCKS.tags().getTag(CrockPotBlockTags.UNKNOWN_CROPS).stream().toList();
        if (unknownCropsBlocks.isEmpty()) {
            return;
        }
        var block = unknownCropsBlocks.get(level.random.nextInt(unknownCropsBlocks.size()));
        var age = this.getBonemealAgeIncrease(level) - 1;
        if (block instanceof AbstractCrockPotDoubleCropBlock cropBlock) {
            var maxAge = cropBlock.getMaxGrowthAge(cropBlock.defaultBlockState());
            if (age > maxAge) {
                level.setBlock(pos, cropBlock.getStateForAge(maxAge), Block.UPDATE_CLIENTS);
                if (level.isEmptyBlock(pos.above())) {
                    level.setBlock(pos.above(), cropBlock.getStateForAge(age), Block.UPDATE_CLIENTS);
                }
            } else {
                level.setBlock(pos, cropBlock.getStateForAge(age), Block.UPDATE_CLIENTS);
            }
        } else if (block instanceof CropBlock cropBlock) {
            level.setBlock(pos, cropBlock.getStateForAge(Math.min(age, cropBlock.getMaxAge())), Block.UPDATE_CLIENTS);
        } else {
            level.setBlock(pos, block.defaultBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CrockPotItems.UNKNOWN_SEEDS.get();
    }
}
