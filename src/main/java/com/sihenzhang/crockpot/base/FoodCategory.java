package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public enum FoodCategory {
    MEAT,
    MONSTER,
    FISH,
    EGG,
    FRUIT,
    VEGGIE,
    DAIRY,
    SWEETENER,
    FROZEN,
    INEDIBLE;

    public static ItemStack getItemStack(FoodCategory type) {
        return Optional.ofNullable(CrockPotRegistry.foodCategoryItems.get(type)).map(o -> new ItemStack(o.get())).orElse(ItemStack.EMPTY);
    }
}
