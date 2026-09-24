package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class CrockPotEffectsEvent {
    @SubscribeEvent
    public static void onLivingEntityAttacked(final LivingIncomingDamageEvent event) {
        if (event.getEntity().isInWaterRainOrBubble()) {
            var source = event.getSource();
            if (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
                if (source.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasEffect(CrockPotEffects.CHARGE)) {
                    event.setAmount(event.getAmount() * 1.3F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWitherEffectApply(final MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect().is(MobEffects.WITHER) && event.getEntity().hasEffect(CrockPotEffects.WITHER_RESISTANCE)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void onWitherResistanceEffectAdded(final MobEffectEvent.Added event) {
        var livingEntity = event.getEntity();
        if (event.getEffectInstance().getEffect().is(CrockPotEffects.WITHER_RESISTANCE.getKey()) && livingEntity.hasEffect(MobEffects.WITHER)) {
            livingEntity.removeEffect(MobEffects.WITHER);
        }
    }

    @SubscribeEvent
    public static void onFoodRightClick(final PlayerInteractEvent.RightClickItem event) {
        var player = event.getEntity();
        if (player.hasEffect(CrockPotEffects.GNAWS_GIFT) && event.getItemStack().has(DataComponents.FOOD)) {
            player.startUsingItem(event.getHand());
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }
}
