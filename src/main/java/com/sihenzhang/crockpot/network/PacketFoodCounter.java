package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFoodCounter {
    private final CompoundTag capabilityTag;

    public PacketFoodCounter(CompoundTag capabilityTag) {
        this.capabilityTag = capabilityTag;
    }

    public static void serialize(PacketFoodCounter pack, FriendlyByteBuf buf) {
        buf.writeNbt(pack.capabilityTag);
    }

    public static PacketFoodCounter deserialize(FriendlyByteBuf buf) {
        return new PacketFoodCounter(buf.readNbt());
    }

    public static void handle(PacketFoodCounter pack, Supplier<NetworkEvent.Context> ctx) {
        if (EffectiveSide.get().isClient()) {
            Handler.handle(pack, ctx);
        }
    }

    private static class Handler {
        static void handle(PacketFoodCounter pack, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                Player player = Minecraft.getInstance().player;
                if (player != null) {
                    player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> foodCounter.deserializeNBT(pack.capabilityTag));
                }
            });
            context.setPacketHandled(true);
        }
    }
}
