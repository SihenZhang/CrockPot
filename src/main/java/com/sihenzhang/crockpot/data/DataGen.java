package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class DataGen {
    @SubscribeEvent
    public static void gatherData(final GatherDataEvent event) {
        var generator = event.getGenerator();
        var packOutput = generator.getPackOutput();
        var helper = event.getExistingFileHelper();
        // Server Side
        var providerFuture = event.getLookupProvider();
        var blockTagsProvider = new CrockPotBlockTagsProvider(packOutput, providerFuture, helper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new CrockPotItemTagsProvider(packOutput, providerFuture, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new CrockPotDamageTypeTagsProvider(packOutput, providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotDataMapProvider(packOutput, providerFuture));
        generator.addProvider(event.includeServer(), new CrockPotEntityTypeTagsProvider(packOutput, providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotAdvancementProvider(packOutput, providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotLootTableProvider(packOutput, providerFuture));
        generator.addProvider(event.includeServer(), new CrockPotGlobalLootModifierProvider(packOutput, providerFuture));
        generator.addProvider(event.includeServer(), new CrockPotRecipeProvider(packOutput, providerFuture));
        generator.addProvider(event.includeServer(), new CrockPotCuriosProvider(packOutput, providerFuture, helper));
        // Client Side
        var blockStateProvider = new CrockPotBlockStateProvider(packOutput, helper);
        generator.addProvider(event.includeClient(), blockStateProvider);
        generator.addProvider(event.includeClient(), new CrockPotItemModelProvider(packOutput, blockStateProvider.models().existingFileHelper));
        generator.addProvider(event.includeClient(), new CrockPotSoundDefinitionsProvider(packOutput, helper));
    }
}
