package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        var generator = event.getGenerator();
        var helper = event.getExistingFileHelper();
        if (event.includeClient()) {
            var blockStateProvider = new CrockPotBlockStateProvider(generator, helper);
            generator.addProvider(blockStateProvider);
            generator.addProvider(new CrockPotItemModelProvider(generator, blockStateProvider.models().existingFileHelper));
        }
    }
}
