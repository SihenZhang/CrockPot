package com.sihenzhang.crockpot.block.food;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class AbstractStackableFoodBlock extends CrockPotFoodBlock {
    public AbstractStackableFoodBlock() {
        this(Properties.of());
    }

    public AbstractStackableFoodBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(this.getStacksProperty(), 1));
    }

    public abstract int getMaxStacks();

    public abstract IntegerProperty getStacksProperty();

    @Override
    @SuppressWarnings("deprecation")
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        if (!pUseContext.isSecondaryUseActive() && pUseContext.getItemInHand().getItem() == this.asItem() && pState.is(this)) {
            return pState.getValue(this.getStacksProperty()) < this.getMaxStacks();
        }
        return super.canBeReplaced(pState, pUseContext);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        var clickedState = pContext.getLevel().getBlockState(pContext.getClickedPos());
        return clickedState.is(this) ? clickedState.cycle(this.getStacksProperty()) : super.getStateForPlacement(pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(this.getStacksProperty());
    }
}
