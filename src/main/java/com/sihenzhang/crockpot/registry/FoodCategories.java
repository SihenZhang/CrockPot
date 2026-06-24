package com.sihenzhang.crockpot.registry;

import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.resources.ResourceKey;

public class FoodCategories {
    public static final ResourceKey<FoodCategory> MEAT = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("meat"));
    public static final ResourceKey<FoodCategory> MONSTER = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("monster"));
    public static final ResourceKey<FoodCategory> FISH = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("fish"));
    public static final ResourceKey<FoodCategory> EGG = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("egg"));
    public static final ResourceKey<FoodCategory> FRUIT = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("fruit"));
    public static final ResourceKey<FoodCategory> VEGGIE = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("veggie"));
    public static final ResourceKey<FoodCategory> DAIRY = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("dairy"));
    public static final ResourceKey<FoodCategory> SWEETENER = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("sweetener"));
    public static final ResourceKey<FoodCategory> FROZEN = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("frozen"));
    public static final ResourceKey<FoodCategory> INEDIBLE = ResourceKey.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, IdUtil.mod("inedible"));
}
