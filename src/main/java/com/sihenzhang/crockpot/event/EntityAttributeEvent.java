package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.entity.VoltGoat;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class EntityAttributeEvent {
    @SubscribeEvent
    public static void onAttributeCreate(final EntityAttributeCreationEvent event) {
        event.put(ModEntities.VOLT_GOAT.get(), VoltGoat.createAttributes().build());
    }
}
