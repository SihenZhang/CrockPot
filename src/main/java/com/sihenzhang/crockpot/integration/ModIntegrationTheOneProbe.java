package com.sihenzhang.crockpot.integration;

import com.sihenzhang.crockpot.integration.theoneprobe.CrockPotProvider;
import mcjty.theoneprobe.api.ITheOneProbe;

import java.util.function.Function;

public class ModIntegrationTheOneProbe implements Function<ITheOneProbe, Void> {
    @Override
    public Void apply(ITheOneProbe theOneProbe) {
        theOneProbe.registerProvider(new CrockPotProvider());
        return null;
    }
}
