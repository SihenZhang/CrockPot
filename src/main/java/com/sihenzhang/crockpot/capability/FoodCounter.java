package com.sihenzhang.crockpot.capability;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

public class FoodCounter implements IFoodCounter {
    private static final Logger LOGGER = LogManager.getLogger();

    private final Multiset<Item> counter = HashMultiset.create();

    @Override
    public boolean hasEaten(Item food) {
        return counter.contains(food);
    }

    @Override
    public void addFood(Item food) {
        counter.add(food);
    }

    @Override
    public int getCount(Item food) {
        return counter.count(food);
    }

    @Override
    public void setCount(Item food, int count) {
        counter.setCount(food, count);
    }

    @Override
    public void clear() {
        counter.clear();
    }

    @Override
    public Map<Item, Integer> asMap() {
        return Maps.asMap(counter.elementSet(), counter::count);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT tag = new CompoundNBT();
        ListNBT list = new ListNBT();
        this.asMap().forEach((food, count) -> {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(food);
            if (key != null) {
                CompoundNBT foodCount = new CompoundNBT();
                foodCount.putString("Food", key.toString());
                foodCount.putInt("Count", count);
                list.add(foodCount);
            }
        });
        tag.put("FoodCounter", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.clear();
        ListNBT foodCounter = nbt.getList("FoodCounter", Constants.NBT.TAG_COMPOUND);
        foodCounter.stream().map(CompoundNBT.class::cast).forEach(foodCount -> {
            String key = foodCount.getString("Food");
            Item food = ForgeRegistries.ITEMS.getValue(new ResourceLocation(foodCount.getString("Food")));
            if (food == null) {
                LOGGER.warn("Attempt to load unregistered item: \"" + key + "\", will remove this.");
                return;
            }
            if (!food.isEdible()) {
                LOGGER.warn("Attempting to load item that is not edible: \"" + key + "\", will not remove this in case it becomes edible again later.");
            }
            this.setCount(food, foodCount.getInt("Count"));
        });
    }
}
