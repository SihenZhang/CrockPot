package com.sihenzhang.crockpot.block;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
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
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            VoxelShapes.fullCube(),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            VoxelShapes.fullCube()
    };

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE_BY_AGE[state.get(this.getAgeProperty())];
    }

    public boolean isTopBlock(BlockState state) {
        return this.getAge(state) > this.getMaxAge() / 2;
    }

    public int getMaxGrowthAge(BlockState state) {
        return this.isTopBlock(state) ? this.getMaxAge() : this.getMaxAge() / 2;
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        BlockState stateDown = worldIn.getBlockState(pos.down());
        if (stateDown.getBlock().getClass() == this.getClass()) {
            worldIn.destroyBlock(pos.down(), true);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 1)) {
            return;
        }
        if (worldIn.getLightSubtracted(pos, 0) >= 9) {
            int age = this.getAge(state);
            if (age < this.getMaxAge()) {
                BlockPos blockPos = this.isTopBlock(state) && worldIn.getBlockState(pos.down()).getBlock().getClass() == this.getClass() ? pos.down() : pos;
                float growthChance = getGrowthChance(this, worldIn, blockPos);
                if (ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int) (25.0F / growthChance) + 1) == 0)) {
                    if (age != this.getMaxGrowthAge(state)) {
                        worldIn.setBlockState(pos, this.withAge(age + 1), 2);
                        ForgeHooks.onCropsGrowPost(worldIn, pos, state);
                    } else {
                        if (worldIn.isAirBlock(pos.up())) {
                            worldIn.setBlockState(pos.up(), this.withAge(age + 1), 2);
                            ForgeHooks.onCropsGrowPost(worldIn, pos, state);
                        }
                    }
                }
            }
        }
    }

    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
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
                    worldIn.setBlockState(pos, this.withAge(maxGrowthAge), 2);
                    if (worldIn.isAirBlock(pos.up())) {
                        worldIn.setBlockState(pos.up(), this.withAge(expectedAge), 2);
                    }
                } else {
                    worldIn.setBlockState(pos, this.withAge(expectedAge), 2);
                }
            } else {
                BlockState stateUp = worldIn.getBlockState(pos.up());
                if (stateUp.getBlock().getClass() == this.getClass() && this.getAge(stateUp) != this.getMaxGrowthAge(stateUp)) {
                    int expectedAge = this.getAge(stateUp) + this.getBonemealAgeIncrease(worldIn);
                    if (expectedAge > maxAge) {
                        expectedAge = maxAge;
                    }
                    worldIn.setBlockState(pos.up(), this.withAge(expectedAge), 2);
                } else if (worldIn.isAirBlock(pos.up())) {
                    int expectedAge = age + this.getBonemealAgeIncrease(worldIn);
                    if (expectedAge > maxAge) {
                        expectedAge = maxAge;
                    }
                    worldIn.setBlockState(pos.up(), this.withAge(expectedAge), 2);
                }
            }
        }
    }

    protected static float getGrowthChance(CrockPotDoubleCropsBlock blockIn, IBlockReader worldIn, BlockPos pos) {
        if (blockIn.isTopBlock(worldIn.getBlockState(pos))) {
            return CropsBlock.getGrowthChance(blockIn, worldIn, pos.down());
        } else {
            return CropsBlock.getGrowthChance(blockIn, worldIn, pos);
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (this.isTopBlock(state)) {
            BlockState stateDown = worldIn.getBlockState(pos.down());
            return (worldIn.getLightSubtracted(pos, 0) >= 8 || worldIn.canSeeSky(pos)) && stateDown.getBlock().getClass() == this.getClass() && this.getAge(stateDown) == this.getMaxGrowthAge(stateDown);
        } else {
            return super.isValidPosition(state, worldIn, pos);
        }
    }

    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        if (this.getAge(state) < this.getMaxAge()) {
            if (this.getAge(state) != this.getMaxGrowthAge(state)) {
                return true;
            } else {
                BlockState stateUp = worldIn.getBlockState(pos.up());
                return (stateUp.getBlock().getClass() == this.getClass() && this.getAge(stateUp) != this.getMaxGrowthAge(stateUp)) || stateUp.isAir(worldIn, pos.up());
            }
        }
        return false;
    }
}
