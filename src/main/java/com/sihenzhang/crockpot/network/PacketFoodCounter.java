package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.capability.FoodCounterCapabilityHandler;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record PacketFoodCounter(CompoundTag capabilityTag) implements CustomPacketPayload {
    public static final Type<PacketFoodCounter> TYPE = new Type<>(RLUtils.createRL("food_counter"));
    public static final StreamCodec<FriendlyByteBuf, PacketFoodCounter> STREAM_CODEC = StreamCodec.of(
            (buf, packet) -> buf.writeNbt(packet.capabilityTag()),
            buf -> new PacketFoodCounter(buf.readNbt()));

    @Override
    public Type<PacketFoodCounter> type() {
        return TYPE;
    }

    public static void handle(PacketFoodCounter packet, IPayloadContext context) {
        FoodCounterCapabilityHandler.getFoodCounter(context.player()).deserializeNBT(packet.capabilityTag());
    }
}
