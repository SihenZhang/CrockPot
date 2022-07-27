package com.sihenzhang.crockpot.tag;

import com.sihenzhang.crockpot.util.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class CrockPotBlockTags {
    public static final TagKey<Block> CROCK_POT = TagUtils.createBlockTag("crock_pot");
    public static final TagKey<Block> UNKNOWN_CROPS = TagUtils.createBlockTag("unknown_crops");
}
