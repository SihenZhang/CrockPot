package com.sihenzhang.crockpot.network;

import com.sihenzhang.crockpot.attachment.ModAttachmentTypes;
import com.sihenzhang.crockpot.util.RLUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.HashMap;
import java.util.Map;

public record FoodCounterPacketPayload(Map<Item, Integer> map) implements CustomPacketPayload {
    public static final Type<FoodCounterPacketPayload> TYPE = new Type<>(RLUtils.mod("food_counter"));

    public static final StreamCodec<RegistryFriendlyByteBuf, FoodCounterPacketPayload> STREAM_CODEC = StreamCodec.of(
            FoodCounterPacketPayload::toNetwork, FoodCounterPacketPayload::fromNetwork
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static class Handler {
        public static void handleData(final FoodCounterPacketPayload data, final IPayloadContext context) {
            context.enqueueWork(() -> {
                var foodCounter = context.player().getData(ModAttachmentTypes.FOOD_COUNTER);
                foodCounter.clear();
                data.map().forEach(foodCounter::setCount);
            });
        }
    }

    private static FoodCounterPacketPayload fromNetwork(RegistryFriendlyByteBuf buffer) {
        var map = new HashMap<Item, Integer>();
        var length = buffer.readVarInt();
        for (var i = 0; i < length; i++) {
            var item = ByteBufCodecs.registry(Registries.ITEM).decode(buffer);
            var count = buffer.readVarInt();
            map.put(item, count);
        }
        return new FoodCounterPacketPayload(map);
    }

    private static void toNetwork(RegistryFriendlyByteBuf buffer, FoodCounterPacketPayload value) {
        var map = value.map();
        buffer.writeVarInt(map.size());
        map.forEach((item, count) -> {
            ByteBufCodecs.registry(Registries.ITEM).encode(buffer, item);
            buffer.writeVarInt(count);
        });
    }
}
