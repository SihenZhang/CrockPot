package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class CrockPotUnknownSeedsItem extends CrockPotSeedsItem {
    public CrockPotUnknownSeedsItem() {
        super(CrockPotRegistry.unknownCropsBlock.get());
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (CrockPotConfig.ENABLE_UNKNOWN_SEEDS.get()) {
            super.fillItemGroup(group, items);
        }
    }
}
