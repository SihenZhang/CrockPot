package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

public class CrockPotBlockTagsProvider extends BlockTagsProvider {
    public CrockPotBlockTagsProvider(DataGenerator generator, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        // Pot
        var pots = new Block[]{CrockPotRegistry.BASIC_CROCK_POT_BLOCK.get(), CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK.get(), CrockPotRegistry.ULTIMATE_CROCK_POT_BLOCK.get()};
        this.tag(CrockPotBlockTags.CROCK_POTS).add(pots);
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(pots);

        // Birdcage
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CrockPotRegistry.BIRDCAGE_BLOCK.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(CrockPotRegistry.BIRDCAGE_BLOCK.get());

        // Crop
        var crops = new Block[]{CrockPotRegistry.ASPARAGUS_BLOCK.get(), CrockPotRegistry.CORN_BLOCK.get(), CrockPotRegistry.EGGPLANT_BLOCK.get(), CrockPotRegistry.GARLIC_BLOCK.get(), CrockPotRegistry.ONION_BLOCK.get(), CrockPotRegistry.PEPPER_BLOCK.get(), CrockPotRegistry.TOMATO_BLOCK.get()};
        this.tag(CrockPotBlockTags.UNKNOWN_CROPS).add(crops);
        this.tag(BlockTags.CROPS).add(CrockPotRegistry.UNKNOWN_CROPS_BLOCK.get()).add(crops);
    }

    @Override
    public String getName() {
        return "CrockPot Block Tags";
    }
}
