package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.ModDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CrockPotDamageTypeTagsProvider extends DamageTypeTagsProvider {
    public CrockPotDamageTypeTagsProvider(PackOutput pOutput, CompletableFuture<HolderLookup.Provider> pLookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pLookupProvider, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(DamageTypeTags.BYPASSES_ARMOR).add(ModDamageTypes.CANDY, ModDamageTypes.MONSTER_FOOD, ModDamageTypes.POW_CAKE, ModDamageTypes.SPICY, ModDamageTypes.TAFFY);
        this.tag(DamageTypeTags.BYPASSES_EFFECTS).add(ModDamageTypes.CANDY, ModDamageTypes.MONSTER_FOOD, ModDamageTypes.POW_CAKE, ModDamageTypes.SPICY, ModDamageTypes.TAFFY);
    }
}
