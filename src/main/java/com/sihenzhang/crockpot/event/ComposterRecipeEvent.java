package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ComposterRecipeEvent {
    @SubscribeEvent
    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        // Add Composter recipes synchronously on the main thread after the parallel dispatch, fix GH-20
        event.enqueueWork(() -> {
            CrockPotItems.SEEDS.get().forEach(seed -> ComposterBlock.add(0.3F, seed));
            CrockPotItems.CROPS.get().forEach(crop -> ComposterBlock.add(0.65F, crop));
            CrockPotItems.COOKED_CROPS.get().forEach(cookedCrop -> ComposterBlock.add(0.85F, cookedCrop));
        });
    }
}
