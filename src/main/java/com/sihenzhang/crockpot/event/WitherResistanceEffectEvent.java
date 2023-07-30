package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.effect.CrockPotEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.MobEffectEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class WitherResistanceEffectEvent {
    @SubscribeEvent
    public static void onWitherPotionApply(final MobEffectEvent.Applicable event) {
        // Avoid adding wither effect to entity
        if (event.getEffectInstance().getEffect() == MobEffects.WITHER && event.getEntity().hasEffect(CrockPotEffects.WITHER_RESISTANCE.get())) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onWitherResistancePotionAdded(final MobEffectEvent.Added event) {
        var livingEntity = event.getEntity();
        // Remove exist wither effect
        if (event.getEffectInstance().getEffect() == CrockPotEffects.WITHER_RESISTANCE.get() && livingEntity.hasEffect(MobEffects.WITHER)) {
            livingEntity.removeEffect(MobEffects.WITHER);
        }
    }
}
