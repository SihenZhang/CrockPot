package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.ModBlocks;
import com.sihenzhang.crockpot.tag.ModBlockTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> providerFuture) {
        super(output, providerFuture, CrockPot.MOD_ID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Pot
        var pots = new Block[]{ModBlocks.CROCK_POT.get(), ModBlocks.PORTABLE_CROCK_POT.get()};
        this.tag(ModBlockTags.CROCK_POTS).add(pots);
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(pots);

        // Birdcage
        this.tag(BlockTags.MINEABLE_WITH_PICKAXE).add(ModBlocks.BIRDCAGE.get());
        this.tag(BlockTags.NEEDS_IRON_TOOL).add(ModBlocks.BIRDCAGE.get());

        // Drying Rack
        this.tag(BlockTags.MINEABLE_WITH_AXE).add(ModBlocks.DRYING_RACK.get());

        // Crop
        var crops = new Block[]{ModBlocks.ASPARAGUS.get(), ModBlocks.CORN.get(), ModBlocks.EGGPLANT.get(), ModBlocks.GARLIC.get(), ModBlocks.ONION.get(), ModBlocks.PEPPER.get(), ModBlocks.TOMATO.get()};
        this.tag(ModBlockTags.UNKNOWN_CROPS).add(crops);
        this.tag(BlockTags.CROPS).add(ModBlocks.UNKNOWN_CROPS.get()).add(crops);

        // Volt Goat
        this.tag(ModBlockTags.VOLT_GOATS_SPAWNABLE_ON).add(
                Blocks.TERRACOTTA, Blocks.WHITE_TERRACOTTA, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.BROWN_TERRACOTTA,
                Blocks.RED_TERRACOTTA, Blocks.ORANGE_TERRACOTTA, Blocks.YELLOW_TERRACOTTA, Blocks.RED_SAND
        );
    }

    @Override
    public String getName() {
        return "CrockPot Block Tags";
    }
}
