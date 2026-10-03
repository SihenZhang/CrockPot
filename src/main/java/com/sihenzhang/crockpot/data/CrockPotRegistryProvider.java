package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.CrockPotDamageTypes;
import com.sihenzhang.crockpot.registry.FoodCategories;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.registry.ModRegistries;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.damagesource.DamageType;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class CrockPotRegistryProvider extends DatapackBuiltinEntriesProvider {
    private static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY, CrockPotRegistryProvider::bootstrapFoodCategories)
            .add(Registries.DAMAGE_TYPE, CrockPotRegistryProvider::bootstrapDamageTypes);

    public CrockPotRegistryProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture) {
        super(output, providerFuture, BUILDER, Set.of(CrockPot.MOD_ID));
    }

    private static void bootstrapFoodCategories(BootstrapContext<FoodCategory> bootstrap) {
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
    }

    private static void bootstrapDamageTypes(BootstrapContext<DamageType> bootstrap) {
        bootstrap.register(CrockPotDamageTypes.CANDY, new DamageType("crockpot.candy", 0.1F));
        bootstrap.register(CrockPotDamageTypes.MONSTER_FOOD, new DamageType("crockpot.monster_food", 0.1F));
        bootstrap.register(CrockPotDamageTypes.POW_CAKE, new DamageType("crockpot.pow_cake", 0.1F));
        bootstrap.register(CrockPotDamageTypes.SPICY, new DamageType("crockpot.spicy", 0.1F));
        bootstrap.register(CrockPotDamageTypes.TAFFY, new DamageType("crockpot.taffy", 0.1F));
    }
}
