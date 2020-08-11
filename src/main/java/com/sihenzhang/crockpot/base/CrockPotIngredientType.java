package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import net.minecraft.item.ItemStack;

public enum CrockPotIngredientType {
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

    public static ItemStack getItemStack(CrockPotIngredientType type) {
        switch (type) {
            case MEAT:
                return new ItemStack(CrockPotRegistry.ingredientMeat.get());
            case MONSTER:
                return new ItemStack(CrockPotRegistry.ingredientMonster.get());
            case FISH:
                return new ItemStack(CrockPotRegistry.ingredientFish.get());
            case EGG:
                return new ItemStack(CrockPotRegistry.ingredientEgg.get());
            case FRUIT:
                return new ItemStack(CrockPotRegistry.ingredientFruit.get());
            case VEGGIE:
                return new ItemStack(CrockPotRegistry.ingredientVeggie.get());
            case DAIRY:
                return new ItemStack(CrockPotRegistry.ingredientDairy.get());
            case SWEETENER:
                return new ItemStack(CrockPotRegistry.ingredientSweetener.get());
            case FROZEN:
                return new ItemStack(CrockPotRegistry.ingredientFrozen.get());
            case INEDIBLE:
                return new ItemStack(CrockPotRegistry.ingredientInedible.get());
        }
        return ItemStack.EMPTY;
    }
}
