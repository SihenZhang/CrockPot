package com.sihenzhang.crockpot.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

import java.util.Random;

public abstract class AbstractCrockPotDoubleCropBlock extends AbstractCrockPotCropBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = {
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Shapes.block(),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Shapes.block()
    };

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }

    public boolean isUpperBlock(BlockState state) {
        return this.getAge(state) > this.getMaxAge() / 2;
    }

    public int getMaxGrowthAge(BlockState state) {
        return this.isUpperBlock(state) ? this.getMaxAge() : this.getMaxAge() / 2;
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        if (!level.isClientSide) {
            if (this.isUpperBlock(state)) {
                var lowerPos = pos.below();
                var lowerState = level.getBlockState(lowerPos);
                if (lowerState.is(this)) {
                    level.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), UPDATE_SUPPRESS_DROPS | UPDATE_ALL);
                    level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, lowerPos, Block.getId(lowerState));
                }
            }
        }
        super.playerWillDestroy(level, pos, state, player);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) {
            return;
        }
        if (level.getRawBrightness(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                BlockPos blockPos = this.isUpperBlock(state) && level.getBlockState(pos.below()).getBlock() == this ? pos.below() : pos;
                float growthChance = getGrowthChance(this, level, blockPos);
                if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                    if (age != this.getMaxGrowthAge(state)) {
                        level.setBlock(pos, this.getStateForAge(age + 1), 2);
                        ForgeHooks.onCropsGrowPost(level, pos, state);
                    } else {
                        if (level.isEmptyBlock(pos.above())) {
                            level.setBlock(pos.above(), this.getStateForAge(age + 1), 2);
                            ForgeHooks.onCropsGrowPost(level, pos, state);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int age = this.getAge(state);
        int maxAge = this.getMaxAge();
        if (age < maxAge) {
            int maxGrowthAge = this.getMaxGrowthAge(state);
            if (age != maxGrowthAge) {
                int expectedAge = age + this.getBonemealAgeIncrease(level);
                if (expectedAge > maxAge) {
                    expectedAge = maxAge;
                }
                if (expectedAge > maxGrowthAge) {
                    level.setBlock(pos, this.getStateForAge(maxGrowthAge), 2);
                    if (level.isEmptyBlock(pos.above())) {
                        level.setBlock(pos.above(), this.getStateForAge(expectedAge), 2);
                    }
                } else {
                    level.setBlock(pos, this.getStateForAge(expectedAge), 2);
                }
            } else {
                BlockState stateUp = level.getBlockState(pos.above());
                if (stateUp.getBlock() == this && this.getAge(stateUp) != this.getMaxGrowthAge(stateUp)) {
                    int expectedAge = this.getAge(stateUp) + this.getBonemealAgeIncrease(level);
                    if (expectedAge > maxAge) {
                        expectedAge = maxAge;
                    }
                    level.setBlock(pos.above(), this.getStateForAge(expectedAge), 2);
                } else if (level.isEmptyBlock(pos.above())) {
                    int expectedAge = age + this.getBonemealAgeIncrease(level);
                    if (expectedAge > maxAge) {
                        expectedAge = maxAge;
                    }
                    level.setBlock(pos.above(), this.getStateForAge(expectedAge), 2);
                }
            }
        }
    }

    protected static float getGrowthChance(AbstractCrockPotDoubleCropBlock block, BlockGetter level, BlockPos pos) {
        if (block.isUpperBlock(level.getBlockState(pos))) {
            return CropBlock.getGrowthSpeed(block, level, pos.below());
        } else {
            return CropBlock.getGrowthSpeed(block, level, pos);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (this.isUpperBlock(state)) {
            BlockState stateDown = level.getBlockState(pos.below());
            return (level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos)) && stateDown.getBlock() == this && this.getAge(stateDown) == this.getMaxGrowthAge(stateDown);
        } else {
            return super.canSurvive(state, level, pos);
        }
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        if (this.getAge(state) < this.getMaxAge()) {
            if (this.getAge(state) != this.getMaxGrowthAge(state)) {
                return true;
            } else {
                BlockState stateUp = level.getBlockState(pos.above());
                return (stateUp.getBlock() == this && this.getAge(stateUp) != this.getMaxGrowthAge(stateUp)) || stateUp.isAir();
            }
        }
        return false;
    }
}
