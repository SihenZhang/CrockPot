package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.base.FoodCategoryManager;
import com.sihenzhang.crockpot.recipe.RecipeManager;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CrockPot.MOD_ID)
public final class CrockPot {
    public static final String MOD_ID = "crockpot";

    public static final ItemGroup ITEM_GROUP = new ItemGroup(MOD_ID) {
        @Override
        public ItemStack createIcon() {
            return CrockPotRegistry.crockPotBasicBlockItem.getDefaultInstance();
        }
    };

    public static final FoodCategoryManager FOOD_CATEGORY_MANAGER = new FoodCategoryManager();
    public static final RecipeManager RECIPE_MANAGER = new RecipeManager();

    public CrockPot() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CrockPotConfig.COMMON_CONFIG);
        CrockPotRegistry.ITEMS.register(modEventBus);
        CrockPotRegistry.BLOCKS.register(modEventBus);
        CrockPotRegistry.TILE_ENTITIES.register(modEventBus);
        CrockPotRegistry.CONTAINERS.register(modEventBus);
        CrockPotRegistry.ENTITIES.register(modEventBus);
        CrockPotRegistry.EFFECTS.register(modEventBus);
        CrockPotRegistry.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        CrockPotRegistry.FEATURES.register(modEventBus);
    }
}
