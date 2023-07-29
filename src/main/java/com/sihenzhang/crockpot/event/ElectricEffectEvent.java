package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.effect.CrockPotEffects;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber()
public class ElectricEffectEvent {
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().isInWaterRainOrBubble()) {
            var source = event.getSource();
            if (source.is(DamageTypes.PLAYER_ATTACK) || source.is(DamageTypes.MOB_ATTACK) || source.is(DamageTypes.MOB_ATTACK_NO_AGGRO)) {
                if (source.getEntity() instanceof LivingEntity livingEntity && livingEntity.hasEffect(CrockPotEffects.ELECTRIC.get())) {
                    event.setAmount(event.getAmount() * 2.0F / 1.35F);
                }
            }
        }
    }
}
