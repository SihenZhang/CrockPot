package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.CrockPot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class NetworkManager {
    @SubscribeEvent
    public static void register(final RegisterPayloadHandlersEvent event) {
        final var registrar = event.registrar("1");
        registrar.playToClient(FoodCounterPacketPayload.TYPE, FoodCounterPacketPayload.STREAM_CODEC, FoodCounterPacketPayload.Handler::handleData);
    }
}
