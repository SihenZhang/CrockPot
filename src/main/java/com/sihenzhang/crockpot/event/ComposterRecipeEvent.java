package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.block.ComposterBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ComposterRecipeEvent {
    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        // Add Composter recipes synchronously on the main thread after the parallel dispatch, fix GH-20
        event.enqueueWork(() -> {
            CrockPotRegistry.seeds.forEach(seed -> ComposterBlock.add(0.3F, seed));
            CrockPotRegistry.crops.forEach(crop -> ComposterBlock.add(0.65F, crop));
            CrockPotRegistry.cookedCrops.forEach(cookedCrop -> ComposterBlock.add(0.85F, cookedCrop));
        });
    }
}
