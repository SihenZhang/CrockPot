package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class ComposterRecipeEvent {
    @SubscribeEvent
    public static void onLoadComplete(final FMLLoadCompleteEvent event) {
        // Add Composter recipes synchronously on the main thread after the parallel dispatch, fix GH-20
//        event.enqueueWork(() -> {
//            ModItems.SEEDS.get().forEach(seed -> ComposterBlock.add(0.3F, seed));
//            ModItems.CROPS.get().forEach(crop -> ComposterBlock.add(0.65F, crop));
//            ModItems.COOKED_CROPS.get().forEach(cookedCrop -> ComposterBlock.add(0.85F, cookedCrop));
//        });
        // TODO: Use Data Maps instead
    }
}
