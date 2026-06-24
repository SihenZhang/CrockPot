package com.sihenzhang.crockpot.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;

public abstract class AbstractCropBlock extends CropBlock {
    protected AbstractCropBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected abstract ItemLike getBaseSeedId();
}
