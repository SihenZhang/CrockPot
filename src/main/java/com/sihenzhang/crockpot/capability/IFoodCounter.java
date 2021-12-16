package com.sihenzhang.crockpot.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface IFoodCounter extends INBTSerializable<CompoundTag> {
    boolean hasEaten(Item food);

    void addFood(Item food);

    int getCount(Item food);

    void setCount(Item food, int count);

    void clear();

    Map<Item, Integer> asMap();
}
