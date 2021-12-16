package com.sihenzhang.crockpot.capability;

import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface IFoodCounter extends INBTSerializable<CompoundNBT> {
    boolean hasEaten(Item food);

    void addFood(Item food);

    int getCount(Item food);

    void setCount(Item food, int count);

    void clear();

    Map<Item, Integer> asMap();
}
