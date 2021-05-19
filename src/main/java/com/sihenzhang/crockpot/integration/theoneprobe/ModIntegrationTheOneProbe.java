package com.sihenzhang.crockpot.integration.theoneprobe;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModIntegrationTheOneProbe {
    public static final String MOD_ID = "theoneprobe";
    public static final String METHOD_NAME = "getTheOneProbe";

    @SubscribeEvent
    public static void sendIMCMessage(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(ModIntegrationTheOneProbe.MOD_ID)) {
            InterModComms.sendTo(ModIntegrationTheOneProbe.MOD_ID, ModIntegrationTheOneProbe.METHOD_NAME, CrockPotProbeInfoProvider::new);
        }
    }
}
