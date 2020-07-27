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
import net.minecraft.world.IBlockReader;

public class CornBlock extends CropsBlock {
    private static final VoxelShape[] SHAPES = {
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 2.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 3.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 5.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 7.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.makeCuboidShape(0.0, 0.0, 0.0, 16.0, 9.0, 16.0)
    };

    public CornBlock() {
        super(Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().hardnessAndResistance(0.0F).sound(SoundType.CROP));
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return CrockPotRegistry.cornBlockItem.get();
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES[state.get(this.getAgeProperty())];
    }
}
