package com.sihenzhang.crockpot.block;

import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CornBlock extends AbstractDoubleCropBlock {
    private static final VoxelShape[] SHAPES = {
            Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 12.0, 16.0),
            Shapes.block(),
            Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0),
            Block.box(0.0, 0.0, 0.0, 16.0, 14.0, 16.0),
            Shapes.block(),
            Shapes.block()
    };
    public static final MapCodec<CornBlock> CODEC = simpleCodec(CornBlock::new);

    @Override
    public MapCodec<CornBlock> codec() {
        return CODEC;
    }

    public CornBlock(Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[this.getAge(state)];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return ModItems.CORN_SEEDS.get();
    }
}
