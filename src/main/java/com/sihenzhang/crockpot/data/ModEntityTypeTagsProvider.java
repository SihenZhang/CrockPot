package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.ModEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;

import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture) {
        super(output, providerFuture, CrockPot.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(ModEntities.PARROT_EGG.get());
    }

    @Override
    public String getName() {
        return "CrockPot Entity Type Tags";
    }
}
