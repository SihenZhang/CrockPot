package com.sihenzhang.crockpot.block.food;

import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class CrockPot4StacksFoodBlock extends AbstractStackableFoodBlock {
    public static final IntegerProperty STACKS = IntegerProperty.create("stacks", 1, 4);

    @Override
    public int getMaxStacks() {
        return 4;
    }

    @Override
    public IntegerProperty getStacksProperty() {
        return STACKS;
    }
}
