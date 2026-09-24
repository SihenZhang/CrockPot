package com.sihenzhang.crockpot.block;

import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CornBlock extends AbstractCrockPotDoubleCropBlock {
    public static final MapCodec<CornBlock> CODEC = simpleCodec(CornBlock::new);

    public CornBlock() {
        super();
    }

    private CornBlock(Properties properties) {
        super(properties);
    }

    @Override
    public MapCodec<? extends CornBlock> codec() {
        return CODEC;
    }

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

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[state.getValue(this.getAgeProperty())];
    }

    @Override
    protected ItemLike getBaseSeedId() {
        return CrockPotItems.CORN_SEEDS.get();
    }
}
