package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

public class CrockPotItemTagsProvider extends ItemTagsProvider {
    public CrockPotItemTagsProvider(DataGenerator generator, BlockTagsProvider blockTagsProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, blockTagsProvider, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        this.copy(CrockPotBlockTags.CROCK_POT, CrockPotItemTags.CROCK_POT);
        this.tag(CrockPotItemTags.MILKMADE_HAT).add(CrockPotRegistry.MILKMADE_HAT.get(), CrockPotRegistry.CREATIVE_MILKMADE_HAT.get());
    }

    @Override
    public String getName() {
        return "CrockPot Item Tags";
    }
}
