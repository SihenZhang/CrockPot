package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IItemProvider;

@MethodsReturnNonnullByDefault
public class TomatoBlock extends CrockPotCropsBlock {
    @Override
    protected IItemProvider getSeedsItem() {
        return CrockPotRegistry.tomatoSeeds.get();
    }
}
