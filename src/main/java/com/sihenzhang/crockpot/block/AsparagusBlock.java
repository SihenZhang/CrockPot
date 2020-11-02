package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.CrockPotRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IItemProvider;

@MethodsReturnNonnullByDefault
public class AsparagusBlock extends CrockPotCropsBlock {
    @Override
    protected IItemProvider getSeedsItem() {
        return CrockPotRegistry.asparagus.get();
    }
}
