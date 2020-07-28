package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class CornBlock extends CropsBlock {
    private static final VoxelShape[] SHAPES = {
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            VoxelShapes.fullCube(),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            VoxelShapes.fullCube(),
            VoxelShapes.fullCube()
    };

    public CornBlock() {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.CROP));
    }

    public int getMaxGrowthAge(BlockState state) {
        if (this.getAge(state) > 3) {
            return this.getMaxAge();
        } else {
            return 3;
        }
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        boolean isValid = super.isValidPosition(state, worldIn, pos);
        if (this.getAge(state) > 3) {
            BlockState stateDown = worldIn.getBlockState(pos.down());
            isValid = stateDown.getBlock() instanceof CornBlock && this.getAge(stateDown) == this.getMaxGrowthAge(stateDown);
        }
        return isValid;
    }

    @Override
    public void onPlayerDestroy(IWorld worldIn, BlockPos pos, BlockState state) {
        BlockState stateDown = worldIn.getBlockState(pos.down());
        if (stateDown.getBlock() instanceof CornBlock) {
            worldIn.removeBlock(pos.down(), false);
        }
    }

    @Override
    public void tick(BlockState state, ServerWorld worldIn, BlockPos pos, Random rand) {
        if (!worldIn.isAreaLoaded(pos, 1)) {
            return;
        }
        if (worldIn.getLightSubtracted(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);
                if (rand.nextInt((int) (25.0F / f) + 1) == 0) {
                    if (this.getAge(state) != this.getMaxGrowthAge(state)) {
                        worldIn.setBlockState(pos, this.withAge(i + 1));
                    }
                    if (this.getAge(state) == this.getMaxGrowthAge(state) && worldIn.isAirBlock(pos.up())) {
                        worldIn.setBlockState(pos.up(), this.withAge(i + 1));
                    }
                }
            }
        }
    }

    @Override
    public boolean canGrow(IBlockReader worldIn, BlockPos pos, BlockState state, boolean isClient) {
        if (this.getAge(state) != this.getMaxGrowthAge(state)) {
            return true;
        }
        BlockState stateUp = worldIn.getBlockState(pos.up());
        if (this.getAge(state) == 3 && stateUp.getBlock() instanceof CornBlock && this.getAge(stateUp) != this.getMaxGrowthAge(stateUp)) {
            return true;
        }
        return this.getAge(state) == 3 && stateUp.isAir(worldIn, pos.up());
    }

    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return this.canGrow(worldIn, pos, worldIn.getBlockState(pos), worldIn.isRemote);
    }

    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        if (this.getAge(state) != this.getMaxGrowthAge(state)) {
            int i = this.getAge(state) + this.getBonemealAgeIncrease(worldIn);
            int j = this.getMaxGrowthAge(state);
            if (i > j) {
                i = j;
            }
            worldIn.setBlockState(pos, this.withAge(i));
        }
        if (this.getAge(state) == 3 && worldIn.getBlockState(pos.up()).getBlock() instanceof CornBlock) {
            int i = this.getAge(worldIn.getBlockState(pos.up())) + this.getBonemealAgeIncrease(worldIn);
            int j = this.getMaxAge();
            if (i > j) {
                i = j;
            }
            worldIn.setBlockState(pos.up(), this.withAge(i));
        }
        if (this.getAge(state) == 3 && worldIn.isAirBlock(pos.up())) {
            worldIn.setBlockState(pos.up(), this.withAge(this.getAge(state) + 1));
        }
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return CrockPotRegistry.cornSeeds.get();
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[state.get(this.getAgeProperty())];
    }
}
