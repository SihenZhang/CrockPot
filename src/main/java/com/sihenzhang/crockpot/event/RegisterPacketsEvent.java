package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.network.PacketFoodCounter;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class RegisterPacketsEvent {
    @SubscribeEvent
    public static void registerPackets(RegisterPayloadHandlersEvent event) {
        event.registrar("1.0").playToClient(PacketFoodCounter.TYPE, PacketFoodCounter.STREAM_CODEC, PacketFoodCounter::handle);
    }
}
