package com.sihenzhang.crockpot.item.consume_effects;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.NumberUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;

public record HealConsumeEffect(float heal) implements ConsumeEffect, TooltipProvider {
    public static final MapCodec<HealConsumeEffect> CODEC = RecordCodecBuilder.mapCodec(
            i -> i.group(ExtraCodecs.POSITIVE_FLOAT.fieldOf("heal").forGetter(HealConsumeEffect::heal)).apply(i, HealConsumeEffect::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, HealConsumeEffect> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, HealConsumeEffect::heal, HealConsumeEffect::new
    );

    @Override
    public Type<HealConsumeEffect> getType() {
        return ModConsumeEffects.HEAL.get();
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        var healthBeforeHeal = user.getHealth();
        user.heal(heal);
        var healthAfterHeal = user.getHealth();
        return !NumberUtil.isClose(healthBeforeHeal, healthAfterHeal);
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        if (this.heal() > 0.0F) {
            var hearts = this.heal() / 2.0F;
            consumer.accept(I18nUtil.tooltip("effect.heal." + (NumberUtil.isClose(hearts, 1.0F) ? "single" : "multiple"), NumberUtil.decimalFormat("0.#", hearts)).withStyle(ChatFormatting.BLUE));
        }
    }
}
