package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.ModItems;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;

@EventBusSubscriber(modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModIntegrationCurios {
    public static final String MOD_ID = "curios";

    @SubscribeEvent
    public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
            CuriosCapabilityRegistry.register(event);
        }
    }

    static class CuriosCapabilityRegistry {
        public static void register(final RegisterCapabilitiesEvent event) {
            event.registerItem(CuriosCapability.ITEM, (stack, context) -> new MilkmadeHatCurios(stack, false), ModItems.MILKMADE_HAT);
            event.registerItem(CuriosCapability.ITEM, (stack, context) -> new MilkmadeHatCurios(stack, true), ModItems.CREATIVE_MILKMADE_HAT);
            event.registerItem(CuriosCapability.ITEM, (stack, context) -> new GnawsCoinCurios(stack), ModItems.GNAWS_COIN);
        }
    }

//    @SubscribeEvent
//    public static void onClientSetupEvent(FMLClientSetupEvent event) {
//        event.enqueueWork(() -> {
//            if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
//                CuriosRendererRegistry.register(ModItems.MILKMADE_HAT.get(), MilkmadeHatCurioRenderer::new);
//                CuriosRendererRegistry.register(ModItems.CREATIVE_MILKMADE_HAT.get(), MilkmadeHatCurioRenderer::new);
//            }
//        });
//    }
}
