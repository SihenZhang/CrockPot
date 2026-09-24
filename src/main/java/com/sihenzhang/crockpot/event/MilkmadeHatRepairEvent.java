package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AnvilUpdateEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class MilkmadeHatRepairEvent {
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof MilkmadeHatItem && event.getRight().getItem() instanceof MilkmadeHatItem) {
            event.setCanceled(true);
        }
    }
}
