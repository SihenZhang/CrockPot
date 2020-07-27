package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Food;

public class CrockPotCropsBlockItem extends BlockItem {
    public CrockPotCropsBlockItem(Block blockIn, int hunger, float saturation) {
        super(blockIn, new Properties().group(CrockPot.ITEM_GROUP).food(new Food.Builder().hunger(hunger).saturation(saturation).build()));
    }
}
