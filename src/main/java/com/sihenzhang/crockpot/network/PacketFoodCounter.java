package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.thread.EffectiveSide;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketFoodCounter {
    private final CompoundNBT capabilityTag;

    public PacketFoodCounter(CompoundNBT capabilityTag) {
        this.capabilityTag = capabilityTag;
    }

    public static void serialize(PacketFoodCounter pack, PacketBuffer buf) {
        buf.writeNbt(pack.capabilityTag);
    }

    public static PacketFoodCounter deserialize(PacketBuffer buf) {
        return new PacketFoodCounter(buf.readNbt());
    }

    public static void handle(PacketFoodCounter pack, Supplier<NetworkEvent.Context> ctx) {
        if (EffectiveSide.get().isClient()) {
            PacketFoodCounter.Handler.handle(pack, ctx);
        }
    }

    private static class Handler {
        static void handle(PacketFoodCounter pack, Supplier<NetworkEvent.Context> ctx) {
            NetworkEvent.Context context = ctx.get();
            context.enqueueWork(() -> {
                PlayerEntity player = Minecraft.getInstance().player;
                if (player != null) {
                    player.getCapability(FoodCounterCapabilityHandler.FOOD_COUNTER_CAPABILITY).ifPresent(foodCounter -> foodCounter.deserializeNBT(pack.capabilityTag));
                }
            });
            context.setPacketHandled(true);
        }
    }
}
