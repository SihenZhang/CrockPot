package com.sihenzhang.crockpot.item.consume_effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.NumberUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public record HurtConsumeEffect(ResourceKey<DamageType> damageType, float damage) implements ConsumeEffect, TooltipProvider {
    public static final MapCodec<HurtConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(
                    ResourceKey.codec(Registries.DAMAGE_TYPE).fieldOf("damage_type").forGetter(HurtConsumeEffect::damageType),
                    ExtraCodecs.POSITIVE_FLOAT.fieldOf("damage").forGetter(HurtConsumeEffect::damage)
            ).apply(i, HurtConsumeEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HurtConsumeEffect> STREAM_CODEC = StreamCodec.composite(
            ResourceKey.streamCodec(Registries.DAMAGE_TYPE),
            HurtConsumeEffect::damageType,
            ByteBufCodecs.FLOAT,
            HurtConsumeEffect::damage,
            HurtConsumeEffect::new
    );

    @Override
    public Type<HurtConsumeEffect> getType() {
        return ModConsumeEffects.HURT.get();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        return user.hurtOrSimulate(level.damageSources().source(damageType), this.damage());
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (this.damage() > 0.0F) {
            var hearts = this.damage() / 2.0F;
            consumer.accept(I18nUtil.tooltip("effect.damage." + (NumberUtil.isClose(hearts, 1.0F) ? "single" : "multiple"), NumberUtil.decimalFormat("0.#", hearts)).withStyle(ChatFormatting.RED));
        }
    }
}
