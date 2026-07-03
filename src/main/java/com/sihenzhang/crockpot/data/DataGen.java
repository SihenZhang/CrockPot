package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.core.ModDamageTypes;
import com.sihenzhang.crockpot.registry.FoodCategories;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.registry.ModRegistries;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class DataGen {
    @SubscribeEvent
    public static void gatherClientData(final GatherDataEvent.Client event) {
        event.createDatapackRegistryObjects(createRegistrySetBuilder());
        event.createBlockAndItemTags(ModBlockTagsProvider::new, ModItemTagsProvider::new);
        event.createProvider(ModDamageTypeTagsProvider::new);
        event.createProvider(ModEntityTypeTagsProvider::new);
        event.createProvider(ModAdvancementProvider::new);
        event.createProvider(ModLootTableProvider::new);
        event.createProvider(ModGlobalLootModifierProvider::new);
        event.createProvider(ModRecipeProvider.Runner::new);
        event.createProvider(ModSoundDefinitionsProvider::new);
        event.createProvider(ModModelProvider::new);
        event.createProvider(ModDataMapProvider::new);
    }

    private static RegistrySetBuilder createRegistrySetBuilder() {
        return new RegistrySetBuilder().add(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, bootstrap -> {
            bootstrap.register(FoodCategories.MEAT, new FoodCategory(0xFFABC7));
            bootstrap.register(FoodCategories.MONSTER, new FoodCategory(0xD700FF));
            bootstrap.register(FoodCategories.FISH, new FoodCategory(0x006BFF));
            bootstrap.register(FoodCategories.EGG, new FoodCategory(0x00FFBB));
            bootstrap.register(FoodCategories.FRUIT, new FoodCategory(0xFF6B00));
            bootstrap.register(FoodCategories.VEGGIE, new FoodCategory(0x00FF00));
            bootstrap.register(FoodCategories.DAIRY, new FoodCategory(0x00C7FF));
            bootstrap.register(FoodCategories.SWEETENER, new FoodCategory(0xFFFF00));
            bootstrap.register(FoodCategories.FROZEN, new FoodCategory(0x82FFFF));
            bootstrap.register(FoodCategories.INEDIBLE, new FoodCategory(0x9B9B9B));
        }).add(Registries.DAMAGE_TYPE, bootstrap -> {
            bootstrap.register(ModDamageTypes.CANDY, new DamageType("crockpot.candy", 0.1F));
            bootstrap.register(ModDamageTypes.MONSTER_FOOD, new DamageType("crockpot.monster_food", 0.1F));
            bootstrap.register(ModDamageTypes.POW_CAKE, new DamageType("crockpot.pow_cake", 0.1F));
            bootstrap.register(ModDamageTypes.SPICY, new DamageType("crockpot.spicy", 0.1F));
            bootstrap.register(ModDamageTypes.TAFFY, new DamageType("crockpot.taffy", 0.1F));
        });
    }
}
