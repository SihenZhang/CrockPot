package com.sihenzhang.crockpot.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class CrockPotDoubleCropsBlock extends CrockPotCropsBlock {
    private static final VoxelShape[] SHAPE_BY_AGE = {
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            VoxelShapes.block(),
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            VoxelShapes.block()
    };

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE_BY_AGE[state.getValue(this.getAgeProperty())];
    }

    public boolean isUpperBlock(BlockState state) {
        return this.getAge(state) > this.getMaxAge() / 2;
    }

    public int getMaxGrowthAge(BlockState state) {
        return this.isUpperBlock(state) ? this.getMaxAge() : this.getMaxAge() / 2;
    }

    @Override
    public void playerWillDestroy(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!worldIn.isClientSide) {
            if (!player.isCreative()) {
                dropResources(state, worldIn, pos, null, player, player.getMainHandItem());
            }
            BlockPos lowerPos = this.isUpperBlock(state) ? pos.below() : pos;
            BlockState lowerState = worldIn.getBlockState(lowerPos);
            if (lowerState.getBlock() == state.getBlock()) {
                worldIn.setBlock(lowerPos, Blocks.AIR.defaultBlockState(), 35);
                worldIn.levelEvent(player, 2001, lowerPos, Block.getId(lowerState));
            }
        }
        super.playerWillDestroy(worldIn, pos, state, player);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 1)) {
            return;
        }
        if (worldIn.getRawBrightness(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                BlockPos blockPos = this.isUpperBlock(state) && worldIn.getBlockState(pos.below()).getBlock() == this.getBlock() ? pos.below() : pos;
                float growthChance = getGrowthChance(this, worldIn, blockPos);
                if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                    if (age != this.getMaxGrowthAge(state)) {
                        worldIn.setBlock(pos, this.getStateForAge(age + 1), 2);
                        ForgeHooks.onCropsGrowPost(worldIn, pos, state);
                    } else {
                        if (worldIn.isEmptyBlock(pos.above())) {
                            worldIn.setBlock(pos.above(), this.getStateForAge(age + 1), 2);
                            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void growCrops(World worldIn, BlockPos pos, BlockState state) {
        int age = this.getAge(state);
        int maxAge = this.getMaxAge();
        if (age < maxAge) {
            int maxGrowthAge = this.getMaxGrowthAge(state);
            if (age != maxGrowthAge) {
                int expectedAge = age + this.getBonemealAgeIncrease(worldIn);
                if (expectedAge > maxAge) {
                    expectedAge = maxAge;
                }
                if (expectedAge > maxGrowthAge) {
                    worldIn.setBlock(pos, this.getStateForAge(maxGrowthAge), 2);
                    if (worldIn.isEmptyBlock(pos.above())) {
                        worldIn.setBlock(pos.above(), this.getStateForAge(expectedAge), 2);
                    }
                } else {
                    worldIn.setBlock(pos, this.getStateForAge(expectedAge), 2);
                }
            } else {
                BlockState stateUp = worldIn.getBlockState(pos.above());
                if (stateUp.getBlock() == this.getBlock() && this.getAge(stateUp) != this.getMaxGrowthAge(stateUp)) {
                    int expectedAge = this.getAge(stateUp) + this.getBonemealAgeIncrease(worldIn);
                    if (expectedAge > maxAge) {
                        expectedAge = maxAge;
                    }
                    worldIn.setBlock(pos.above(), this.getStateForAge(expectedAge), 2);
                } else if (worldIn.isEmptyBlock(pos.above())) {
                    int expectedAge = age + this.getBonemealAgeIncrease(worldIn);
                    if (expectedAge > maxAge) {
                        expectedAge = maxAge;
                    }
                    worldIn.setBlock(pos.above(), this.getStateForAge(expectedAge), 2);
                }
            }
        }
    }

    protected static float getGrowthChance(CrockPotDoubleCropsBlock blockIn, IBlockReader worldIn, BlockPos pos) {
        if (blockIn.isUpperBlock(worldIn.getBlockState(pos))) {
            return CropsBlock.getGrowthSpeed(blockIn, worldIn, pos.below());
        } else {
            return CropsBlock.getGrowthSpeed(blockIn, worldIn, pos);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (this.isUpperBlock(state)) {
            BlockState stateDown = worldIn.getBlockState(pos.below());
            return (worldIn.getRawBrightness(pos, 0) >= 8 || worldIn.canSeeSky(pos)) && stateDown.getBlock() == this.getBlock() && this.getAge(stateDown) == this.getMaxGrowthAge(stateDown);
        } else {
            return super.canSurvive(state, worldIn, pos);
        }
    }

    @Override
    public boolean isValidBonemealTarget(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        if (this.getAge(state) < this.getMaxAge()) {
            if (this.getAge(state) != this.getMaxGrowthAge(state)) {
                return true;
            } else {
                BlockState stateUp = worldIn.getBlockState(pos.above());
                return (stateUp.getBlock() == this.getBlock() && this.getAge(stateUp) != this.getMaxGrowthAge(stateUp)) || stateUp.isAir(worldIn, pos.above());
            }
        }
        return false;
    }
}
