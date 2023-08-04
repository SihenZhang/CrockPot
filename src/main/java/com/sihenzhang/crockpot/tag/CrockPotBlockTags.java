package com.sihenzhang.crockpot.tag;

import com.sihenzhang.crockpot.util.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class CrockPotBlockTags {
    private CrockPotBlockTags() {
    }

    public static final TagKey<Block> CROCK_POTS = TagUtils.createBlockTag("crock_pots");
    public static final TagKey<Block> UNKNOWN_CROPS = TagUtils.createBlockTag("unknown_crops");

    public static final TagKey<Block> VOLT_GOATS_SPAWNABLE_ON = TagUtils.createBlockTag("volt_goats_spawnable_on");
}
