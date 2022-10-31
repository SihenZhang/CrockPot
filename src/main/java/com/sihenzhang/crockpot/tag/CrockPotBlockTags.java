package com.sihenzhang.crockpot.tag;

import com.sihenzhang.crockpot.util.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class CrockPotBlockTags {
    public static final TagKey<Block> CROCK_POTS = TagUtils.createBlockTag("crock_pots");
    public static final TagKey<Block> UNKNOWN_CROPS = TagUtils.createBlockTag("unknown_crops");
}
