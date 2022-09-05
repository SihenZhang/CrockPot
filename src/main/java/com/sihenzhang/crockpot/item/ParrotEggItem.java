package com.sihenzhang.crockpot.item;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.world.item.Item;

import java.util.List;

public class ParrotEggItem extends Item {
    public static final List<Pair<Integer, String>> VARIANT_NAMES = ImmutableList.of(
            Pair.of(0, "red_blue"),
            Pair.of(1, "blue"),
            Pair.of(2, "green"),
            Pair.of(3, "yellow_blue"),
            Pair.of(4, "grey")
    );

    private final int variant;

    public ParrotEggItem(int variant) {
        super(new Properties().tab(CrockPot.ITEM_GROUP));
        this.variant = variant;
    }

    public int getVariant() {
        return variant;
    }
}
