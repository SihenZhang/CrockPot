package com.sihenzhang.crockpot.world;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.block.CrockPotCropsBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CrockPotCropsFeatureConfig implements IFeatureConfig {
    public static final Codec<CrockPotCropsFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state_provider").forGetter(config -> config.cropsBlock.getDefaultState()),
            BlockState.CODEC.listOf().fieldOf("whitelist").orElse(ImmutableSet.of(Blocks.GRASS_BLOCK.getDefaultState()).asList()).forGetter(config -> config.whitelist.stream().map(Block::getDefaultState).collect(Collectors.toList())),
            BlockState.CODEC.fieldOf("replacement_block").orElse(Blocks.FARMLAND.getDefaultState()).forGetter(config -> config.replacementBlock.getDefaultState()),
            Codec.INT.fieldOf("tries").orElse(32).forGetter(config -> config.tryCount),
            Codec.INT.fieldOf("spread_radius").orElse(2).forGetter(config -> config.spreadRadius)
    ).apply(instance, CrockPotCropsFeatureConfig::new));

    public final CrockPotCropsBlock cropsBlock;
    public final Set<Block> whitelist;
    public final Block replacementBlock;
    public final int tryCount;
    public final int spreadRadius;

    private CrockPotCropsFeatureConfig(BlockState cropsBlock, List<BlockState> whitelist, BlockState replacedBlock, int tryCount, int spreadRadius) {
        this((CrockPotCropsBlock) cropsBlock.getBlock(), whitelist.stream().map(AbstractBlock.AbstractBlockState::getBlock).collect(Collectors.toSet()), replacedBlock.getBlock(), tryCount, spreadRadius);
    }

    private CrockPotCropsFeatureConfig(CrockPotCropsBlock cropsBlock, Set<Block> whitelist, Block replacementBlock, int tryCount, int spreadRadius) {
        this.cropsBlock = cropsBlock;
        this.whitelist = whitelist;
        this.replacementBlock = replacementBlock;
        this.tryCount = tryCount;
        this.spreadRadius = spreadRadius;
    }

    public static CrockPotCropsFeatureConfigBuilder builder(CrockPotCropsBlock cropsBlock) {
        return new CrockPotCropsFeatureConfigBuilder(cropsBlock);
    }

    public static class CrockPotCropsFeatureConfigBuilder {
        private final CrockPotCropsBlock cropsBlock;
        private Set<Block> whitelist = ImmutableSet.of(Blocks.GRASS_BLOCK.getBlock());
        private Block replacementBlock = Blocks.FARMLAND;
        private int tryCount = 64;
        private int spreadRadius = 2;

        public CrockPotCropsFeatureConfigBuilder(CrockPotCropsBlock cropsBlock) {
            this.cropsBlock = cropsBlock;
        }

        public CrockPotCropsFeatureConfigBuilder whitelist(Set<Block> whitelist) {
            this.whitelist = whitelist;
            return this;
        }

        public CrockPotCropsFeatureConfigBuilder replacedBy(Block replacementBlock) {
            this.replacementBlock = replacementBlock;
            return this;
        }

        public CrockPotCropsFeatureConfigBuilder tries(int tryCount) {
            this.tryCount = tryCount;
            return this;
        }

        public CrockPotCropsFeatureConfigBuilder spreadRadius(int spreadRadius) {
            this.spreadRadius = spreadRadius;
            return this;
        }

        public CrockPotCropsFeatureConfig build() {
            return new CrockPotCropsFeatureConfig(cropsBlock, whitelist, replacementBlock, tryCount, spreadRadius);
        }
    }
}
