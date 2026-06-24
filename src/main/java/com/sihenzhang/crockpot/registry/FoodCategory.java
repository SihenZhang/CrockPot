package com.sihenzhang.crockpot.registry;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;

public record FoodCategory(int color) {
    public static final Codec<FoodCategory> DIRECT_CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Codec.INT.fieldOf("color").forGetter(FoodCategory::color)
            ).apply(instance, FoodCategory::new)
    );
    public static final Codec<Holder<FoodCategory>> CODEC = RegistryFixedCodec.create(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY);
}
