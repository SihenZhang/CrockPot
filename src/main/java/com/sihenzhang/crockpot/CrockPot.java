package com.sihenzhang.crockpot;

import com.sihenzhang.crockpot.advancement.CrockPotCriteriaTriggers;
import com.sihenzhang.crockpot.base.CrockPotSoundEvents;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntities;
import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.inventory.CrockPotMenuTypes;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.loot.CrockPotLootModifiers;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(CrockPot.MOD_ID)
public final class CrockPot {
    public static final String MOD_ID = "crockpot";
    public static final String MOD_NAME = "Crock Pot";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MOD_ID))
            .icon(() -> CrockPotItems.CROCK_POT.get().getDefaultInstance())
            .displayItems((params, output) -> CrockPotItems.ITEMS.getEntries().forEach(regObj -> {
                var item = regObj.get();
                if (item != CrockPotItems.AVAJ.get()) {
                    output.accept(item);
                }
            }))
            .build()
    );

    public CrockPot(IEventBus modEventBus, ModContainer modLoadingContext) {

        modLoadingContext.registerConfig(ModConfig.Type.COMMON, CrockPotConfigs.COMMON_CONFIG);
        modLoadingContext.registerConfig(ModConfig.Type.CLIENT, CrockPotConfigs.CLIENT_CONFIG);

        CREATIVE_MODE_TABS.register(modEventBus);
        CrockPotCriteriaTriggers.TRIGGERS.register(modEventBus);
        FoodCounterCapabilityHandler.ATTACHMENT_TYPES.register(modEventBus);
        modEventBus.addListener(CrockPotBlockEntities::registerCapabilities);
        CrockPotEffects.EFFECTS.register(modEventBus);
        CrockPotItems.ITEMS.register(modEventBus);
        CrockPotBlocks.BLOCKS.register(modEventBus);
        CrockPotBlocks.BLOCK_TYPES.register(modEventBus);
        CrockPotBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        CrockPotMenuTypes.MENU_TYPES.register(modEventBus);
        CrockPotEntities.ENTITIES.register(modEventBus);
        CrockPotLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        CrockPotRecipes.RECIPE_TYPES.register(modEventBus);
        CrockPotRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        CrockPotSoundEvents.SOUND_EVENTS.register(modEventBus);
    }
}
