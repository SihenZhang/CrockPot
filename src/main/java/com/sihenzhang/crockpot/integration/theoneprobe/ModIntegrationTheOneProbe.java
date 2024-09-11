package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.CrockPot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.InterModComms;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.InterModEnqueueEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ModIntegrationTheOneProbe {
    public static final String MOD_ID = "theoneprobe";
    public static final String METHOD_NAME = "getTheOneProbe";

    @SubscribeEvent
    public static void sendIMCMessage(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(ModIntegrationTheOneProbe.MOD_ID)) {
            InterModComms.sendTo(ModIntegrationTheOneProbe.MOD_ID, ModIntegrationTheOneProbe.METHOD_NAME, CrockPotProbeInfoProvider::new);
            InterModComms.sendTo(ModIntegrationTheOneProbe.MOD_ID, ModIntegrationTheOneProbe.METHOD_NAME, BirdcageProbeInfoProvider::new);
        }
    }
}
