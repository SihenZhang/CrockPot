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
        if (event.includeServer()) {
            var blockTagsProvider = new CrockPotBlockTagsProvider(generator, helper);
            generator.addProvider(blockTagsProvider);
            generator.addProvider(new CrockPotItemTagsProvider(generator, blockTagsProvider, helper));
            generator.addProvider(new CrockPotEntityTypeTagsProvider(generator, helper));
            generator.addProvider(new CrockPotAdvancementProvider(generator, helper));
            generator.addProvider(new CrockPotLootTableProvider(generator));
            generator.addProvider(new CrockPotGlobalLootModifierProvider(generator));
            generator.addProvider(new CrockPotRecipeProvider(generator));
        }
        if (event.includeClient()) {
            var blockStateProvider = new CrockPotBlockStateProvider(generator, helper);
            generator.addProvider(blockStateProvider);
            generator.addProvider(new CrockPotItemModelProvider(generator, blockStateProvider.models().existingFileHelper));
        }
    }
}
