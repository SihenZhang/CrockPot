package com.sihenzhang.crockpot.registry;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.IdUtil;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ModRegistries {
    public static final ResourceKey<Registry<FoodCategory>> FOOD_CATEGORY_REGISTRY_KEY = ResourceKey.createRegistryKey(IdUtil.mod("food_category"));

    @SubscribeEvent
    public static void registerDatapackRegistries(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(FOOD_CATEGORY_REGISTRY_KEY, FoodCategory.DIRECT_CODEC, FoodCategory.DIRECT_CODEC);
    }
}
