package com.sihenzhang.crockpot.block.food;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CrockPotStackableFoodBlock extends CrockPotFoodBlock {
    public static final MapCodec<CrockPotStackableFoodBlock> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    Codec.INT.fieldOf("max_stacks").forGetter(b -> b.maxStacks),
                    propertiesCodec()
            ).apply(i, CrockPotStackableFoodBlock::new)
    );
    private static final Int2ObjectMap<IntegerProperty> STACKS_PROPERTY_CACHE = new Int2ObjectOpenHashMap<>();
    private final int maxStacks;
    private final IntegerProperty stacksProperty;

    @Override
    public MapCodec<CrockPotStackableFoodBlock> codec() {
        return CODEC;
    }

    public CrockPotStackableFoodBlock(int maxStacks, BlockBehaviour.Properties properties) {
        this.maxStacks = maxStacks;
        this.stacksProperty = STACKS_PROPERTY_CACHE.computeIfAbsent(
                maxStacks,
                stacks -> IntegerProperty.create("stacks", 1, stacks)
        );
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(this.stacksProperty, 1));
    }

    public int getMaxStacks() {
        return maxStacks;
    }

    public IntegerProperty getStacksProperty() {
        return stacksProperty;
    }

    @Override
    protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
        if (context.getItemInHand().is(this.asItem()) && state.is(this)) {
            return state.getValue(this.stacksProperty) < this.maxStacks;
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        var clickedState = context.getLevel().getBlockState(context.getClickedPos());
        return clickedState.is(this) ? clickedState.cycle(this.stacksProperty) : super.getStateForPlacement(context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(this.stacksProperty);
    }
}
