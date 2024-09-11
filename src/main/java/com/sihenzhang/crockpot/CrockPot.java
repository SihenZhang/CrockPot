package com.sihenzhang.crockpot;

import com.mojang.logging.LogUtils;
import com.sihenzhang.crockpot.advancement.ModCriterionTriggers;
import com.sihenzhang.crockpot.attachment.ModAttachmentTypes;
import com.sihenzhang.crockpot.base.ModSoundEvents;
import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.block.entity.ModBlockEntities;
import com.sihenzhang.crockpot.effect.ModEffects;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.inventory.ModMenuTypes;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.loot.ModLootModifiers;
import com.sihenzhang.crockpot.recipe.ModRecipes;
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

@Mod(CrockPot.MOD_ID)
public final class CrockPot {
    public static final String MOD_ID = "crockpot";
    public static final String MOD_NAME = "Crock Pot";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tab", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup." + MOD_ID))
            .icon(() -> ModItems.CROCK_POT.get().getDefaultInstance())
            .displayItems((params, output) -> ModItems.ITEMS.getEntries().forEach(regObj -> {
                var item = regObj.get();
                if (item != ModItems.AVAJ.get()) {
                    output.accept(item);
                }
            }))
            .build()
    );

    public CrockPot(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, CrockPotConfigs.COMMON_CONFIG);
        modContainer.registerConfig(ModConfig.Type.CLIENT, CrockPotConfigs.CLIENT_CONFIG);

        CREATIVE_MODE_TABS.register(modEventBus);
        ModAttachmentTypes.ATTACHMENT_TYPES.register(modEventBus);
        ModCriterionTriggers.TRIGGERS.register(modEventBus);
        ModEffects.EFFECTS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);
        ModBlockEntities.BLOCK_ENTITIES.register(modEventBus);
        ModMenuTypes.MENU_TYPES.register(modEventBus);
        ModEntities.ENTITIES.register(modEventBus);
        ModLootModifiers.LOOT_MODIFIERS.register(modEventBus);
        ModRecipes.RECIPE_TYPES.register(modEventBus);
        ModRecipes.RECIPE_SERIALIZERS.register(modEventBus);
        ModSoundEvents.SOUND_EVENTS.register(modEventBus);
    }
}
