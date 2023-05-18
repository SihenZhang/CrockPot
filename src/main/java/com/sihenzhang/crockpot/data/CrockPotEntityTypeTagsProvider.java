package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CrockPotEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public CrockPotEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, providerFuture, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(CrockPotEntities.PARROT_EGG.get());
    }

    @Override
    public String getName() {
        return "CrockPot Entity Type Tags";
    }
}
