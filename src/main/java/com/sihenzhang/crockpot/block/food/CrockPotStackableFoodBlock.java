package com.sihenzhang.crockpot.block.food;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class CrockPotStackableFoodBlock extends CrockPotFoodBlock {
    private static final Int2ObjectMap<IntegerProperty> STACKS_PROPERTY_CACHE = new Int2ObjectOpenHashMap<>();

    private CrockPotStackableFoodBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(this.getStacksProperty(), 1));
    }

    public abstract int getMaxStacks();

    public abstract IntegerProperty getStacksProperty();

    public static CrockPotStackableFoodBlock of(Properties pProperties, int maxStacks) {
        var stacksProperty = STACKS_PROPERTY_CACHE.computeIfAbsent(maxStacks, stacks -> IntegerProperty.create("stacks", 1, stacks));
        return new CrockPotStackableFoodBlock(pProperties) {
            @Override
            public int getMaxStacks() {
                return maxStacks;
            }

            @Override
            public IntegerProperty getStacksProperty() {
                return stacksProperty;
            }
        };
    }

    public static CrockPotStackableFoodBlock of(int maxStacks) {
        return of(Properties.of(), maxStacks);
    }

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
