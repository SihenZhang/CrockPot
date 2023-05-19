package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class CrockPotBlockTagsProvider extends BlockTagsProvider {
    public CrockPotBlockTagsProvider(DataGenerator generator, CompletableFuture<HolderLookup.Provider> providerFuture, @Nullable ExistingFileHelper existingFileHelper) {
        super(generator.getPackOutput(), providerFuture, CrockPot.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Pot
        var pots = new Block[]{CrockPotBlocks.BASIC_CROCK_POT.get(), CrockPotBlocks.ADVANCED_CROCK_POT.get(), CrockPotBlocks.ULTIMATE_CROCK_POT.get()};
        this.tag(CrockPotBlockTags.CROCK_POTS).add(pots);
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(pots);

        // Birdcage
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(CrockPotBlocks.BIRDCAGE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(CrockPotBlocks.BIRDCAGE.get());

        // Crop
        var crops = new Block[]{CrockPotBlocks.ASPARAGUS.get(), CrockPotBlocks.CORN.get(), CrockPotBlocks.EGGPLANT.get(), CrockPotBlocks.GARLIC.get(), CrockPotBlocks.ONION.get(), CrockPotBlocks.PEPPER.get(), CrockPotBlocks.TOMATO.get()};
        this.tag(CrockPotBlockTags.UNKNOWN_CROPS).add(crops);
        this.tag(BlockTags.CROPS).add(CrockPotBlocks.UNKNOWN_CROPS.get()).add(crops);
    }

    @Override
    public String getName() {
        return "CrockPot Block Tags";
    }
}
