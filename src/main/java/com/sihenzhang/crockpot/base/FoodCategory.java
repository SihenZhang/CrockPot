package com.sihenzhang.crockpot.base;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;

public enum FoodCategory implements StringRepresentable {
    MEAT("MEAT", 0xFFABC7),
    MONSTER("MONSTER", 0xD700FF),
    FISH("FISH", 0x006BFF),
    EGG("EGG", 0x00FFBB),
    FRUIT("FRUIT", 0xFF6B00),
    VEGGIE("VEGGIE", 0x00FF00),
    DAIRY("DAIRY", 0x00C7FF),
    SWEETENER("SWEETENER", 0xFFFF00),
    FROZEN("FROZEN", 0x82FFFF),
    INEDIBLE("INEDIBLE", 0x9B9B9B);

    public static final Codec<FoodCategory> CODEC = StringRepresentable.fromEnum(FoodCategory::values);
    private final String name;
    private final TextColor color;

    FoodCategory(String name, int color) {
        this.name = name;
        this.color = TextColor.fromRgb(color);
    }

    public TextColor getColor() {
        return color;
    }

    @Override
    public String getSerializedName() {
        return name;
    }

    public static ItemStack getItemStack(FoodCategory category) {
        return ModItems.FOOD_CATEGORY_ITEMS.get(category).get().getDefaultInstance();
    }
}
