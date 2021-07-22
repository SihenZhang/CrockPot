package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CrockPotBlockItem extends BlockItem {
    public CrockPotBlockItem(Block blockIn) {
        super(blockIn, new Properties().tab(CrockPot.ITEM_GROUP));
    }

    @Override
    public ITextComponent getName(ItemStack stack) {
        return this.getBlock().getName();
    }
}
