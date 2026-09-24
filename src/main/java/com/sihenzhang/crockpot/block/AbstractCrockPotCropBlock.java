package com.sihenzhang.crockpot.block;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;

public abstract class AbstractCrockPotCropBlock extends CropBlock {
    public static final MapCodec<AbstractCrockPotCropBlock> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            BuiltInRegistries.ITEM.byNameCodec().fieldOf("seeds").forGetter(block -> block.getBaseSeedId().asItem()),
            propertiesCodec()
    ).apply(instance, AbstractCrockPotCropBlock::create));

    @Override
    public MapCodec<? extends AbstractCrockPotCropBlock> codec() {
        return CODEC;
    }

    private static AbstractCrockPotCropBlock create(Item seeds, Properties properties) {
        return new AbstractCrockPotCropBlock(properties) {
            @Override
            protected ItemLike getBaseSeedId() {
                return seeds;
            }
        };
    }

    protected AbstractCrockPotCropBlock() {
        this(Properties.of().noCollission().randomTicks().instabreak().sound(SoundType.CROP));
    }

    protected AbstractCrockPotCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected abstract ItemLike getBaseSeedId();
}
