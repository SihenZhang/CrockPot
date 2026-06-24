package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.core.ModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;

import java.util.concurrent.CompletableFuture;

public class ModDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public ModDamageTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, CrockPot.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).addOptional(ModDamageTypes.CANDY).addOptional(ModDamageTypes.MONSTER_FOOD).addOptional(ModDamageTypes.POW_CAKE).addOptional(ModDamageTypes.SPICY).addOptional(ModDamageTypes.TAFFY);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).addOptional(ModDamageTypes.CANDY).addOptional(ModDamageTypes.MONSTER_FOOD).addOptional(ModDamageTypes.POW_CAKE).addOptional(ModDamageTypes.SPICY).addOptional(ModDamageTypes.TAFFY);
    }
}
