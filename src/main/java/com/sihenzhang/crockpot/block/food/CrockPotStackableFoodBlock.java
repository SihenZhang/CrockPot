package com.sihenzhang.crockpot.block.food;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class CrockPotStackableFoodBlock extends CrockPotFoodBlock {
    private static final Int2ObjectMap<IntegerProperty> STACKS_PROPERTY_CACHE = new Int2ObjectOpenHashMap<>();

    private CrockPotStackableFoodBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(this.getStacksProperty(), 1));
    }

    public abstract int getMaxStacks();

    public abstract IntegerProperty getStacksProperty();

    public static CrockPotStackableFoodBlock of(BlockBehaviour.Properties properties, int maxStacks) {
        var stacksProperty = STACKS_PROPERTY_CACHE.computeIfAbsent(maxStacks, stacks -> IntegerProperty.create("stacks", 1, stacks));
        return new CrockPotStackableFoodBlock(properties) {
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
        return of(BlockBehaviour.Properties.of(), maxStacks);
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (context.getItemInHand().is(this.asItem()) && state.is(this)) {
            return state.getValue(this.getStacksProperty()) < this.getMaxStacks();
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var clickedState = context.getLevel().getBlockState(context.getClickedPos());
        return clickedState.is(this) ? clickedState.cycle(this.getStacksProperty()) : super.getStateForPlacement(context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(this.getStacksProperty());
    }
}
