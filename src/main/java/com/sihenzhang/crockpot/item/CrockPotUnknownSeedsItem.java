package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPotConfig;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class CrockPotUnknownSeedsItem extends CrockPotSeedsItem {
    public CrockPotUnknownSeedsItem() {
        super(CrockPotRegistry.unknownCropsBlock);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (CrockPotConfig.ENABLE_UNKNOWN_SEEDS.get()) {
            super.fillItemCategory(group, items);
        }
    }
}
