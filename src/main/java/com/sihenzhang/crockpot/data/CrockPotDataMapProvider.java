package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.concurrent.CompletableFuture;

public class CrockPotDataMapProvider extends DataMapProvider {
    protected CrockPotDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather() {
        var compostables = this.builder(NeoForgeDataMaps.COMPOSTABLES);

        ModItems.SEEDS.get().forEach(seed -> compostables.add(seed.builtInRegistryHolder(), new Compostable(0.3F), false));
        ModItems.CROPS.get().forEach(seed -> compostables.add(seed.builtInRegistryHolder(), new Compostable(0.65F), false));
        ModItems.COOKED_CROPS.get().forEach(seed -> compostables.add(seed.builtInRegistryHolder(), new Compostable(0.85F), false));
    }

    @Override
    public String getName() {
        return "Crock Pot Data Maps";
    }
}
