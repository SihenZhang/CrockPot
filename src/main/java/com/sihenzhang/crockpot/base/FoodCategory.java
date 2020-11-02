package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.item.ItemStack;

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
        switch (type) {
            case MEAT:
                return new ItemStack(CrockPotRegistry.foodCategoryMeat.get());
            case MONSTER:
                return new ItemStack(CrockPotRegistry.foodCategoryMonster.get());
            case FISH:
                return new ItemStack(CrockPotRegistry.foodCategoryFish.get());
            case EGG:
                return new ItemStack(CrockPotRegistry.foodCategoryEgg.get());
            case FRUIT:
                return new ItemStack(CrockPotRegistry.foodCategoryFruit.get());
            case VEGGIE:
                return new ItemStack(CrockPotRegistry.foodCategoryVeggie.get());
            case DAIRY:
                return new ItemStack(CrockPotRegistry.foodCategoryDairy.get());
            case SWEETENER:
                return new ItemStack(CrockPotRegistry.foodCategorySweetener.get());
            case FROZEN:
                return new ItemStack(CrockPotRegistry.foodCategoryFrozen.get());
            case INEDIBLE:
                return new ItemStack(CrockPotRegistry.foodCategoryInedible.get());
            default:
                return ItemStack.EMPTY;
        }
    }
}
