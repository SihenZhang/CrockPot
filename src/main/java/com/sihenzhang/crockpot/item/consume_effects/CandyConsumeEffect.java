package com.sihenzhang.crockpot.item.consume_effects;

import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.core.ModDamageTypes;
import com.sihenzhang.crockpot.util.I18nUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringUtil;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.consume_effects.ConsumeEffect;
import net.minecraft.world.level.Level;

import java.util.function.Consumer;
import java.util.function.Supplier;

public record CandyConsumeEffect() implements ConsumeEffect, TooltipProvider {
    public static final CandyConsumeEffect INSTANCE = new CandyConsumeEffect();
    public static final MapCodec<CandyConsumeEffect> CODEC = MapCodec.unit(INSTANCE);
    public static final StreamCodec<RegistryFriendlyByteBuf, CandyConsumeEffect> STREAM_CODEC = StreamCodec.unit(INSTANCE);

    private static final Supplier<MutableComponent> SPACE = () -> Component.literal("  ");
    private static final MutableComponent DELIMITER = Component.literal(", ").withStyle(ChatFormatting.GRAY);

    @Override
    public Type<CandyConsumeEffect> getType() {
        return ModConsumeEffects.CANDY.get();
    }

    @Override
    public boolean apply(Level level, ItemStack stack, LivingEntity user) {
        var chance = user.getRandom().nextFloat();
        if (chance < 0.25F) {
            return user.removeEffect(MobEffects.SLOWNESS);
        } else if (chance < 0.45F) {
            var a = user.removeEffect(MobEffects.HUNGER);
            var b = user.addEffect(new MobEffectInstance(MobEffects.SATURATION, 1, 1));
            return a || b;
        } else if (chance < 0.55F) {
            var a = user.removeEffect(MobEffects.MINING_FATIGUE);
            var b = user.addEffect(new MobEffectInstance(MobEffects.HASTE, 20 * 20));
            return a || b;
        } else if (chance < 0.6F) {
            var a = user.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 10 * 20));
            var b = hurtWithCandyDamage(level, user, 2.0F);
            return a || b;
        } else if (chance < 0.605F) {
            return hurtWithCandyDamage(level, user, 10.0F);
        }
        return false;
    }

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> consumer, TooltipFlag flag, DataComponentGetter components) {
        consumer.accept(I18nUtil.tooltip("effect.candy").withStyle(ChatFormatting.DARK_GREEN));
        consumer.accept(SPACE.get().append(I18nUtil.tooltip("effect.no_effect").withStyle(ChatFormatting.GRAY)));
        consumer.accept(SPACE.get().append(I18nUtil.tooltip("effect.remove", Component.translatable(MobEffects.SLOWNESS.value().getDescriptionId())).withStyle(ChatFormatting.GOLD)));
        consumer.accept(SPACE.get().append(Component.translatable("potion.withAmplifier", Component.translatable(MobEffects.SATURATION.value().getDescriptionId()), Component.translatable("potion.potency.1")).withStyle(ChatFormatting.BLUE)).append(DELIMITER).append(I18nUtil.tooltip("effect.remove", Component.translatable(MobEffects.HUNGER.value().getDescriptionId())).withStyle(ChatFormatting.GOLD)));
        consumer.accept(SPACE.get().append(Component.translatable("potion.withDuration", Component.translatable(MobEffects.HASTE.value().getDescriptionId()), StringUtil.formatTickDuration(20 * 20, context.tickRate())).withStyle(ChatFormatting.BLUE)).append(DELIMITER).append(I18nUtil.tooltip("effect.remove", Component.translatable(MobEffects.MINING_FATIGUE.value().getDescriptionId())).withStyle(ChatFormatting.GOLD)));
        consumer.accept(SPACE.get().append(Component.translatable("potion.withDuration", Component.translatable(MobEffects.WEAKNESS.value().getDescriptionId()), StringUtil.formatTickDuration(10 * 20, context.tickRate())).withStyle(ChatFormatting.RED)).append(DELIMITER).append(I18nUtil.tooltip("effect.damage.single", 1).withStyle(ChatFormatting.RED)));
        consumer.accept(SPACE.get().append(Component.literal("Damage5Hearts").withStyle(ChatFormatting.GRAY, ChatFormatting.OBFUSCATED)));
    }

    @SuppressWarnings("deprecation")
    private static boolean hurtWithCandyDamage(Level level, LivingEntity user, float amount) {
        return user.hurtOrSimulate(level.damageSources().source(ModDamageTypes.CANDY), amount);
    }
}
