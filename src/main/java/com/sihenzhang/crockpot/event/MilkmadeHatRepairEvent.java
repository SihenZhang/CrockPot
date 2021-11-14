package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.MilkmadeHatItem;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class MilkmadeHatRepairEvent {
    @SubscribeEvent
    public static void onAnvilUpdate(AnvilUpdateEvent event) {
        if (event.getLeft().getItem() instanceof MilkmadeHatItem && event.getRight().getItem() instanceof MilkmadeHatItem) {
            event.setCanceled(true);
        }
    }
}
