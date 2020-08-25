package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CornBlock extends CrockPotDoubleCropsBlock {
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

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[state.get(this.getAgeProperty())];
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return CrockPotRegistry.cornSeeds.get();
    }
}
