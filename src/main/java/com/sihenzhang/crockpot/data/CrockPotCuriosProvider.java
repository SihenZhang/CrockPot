package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import top.theillusivec4.curios.api.CuriosDataProvider;

import java.util.concurrent.CompletableFuture;

public class CrockPotCuriosProvider extends CuriosDataProvider {
    public CrockPotCuriosProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper fileHelper) {
        super(CrockPot.MOD_ID, output, fileHelper, registries);
    }

    @Override
    public void generate(HolderLookup.Provider registries, ExistingFileHelper fileHelper) {
        this.createEntities("slots").addPlayer().addSlots("head", "charm");
    }
}
