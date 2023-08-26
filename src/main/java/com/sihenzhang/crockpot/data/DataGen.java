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
        // Server Side
        var providerFuture = event.getLookupProvider();
        var blockTagsProvider = new CrockPotBlockTagsProvider(generator, providerFuture, helper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(event.includeServer(), new CrockPotItemTagsProvider(generator.getPackOutput(), providerFuture, blockTagsProvider.contentsGetter(), helper));
        generator.addProvider(event.includeServer(), new CrockPotDamageTypeTagsProvider(generator.getPackOutput(), providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotEntityTypeTagsProvider(generator.getPackOutput(), providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotAdvancementProvider(generator.getPackOutput(), providerFuture, helper));
        generator.addProvider(event.includeServer(), new CrockPotLootTableProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new CrockPotGlobalLootModifierProvider(generator.getPackOutput()));
        generator.addProvider(event.includeServer(), new CrockPotRecipeProvider(generator.getPackOutput()));
        // Client Side
        var blockStateProvider = new CrockPotBlockStateProvider(generator.getPackOutput(), helper);
        generator.addProvider(event.includeClient(), blockStateProvider);
        generator.addProvider(event.includeClient(), new CrockPotItemModelProvider(generator.getPackOutput(), blockStateProvider.models().existingFileHelper));
    }
}
