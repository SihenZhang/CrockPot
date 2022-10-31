package com.sihenzhang.crockpot.tag;

import com.sihenzhang.crockpot.integration.curios.ModIntegrationCurios;
import com.sihenzhang.crockpot.util.TagUtils;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public final class CrockPotItemTags {
    public static final TagKey<Item> CROCK_POTS = TagUtils.createItemTag("crock_pots");
    public static final TagKey<Item> MILKMADE_HATS = TagUtils.createItemTag("milkmade_hats");
    public static final TagKey<Item> PARROT_EGGS = TagUtils.createItemTag("parrot_eggs");

    public static final TagKey<Item> CURIO = TagUtils.createItemTag(ModIntegrationCurios.MOD_ID, "curio");
    public static final TagKey<Item> HEAD = TagUtils.createItemTag(ModIntegrationCurios.MOD_ID, "head");
}
