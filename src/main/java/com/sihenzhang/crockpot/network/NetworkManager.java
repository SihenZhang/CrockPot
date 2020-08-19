package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class NetworkManager {
    private static final String PROTOCOL_VERSION = "1.0";

    public static SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CrockPot.MOD_ID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private NetworkManager() {
        throw new UnsupportedOperationException("No instance");
    }

    public static void registerPackets() {
        registerPacket(
                PacketSyncCrockpotIngredients.class,
                PacketSyncCrockpotIngredients::serialize,
                PacketSyncCrockpotIngredients::deserialize,
                PacketSyncCrockpotIngredients::handle
        );
    }

    private static int id = 0;

    private static <MSG> void registerPacket(Class<MSG> msg, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder,
                                             BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler) {
        INSTANCE.registerMessage(id++, msg, encoder, decoder, handler);
    }
}
