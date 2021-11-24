package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.Optional;
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

    private static int id = 0;

    public static <MSG> void registerPacket(Class<MSG> msg, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder,
                                            BiConsumer<MSG, Supplier<NetworkEvent.Context>> handler, NetworkDirection direction) {
        INSTANCE.registerMessage(id++, msg, encoder, decoder, handler, Optional.of(direction));
    }

    public static <MSG> void sendToPlayer(ServerPlayerEntity player, MSG msg) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
    }

    public static <MSG> void sendToAll(MSG msg) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), msg);
    }
}
