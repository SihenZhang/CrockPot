package com.sihenzhang.crockpot.capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FoodCounterProvider implements ICapabilitySerializable<INBT> {
    private final IFoodCounter foodCounter;
    private final LazyOptional<IFoodCounter> foodCounterOptional;

    public FoodCounterProvider() {
        this.foodCounter = new FoodCounter();
        this.foodCounterOptional = LazyOptional.of(() -> this.foodCounter);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY.orEmpty(cap, foodCounterOptional);
    }

    @Override
    public INBT serializeNBT() {
        return FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY.writeNBT(foodCounter, null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY.readNBT(foodCounter, null, nbt);
    }
}
