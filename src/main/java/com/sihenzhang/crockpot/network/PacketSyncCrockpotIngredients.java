package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncCrockpotIngredients {
    private final String data;

    public PacketSyncCrockpotIngredients(String data) {
        this.data = data;
    }

    public static void serialize(PacketSyncCrockpotIngredients pack, PacketBuffer buf) {
        buf.writeString(pack.data);
    }

    public static PacketSyncCrockpotIngredients deserialize(PacketBuffer buf) {
        return new PacketSyncCrockpotIngredients(buf.readString());
    }

    public static void handle(PacketSyncCrockpotIngredients pack, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection() == NetworkDirection.PLAY_TO_CLIENT) {
            CrockPot.INGREDIENT_MANAGER.deserialize(pack.data);
        } else {
            ctx.get().getNetworkManager().getNetHandler().onDisconnect(new StringTextComponent("Hello, what are you doing?"));
        }
        ctx.get().setPacketHandled(true);
    }
}
