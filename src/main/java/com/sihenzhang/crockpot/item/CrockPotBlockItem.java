package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class CrockPotBlockItem extends BlockItem {
    private final Random rand = new Random();
    private long lastSysTime;
    private Set<Integer> toPick;
    private final String[] suffixes = {"Pro", "Plus", "Max", "Ultra", "Premium"};

    public CrockPotBlockItem(Block blockIn) {
        super(blockIn, new Item.Properties().group(CrockPot.ITEM_GROUP));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        int potLevel = ((CrockPotBlock) this.getBlock()).getPotLevel();
        if (potLevel > 0) {
            long sysTime = System.currentTimeMillis();
            if (this.lastSysTime + 5000 < sysTime) {
                this.lastSysTime = sysTime;
                this.toPick = new HashSet<>(potLevel);
                while(this.toPick.size() < potLevel) {
                    this.toPick.add(this.rand.nextInt(this.suffixes.length));
                }
            }
            ITextComponent[] toPickSuffixes = this.toPick.stream().map(i -> new StringTextComponent(suffixes[i])).toArray(ITextComponent[]::new);
            return new TranslationTextComponent(this.getTranslationKey(stack), toPickSuffixes);
        } else {
            return super.getDisplayName(stack);
        }
    }
}
