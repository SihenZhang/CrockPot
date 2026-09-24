package com.sihenzhang.crockpot.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;

public final class NetworkManager {
    private NetworkManager() {
        throw new UnsupportedOperationException("No instance");
    }

    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload msg) {
        PacketDistributor.sendToPlayer(player, msg);
    }

    public static void sendToAll(CustomPacketPayload msg) {
        PacketDistributor.sendToAllPlayers(msg);
    }
}
