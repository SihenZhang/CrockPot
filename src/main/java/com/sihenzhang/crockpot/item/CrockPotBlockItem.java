package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CrockPotBlockItem extends BlockItem {
    public CrockPotBlockItem(Block block) {
        super(block, new Properties().tab(CrockPot.ITEM_GROUP));
    }

    @Override
    public Component getName(ItemStack stack) {
        return this.getBlock().getName();
    }
}
