package com.sihenzhang.crockpot.registry;

import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.resources.ResourceKey;

public final class FoodCategories {
    public static final ResourceKey<FoodCategory> MEAT = create("meat");
    public static final ResourceKey<FoodCategory> MONSTER = create("monster");
    public static final ResourceKey<FoodCategory> FISH = create("fish");
    public static final ResourceKey<FoodCategory> EGG = create("egg");
    public static final ResourceKey<FoodCategory> FRUIT = create("fruit");
    public static final ResourceKey<FoodCategory> VEGGIE = create("veggie");
    public static final ResourceKey<FoodCategory> DAIRY = create("dairy");
    public static final ResourceKey<FoodCategory> SWEETENER = create("sweetener");
    public static final ResourceKey<FoodCategory> FROZEN = create("frozen");
    public static final ResourceKey<FoodCategory> INEDIBLE = create("inedible");

    private FoodCategories() {
    }

    private static ResourceKey<FoodCategory> create(String name) {
        return ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, RLUtils.createRL(name));
    }
}
