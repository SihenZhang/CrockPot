package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.effect.ModEffects;
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
        if (event.getEntity().isInWaterOrRain()) {
            var source = event.getSource();
            if (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
                if (source.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasEffect(ModEffects.CHARGE)) {
                    event.setAmount(event.getAmount() * 1.3F);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onWitherEffectApply(final MobEffectEvent.Applicable event) {
        if (event.getEffectInstance().getEffect() == MobEffects.WITHER && event.getEntity().hasEffect(ModEffects.WITHER_RESISTANCE)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void onWitherResistanceEffectAdded(final MobEffectEvent.Added event) {
        var livingEntity = event.getEntity();
        if (event.getEffectInstance().getEffect() == ModEffects.WITHER_RESISTANCE && livingEntity.hasEffect(MobEffects.WITHER)) {
            livingEntity.removeEffect(MobEffects.WITHER);
        }
    }

    @SubscribeEvent
    public static void onFoodRightClick(final PlayerInteractEvent.RightClickItem event) {
        var player = event.getEntity();
        if (player.hasEffect(ModEffects.GNAWS_GIFT) && event.getItemStack().has(DataComponents.CONSUMABLE)) {
            player.startUsingItem(event.getHand());
            event.setCancellationResult(InteractionResult.CONSUME);
            event.setCanceled(true);
        }
    }
}
