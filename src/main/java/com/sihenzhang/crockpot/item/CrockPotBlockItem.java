package com.sihenzhang.crockpot.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CrockPotBlockItem extends BlockItem {
    public CrockPotBlockItem(Block block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public Component getName(ItemStack pStack) {
        return this.getBlock().getName();
    }
}
