package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.effect.CrockPotMobEffects;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class WitherResistancePotionEffectEvent {
    @SubscribeEvent
    public static void onWitherPotionApply(PotionEvent.PotionApplicableEvent event) {
        // Avoid adding wither effect to entity
        if (event.getPotionEffect().getEffect() == MobEffects.WITHER && event.getEntityLiving().hasEffect(CrockPotMobEffects.WITHER_RESISTANCE.get())) {
            event.setResult(Event.Result.DENY);
        }
    }

    @SubscribeEvent
    public static void onWitherResistancePotionAdded(PotionEvent.PotionAddedEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        // Remove exist wither effect
        if (event.getPotionEffect().getEffect() == CrockPotMobEffects.WITHER_RESISTANCE.get() && livingEntity.hasEffect(MobEffects.WITHER)) {
            livingEntity.removeEffect(MobEffects.WITHER);
        }
    }
}
