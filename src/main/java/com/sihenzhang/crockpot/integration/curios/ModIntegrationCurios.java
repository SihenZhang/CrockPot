package com.sihenzhang.crockpot.integration.curios;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModIntegrationCurios {
    public static final String MOD_ID = "curios";

    @SubscribeEvent
    public static void sendIMCMessage(InterModEnqueueEvent event) {
        if (ModList.get().isLoaded(ModIntegrationCurios.MOD_ID)) {
            InterModComms.sendTo(ModIntegrationCurios.MOD_ID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.HEAD.getMessageBuilder().build());
            InterModComms.sendTo(ModIntegrationCurios.MOD_ID, SlotTypeMessage.REGISTER_TYPE, () -> SlotTypePreset.CURIO.getMessageBuilder().build());
        }
    }
}
