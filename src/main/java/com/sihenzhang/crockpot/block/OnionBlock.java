package com.sihenzhang.crockpot.block;

import com.sihenzhang.crockpot.registry.CrockPotRegistry;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.IItemProvider;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class OnionBlock extends CrockPotCropsBlock {
    @Override
    protected IItemProvider getSeedsItem() {
        return CrockPotRegistry.onion.get();
    }
}
