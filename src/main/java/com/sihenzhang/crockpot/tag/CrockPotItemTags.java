package com.sihenzhang.crockpot.tag;

import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import com.sihenzhang.crockpot.util.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class CrockPotItemTags {
    public static final TagKey<Item> CROCK_POT = TagUtils.createItemTag("crock_pot");
    public static final TagKey<Item> MILKMADE_HAT = TagUtils.createItemTag("milkmade_hat");

    public static final TagKey<Item> CURIO = TagUtils.createItemTag(ModIntegrationCurios.MOD_ID, "curio");
    public static final TagKey<Item> HEAD = TagUtils.createItemTag(ModIntegrationCurios.MOD_ID, "head");
}
