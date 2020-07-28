package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.block.Block;
import net.minecraft.item.BlockNamedItem;
import net.minecraft.item.Food;

public class CrockPotCropsBlockItem extends BlockNamedItem {
    public CrockPotCropsBlockItem(Block blockIn) {
        super(blockIn, new Properties().group(CrockPot.ITEM_GROUP));
    }

    public CrockPotCropsBlockItem(Block blockIn, int hunger, float saturation) {
        super(blockIn, new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).build()));
    }
}
