package com.sihenzhang.crockpot.block;

import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

public abstract class AbstractCrockPotCropBlock extends CropBlock {
    protected AbstractCrockPotCropBlock() {
        super(Properties.of().mapColor(MapColor.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.CROP).pushReaction(PushReaction.DESTROY));
    }

    @Override
    protected abstract ItemLike getBaseSeedId();
}
