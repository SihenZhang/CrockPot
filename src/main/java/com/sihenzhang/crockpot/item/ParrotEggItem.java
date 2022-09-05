package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.core.NonNullList;
import net.minecraft.util.Mth;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ParrotEggItem extends Item implements ItemColor {
    private static final int[][] COLORS = {
            {0xb20200, 0x005eb7},
            {0x0e26cb, 0x04104e},
            {0x9bd901, 0x426000},
            {0x188bb7, 0xfed305},
            {0xababab, 0x616161}
    };

    public ParrotEggItem() {
        super(new Properties().tab(CrockPot.ITEM_GROUP));
    }

    @Override
    public int getColor(ItemStack pStack, int pTintIndex) {
        return COLORS[this.getVariant(pStack)][pTintIndex];
    }

    @Override
    public void fillItemCategory(CreativeModeTab pCategory, NonNullList<ItemStack> pItems) {
        if (this.allowdedIn(pCategory)) {
            for (var i = 0; i < 5; i++) {
                pItems.add(this.getWithVariant(i));
            }
        }
    }

    public int getVariant(ItemStack pStack) {
        return Mth.clamp(pStack.getOrCreateTag().getInt("Variant"), 0, 4);
    }

    public ItemStack getWithVariant(int pVariant) {
        var parrotEgg = this.getDefaultInstance();
        parrotEgg.getOrCreateTag().putInt("Variant", Mth.clamp(pVariant, 0, 4));
        return parrotEgg;
    }
}
