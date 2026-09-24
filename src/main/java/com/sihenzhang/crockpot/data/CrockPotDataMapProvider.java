package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class CrockPotDataMapProvider extends DataMapProvider {
    public CrockPotDataMapProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture) {
        super(output, providerFuture);
    }

    @Override
    protected void gather(HolderLookup.Provider provider) {
        var compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);
        CrockPotItems.SEEDS.get().forEach(seed -> compostables.add(seed.builtInRegistryHolder(), new Compostable(0.3F), false));
        CrockPotItems.CROPS.get().forEach(crop -> compostables.add(crop.builtInRegistryHolder(), new Compostable(0.65F), false));
        CrockPotItems.COOKED_CROPS.get().forEach(cookedCrop -> compostables.add(cookedCrop.builtInRegistryHolder(), new Compostable(0.85F), false));
    }
}
