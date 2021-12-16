package com.sihenzhang.crockpot.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class FoodCounterProvider implements ICapabilitySerializable<CompoundTag> {
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
    public CompoundTag serializeNBT() {
        return foodCounter.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        foodCounter.deserializeNBT(nbt);
    }
}
