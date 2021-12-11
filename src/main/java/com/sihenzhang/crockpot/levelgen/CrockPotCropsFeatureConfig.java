package com.sihenzhang.crockpot.levelgen;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.block.AbstractCrockPotCropBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class CrockPotCropsFeatureConfig implements FeatureConfiguration {
    public static final Codec<CrockPotCropsFeatureConfig> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BlockState.CODEC.fieldOf("state_provider").forGetter(config -> config.cropsBlock.defaultBlockState()),
            BlockState.CODEC.listOf().fieldOf("whitelist").orElse(ImmutableSet.of(Blocks.GRASS_BLOCK.defaultBlockState()).asList()).forGetter(config -> config.whitelist.stream().map(Block::defaultBlockState).collect(Collectors.toList())),
            BlockState.CODEC.fieldOf("replacement_block").orElse(Blocks.FARMLAND.defaultBlockState()).forGetter(config -> config.replacementBlock.defaultBlockState()),
            Codec.INT.fieldOf("tries").orElse(32).forGetter(config -> config.tryCount),
            Codec.INT.fieldOf("spread_radius").orElse(2).forGetter(config -> config.spreadRadius)
    ).apply(instance, CrockPotCropsFeatureConfig::new));

    public final AbstractCrockPotCropBlock cropsBlock;
    public final Set<Block> whitelist;
    public final Block replacementBlock;
    public final int tryCount;
    public final int spreadRadius;

    private CrockPotCropsFeatureConfig(BlockState cropsBlock, List<BlockState> whitelist, BlockState replacedBlock, int tryCount, int spreadRadius) {
        this((AbstractCrockPotCropBlock) cropsBlock.getBlock(), whitelist.stream().map(BlockBehaviour.BlockStateBase::getBlock).collect(Collectors.toSet()), replacedBlock.getBlock(), tryCount, spreadRadius);
    }

    private CrockPotCropsFeatureConfig(AbstractCrockPotCropBlock cropsBlock, Set<Block> whitelist, Block replacementBlock, int tryCount, int spreadRadius) {
        this.cropsBlock = cropsBlock;
        this.whitelist = whitelist;
        this.replacementBlock = replacementBlock;
        this.tryCount = tryCount;
        this.spreadRadius = spreadRadius;
    }

    public static CrockPotCropsFeatureConfigBuilder builder(AbstractCrockPotCropBlock cropsBlock) {
        return new CrockPotCropsFeatureConfigBuilder(cropsBlock);
    }

    public static class CrockPotCropsFeatureConfigBuilder {
        private final AbstractCrockPotCropBlock cropsBlock;
        private Set<Block> whitelist = ImmutableSet.of(Blocks.GRASS_BLOCK);
        private Block replacementBlock = Blocks.FARMLAND;
        private int tryCount = 64;
        private int spreadRadius = 2;

        public CrockPotCropsFeatureConfigBuilder(AbstractCrockPotCropBlock cropsBlock) {
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
