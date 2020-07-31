package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IItemProvider;

@MethodsReturnNonnullByDefault
public class OnionBlock extends CrockPotCropsBlock {
    public OnionBlock() {
        super();
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return CrockPotRegistry.onionSeeds.get();
    }
}
