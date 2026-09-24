package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.JsonOps;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public final class RecipeCodecs {
    private RecipeCodecs() {
    }

    public static final Codec<ItemStack> RESULT = Codec.withAlternative(ItemStack.CODEC,
            json(json -> JsonUtils.convertToItemStack(json, "result"),
                    stack -> ItemStack.CODEC.encodeStart(JsonOps.INSTANCE, stack).getOrThrow()));

    public static <T> Codec<T> json(Function<JsonElement, T> reader, Function<T, JsonElement> writer) {
        return Codec.PASSTHROUGH.comapFlatMap(dynamic -> {
            try {
                return DataResult.success(reader.apply(dynamic.convert(JsonOps.INSTANCE).getValue()));
            } catch (RuntimeException exception) {
                return DataResult.error(exception::getMessage);
            }
        }, value -> new Dynamic<>(JsonOps.INSTANCE, writer.apply(value)));
    }

    public static <R> com.mojang.serialization.MapCodec<R> object(Function<JsonElement, R> reader,
                                                                  Function<R, JsonElement> writer, String... fields) {
        return new com.mojang.serialization.MapCodec<>() {
            @Override
            public <T> java.util.stream.Stream<T> keys(com.mojang.serialization.DynamicOps<T> ops) {
                return java.util.Arrays.stream(fields).map(ops::createString);
            }

            @Override
            public <T> DataResult<R> decode(com.mojang.serialization.DynamicOps<T> ops, com.mojang.serialization.MapLike<T> input) {
                try {
                    return DataResult.success(reader.apply(ops.convertTo(JsonOps.INSTANCE, ops.createMap(input.entries()))));
                } catch (RuntimeException exception) {
                    return DataResult.error(exception::getMessage);
                }
            }

            @Override
            public <T> com.mojang.serialization.RecordBuilder<T> encode(R input, com.mojang.serialization.DynamicOps<T> ops,
                                                                        com.mojang.serialization.RecordBuilder<T> prefix) {
                writer.apply(input).getAsJsonObject().entrySet().forEach(entry ->
                        prefix.add(entry.getKey(), JsonOps.INSTANCE.convertTo(ops, entry.getValue())));
                return prefix;
            }
        };
    }

}
