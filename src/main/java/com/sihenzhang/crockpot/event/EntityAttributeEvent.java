package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import com.sihenzhang.crockpot.entity.VoltGoat;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class EntityAttributeEvent {
    @SubscribeEvent
    public static void onAttributeCreate(final EntityAttributeCreationEvent event) {
        event.put(CrockPotEntities.VOLT_GOAT.get(), VoltGoat.createAttributes().build());
    }
}
