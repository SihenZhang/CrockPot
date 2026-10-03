package com.sihenzhang.crockpot.registry;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.DataPackRegistryEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class ModRegistries {
    public static final ResourceKey<Registry<FoodCategory>> FOOD_CATEGORY_REGISTRY_KEY = ResourceKey.createRegistryKey(RLUtils.createRL("food_category"));

    private ModRegistries() {
    }

    @SubscribeEvent
    public static void registerDatapackRegistries(final DataPackRegistryEvent.NewRegistry event) {
        event.dataPackRegistry(FOOD_CATEGORY_REGISTRY_KEY, FoodCategory.DIRECT_CODEC, FoodCategory.DIRECT_CODEC);
    }
}
