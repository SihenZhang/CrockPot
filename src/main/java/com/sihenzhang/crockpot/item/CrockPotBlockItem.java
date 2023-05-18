package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

public class CrockPotBlockItem extends BlockItem {
    public CrockPotBlockItem(Block pBlock) {
        super(pBlock, new Properties());
    }

    @Override
    public Component getName(ItemStack pStack) {
        return this.getBlock().getName();
    }
}
