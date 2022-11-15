package com.sihenzhang.crockpot.item;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.world.item.Item;

public class CrockPotBaseItem extends Item {
    public CrockPotBaseItem(Properties pProperties) {
        super(pProperties.tab(CrockPot.ITEM_GROUP));
    }

    public CrockPotBaseItem() {
        this(new Properties());
    }
}
