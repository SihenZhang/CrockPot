package com.sihenzhang.crockpot.attachment;

import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Util;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

import java.util.HashMap;
import java.util.Map;

public class FoodCounter implements ValueIOSerializable {
    public static final StreamCodec<RegistryFriendlyByteBuf, FoodCounter> STREAM_CODEC =
            ByteBufCodecs.<RegistryFriendlyByteBuf, Item, Integer, Map<Item, Integer>>map(
                    HashMap::new,
                    ByteBufCodecs.registry(Registries.ITEM),
                    ByteBufCodecs.VAR_INT
            ).map(
                    map -> Util.make(new FoodCounter(), counter -> map.forEach(counter::setCount)),
                    FoodCounter::asMap
            );

    private final Multiset<Item> counter = HashMultiset.create();

    public boolean hasEaten(Item food) {
        return counter.contains(food);
    }

    public void addFood(Item food) {
        counter.add(food);
    }

    public int getCount(Item food) {
        return counter.count(food);
    }

    public void setCount(Item food, int count) {
        counter.setCount(food, count);
    }

    public void clear() {
        counter.clear();
    }

    public Map<Item, Integer> asMap() {
        return Maps.asMap(counter.elementSet(), counter::count);
    }

    @Override
    public void serialize(ValueOutput output) {
        var list = output.childrenList("FoodCounter");
        this.asMap().forEach((item, count) -> {
            var child = list.addChild();
            child.store("Food", BuiltInRegistries.ITEM.byNameCodec(), item);
            child.putInt("Count", count);
        });
    }

    @Override
    public void deserialize(ValueInput input) {
        this.clear();
        var list = input.childrenListOrEmpty("FoodCounter");
        list.forEach(child -> {
            var optionalItem = child.read("Food", BuiltInRegistries.ITEM.byNameCodec());
            optionalItem.ifPresent(item -> this.setCount(item, child.getIntOr("Count", 0)));
        });
    }
}
