package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntities;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.inventory.CrockPotMenuTypes;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.loot.CrockPotLootModifiers;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CrockPot.MOD_ID)
public final class CrockPot {
    public static final String MOD_ID = "crockpot";
    public static final String MOD_NAME = "Crock Pot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public CrockPot() {
        var modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        var modLoadingContext = ModLoadingContext.get();

        modEventBus.addListener(EventPriority.NORMAL, CrockPot::regCreativeTab);

        modLoadingContext.registerConfig(ModConfig.Type.COMMON, CrockPotConfigs.COMMON_CONFIG);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, CrockPotConfigs.CLIENT_CONFIG);

        CrockPotEffects.EFFECTS.register(modEventBus);
        CrockPotItems.ITEMS.register(modEventBus);
        CrockPotBlocks.BLOCKS.register(modEventBus);
        CrockPotBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        CrockPotMenuTypes.MENU_TYPES.register(modEventBus);
        CrockPotEntities.ENTITIES.register(modEventBus);
        CrockPotLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        CrockPotRecipes.RECIPE_TYPES.register(modEventBus);
        CrockPotRecipes.RECIPE_SERIALIZERS.register(modEventBus);
    }

    private static void regCreativeTab(CreativeModeTabEvent.Register event) {
        event.registerCreativeModeTab(new ResourceLocation(MOD_ID, "tab"), builder -> {
            builder.icon(() -> CrockPotItems.BASIC_CROCK_POT.get().getDefaultInstance())
                    .title(Component.translatable("itemGroup.crockpot"))
                    .displayItems((flagSet, output, hasPermission) -> {
                        output.acceptAll(CrockPotItems.ITEMS.getEntries().stream().map(regObj -> new ItemStack(regObj.get())).toList());
                    });
        });
    }
}
