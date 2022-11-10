package com.sihenzhang.crockpot.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Map;

public interface IFoodCounter extends INBTSerializable<CompoundTag> {
    /**
     * Check if the player has eaten the food
     *
     * @param food the food to be checked
     * @return true if the player has eaten the food
     */
    boolean hasEaten(Item food);

    /**
     * Add one to the number of times the food is eaten
     *
     * @param food the food is eaten
     */
    void addFood(Item food);

    /**
     * Get the number of times the food is eaten
     *
     * @param food the food to be checked
     * @return the number of times the food is eaten
     */
    int getCount(Item food);

    /**
     * Set the number of times the food is eaten
     * <p>
     * This method is usually used to sync the data from server to client
     *
     * @param food  the food to be set
     * @param count the number of times the food is eaten
     */
    void setCount(Item food, int count);

    /**
     * Clear the food counter, i.e. set all food counts to 0
     * <p>
     * This method is usually used before sync the data from server to client
     */
    void clear();

    /**
     * Convert the food counter to a map
     * <p>
     * This method is used to convert the food counter to a more easily traversable data structure
     *
     * @return the food counter map
     */
    Map<Item, Integer> asMap();
}
