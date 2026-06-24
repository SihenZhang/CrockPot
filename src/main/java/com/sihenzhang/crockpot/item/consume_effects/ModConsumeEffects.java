package com.sihenzhang.crockpot.item.consume_effects;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public final class ModConsumeEffects {
    private ModConsumeEffects() {
    }

    public static final DeferredRegister<ConsumeEffect.Type<?>> CONSUME_EFFECT_TYPES = DeferredRegister.create(Registries.CONSUME_EFFECT_TYPE, CrockPot.MOD_ID);

    public static final Supplier<ConsumeEffect.Type<HealConsumeEffect>> HEAL = register(
            "heal",
            () -> new ConsumeEffect.Type<>(HealConsumeEffect.CODEC, HealConsumeEffect.STREAM_CODEC)
    );
    public static final Supplier<ConsumeEffect.Type<HurtConsumeEffect>> HURT = register(
            "hurt",
            () -> new ConsumeEffect.Type<>(HurtConsumeEffect.CODEC, HurtConsumeEffect.STREAM_CODEC)
    );
    public static final Supplier<ConsumeEffect.Type<CandyConsumeEffect>> CANDY = register(
            "candy",
            () -> new ConsumeEffect.Type<>(CandyConsumeEffect.CODEC, CandyConsumeEffect.STREAM_CODEC)
    );

    private static <T extends ConsumeEffect> Supplier<ConsumeEffect.Type<T>> register(String name, Supplier<ConsumeEffect.Type<T>> factory) {
        return CONSUME_EFFECT_TYPES.register(name, factory);
    }
}
