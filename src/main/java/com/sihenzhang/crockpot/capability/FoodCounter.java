package com.sihenzhang.crockpot.capability;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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
    public CompoundTag serializeNBT() {
        var tag = new CompoundTag();
        var list = new ListTag();
        this.asMap().forEach((food, count) -> {
            var key = ForgeRegistries.ITEMS.getKey(food);
            if (key != null) {
                var foodCount = new CompoundTag();
                foodCount.putString("Food", key.toString());
                foodCount.putInt("Count", count);
                list.add(foodCount);
            }
        });
        tag.put("FoodCounter", list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.clear();
        var foodCounter = nbt.getList("FoodCounter", Tag.TAG_COMPOUND);
        foodCounter.stream().map(CompoundTag.class::cast).forEach(foodCount -> {
            var key = foodCount.getString("Food");
            var food = ForgeRegistries.ITEMS.getValue(new ResourceLocation(foodCount.getString("Food")));
            if (food == null) {
                LOGGER.warn("Attempt to load unregistered item: \"{}\", will remove this.", key);
                return;
            }
            if (!food.isEdible()) {
                LOGGER.warn("Attempting to load item that is not edible: \"{}\", will not remove this in case it becomes edible again later.", key);
            }
            this.setCount(food, foodCount.getInt("Count"));
        });
    }
}
