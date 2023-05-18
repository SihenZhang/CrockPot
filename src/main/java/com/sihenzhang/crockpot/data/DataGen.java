package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        var generator = event.getGenerator();
        var helper = event.getExistingFileHelper();
        // TODO Pull all addProvider call out of if-block
        if (event.includeServer()) {
            var blockTagsProvider = new CrockPotBlockTagsProvider(generator, helper);
            generator.addProvider(event.includeServer(), blockTagsProvider);
            generator.addProvider(event.includeServer(), new CrockPotItemTagsProvider(generator, blockTagsProvider, helper));
            generator.addProvider(event.includeServer(), new CrockPotEntityTypeTagsProvider(generator, helper));
            generator.addProvider(event.includeServer(), new CrockPotAdvancementProvider(generator, helper));
            generator.addProvider(event.includeServer(), new CrockPotLootTableProvider(generator));
            generator.addProvider(event.includeServer(), new CrockPotGlobalLootModifierProvider(generator));
            generator.addProvider(event.includeServer(), new CrockPotRecipeProvider(generator));
        }
        if (event.includeClient()) {
            var blockStateProvider = new CrockPotBlockStateProvider(generator, helper);
            generator.addProvider(event.includeClient(), blockStateProvider);
            generator.addProvider(event.includeClient(), new CrockPotItemModelProvider(generator, blockStateProvider.models().existingFileHelper));
        }
    }
}
