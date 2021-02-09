package com.sihenzhang.crockpot.base;

import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.Color;

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

    public static ItemStack getItemStack(FoodCategory category) {
        return Optional.ofNullable(CrockPotRegistry.foodCategoryItems.get(category)).map(o -> o.get().getDefaultInstance()).orElse(ItemStack.EMPTY);
    }

    public static Color getColor(FoodCategory category) {
        switch (category) {
            case MEAT:
                return Color.fromHex("#FFABC7");
            case MONSTER:
                return Color.fromHex("#D700FF");
            case FISH:
                return Color.fromHex("#006BFF");
            case EGG:
                return Color.fromHex("#00FFBB");
            case FRUIT:
                return Color.fromHex("#FF6B00");
            case VEGGIE:
                return Color.fromHex("#00FF00");
            case DAIRY:
                return Color.fromHex("#00C7FF");
            case SWEETENER:
                return Color.fromHex("#FFFF00");
            case FROZEN:
                return Color.fromHex("#82FFFF");
            case INEDIBLE:
                return Color.fromHex("#9B9B9B");
            default:
                return Color.fromHex("white");
        }
    }
}
