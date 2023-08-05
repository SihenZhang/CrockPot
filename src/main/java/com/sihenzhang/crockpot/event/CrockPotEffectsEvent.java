package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class CrockPotEffectsEvent {
    @SubscribeEvent
    public static void onLivingEntityAttacked(final LivingHurtEvent event) {
        if (event.getEntity().isInWaterRainOrBubble()) {
            var source = event.getSource();
            if (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
                if (source.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasEffect(CrockPotEffects.CHARGE.get())) {
                    event.setAmount(event.getAmount() * 1.3F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWitherEffectApply(final MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect() == MobEffects.WITHER && event.getEntity().hasEffect(CrockPotEffects.WITHER_RESISTANCE.get())) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onWitherResistanceEffectAdded(final MobEffectEvent.Added event) {
        var livingEntity = event.getEntity();
        if (event.getEffectInstance().getEffect() == CrockPotEffects.WITHER_RESISTANCE.get() && livingEntity.hasEffect(MobEffects.WITHER)) {
            livingEntity.removeEffect(MobEffects.WITHER);
        }
    }

    @SubscribeEvent
    public static void onFoodRightClick(final PlayerInteractEvent.RightClickItem event) {
        var player = event.getEntity();
        if (player.hasEffect(CrockPotEffects.GNAWS_GIFT.get()) && event.getItemStack().isEdible()) {
            player.startUsingItem(event.getHand());
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }
}
