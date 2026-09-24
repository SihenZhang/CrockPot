package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.curios.renderer.MilkmadeHatCurioRenderer;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ModIntegrationCurios {
    public static final String MOD_ID = "curios";

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        if (ModList.get().isLoaded(MOD_ID)) {
            event.registerItem(CuriosCapability.ITEM, (stack, context) -> new GnawsCoinCuriosCapabilityProvider(stack), CrockPotItems.GNAWS_COIN.get());
            event.registerItem(CuriosCapability.ITEM, (stack, context) -> new MilkmadeHatCuriosCapabilityProvider(stack, false), CrockPotItems.MILKMADE_HAT.get());
            event.registerItem(CuriosCapability.ITEM, (stack, context) -> new MilkmadeHatCuriosCapabilityProvider(stack, true), CrockPotItems.CREATIVE_MILKMADE_HAT.get());
        }
    }

    @EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
    public static class ClientEvents {
        @SubscribeEvent
        public static void onClientSetupEvent(FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                if (ModList.get().isLoaded(MOD_ID)) {
                    CuriosRendererRegistry.register(CrockPotItems.MILKMADE_HAT.get(), MilkmadeHatCurioRenderer::new);
                    CuriosRendererRegistry.register(CrockPotItems.CREATIVE_MILKMADE_HAT.get(), MilkmadeHatCurioRenderer::new);
                }
            });
        }
    }
}
