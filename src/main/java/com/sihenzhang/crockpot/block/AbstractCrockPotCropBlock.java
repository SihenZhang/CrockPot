package com.sihenzhang.crockpot.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.Material;

public abstract class AbstractCrockPotCropBlock extends CropBlock {
    protected AbstractCrockPotCropBlock() {
        super(Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP));
    }

    @Override
    protected abstract ItemLike getBaseSeedId();
}
