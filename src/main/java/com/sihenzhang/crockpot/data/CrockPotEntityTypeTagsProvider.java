package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class CrockPotEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public CrockPotEntityTypeTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.tag(EntityTypeTags.IMPACT_PROJECTILES).add(CrockPotEntities.PARROT_EGG.get());
    }

    @Override
    public String getName() {
        return "CrockPot Entity Type Tags";
    }
}
