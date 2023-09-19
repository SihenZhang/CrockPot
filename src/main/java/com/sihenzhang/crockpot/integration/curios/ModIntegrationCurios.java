package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.integration.curios.renderer.MilkmadeHatCurioRenderer;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import top.theillusivec4.curios.api.client.CuriosRendererRegistry;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModIntegrationCurios {
    public static final String MOD_ID = "curios";

    @SubscribeEvent
    public static void onClientSetupEvent(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
                CuriosRendererRegistry.register(CrockPotItems.MILKMADE_HAT.get(), MilkmadeHatCurioRenderer::new);
                CuriosRendererRegistry.register(CrockPotItems.CREATIVE_MILKMADE_HAT.get(), MilkmadeHatCurioRenderer::new);
            }
        });
    }
}
