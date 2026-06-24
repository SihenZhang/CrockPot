package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class ModDataMapProvider extends DataMapProvider {
    protected ModDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var compostableItemBuilder = this.builder(NeoForgeDataMaps.COMPOSTABLES);
        ModItems.SEEDS.get().forEach(seed -> compostableItemBuilder.add(seed.builtInRegistryHolder(), new Compostable(0.3F), false));
        ModItems.CROPS.get().forEach(crop -> compostableItemBuilder.add(crop.builtInRegistryHolder(), new Compostable(0.65F), false));
        ModItems.COOKED_CROPS.get().forEach(cookedCrop -> compostableItemBuilder.add(cookedCrop.builtInRegistryHolder(), new Compostable(0.85F), false));
    }
}
