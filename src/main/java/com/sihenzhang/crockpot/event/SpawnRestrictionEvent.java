package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.ModEntities;
import com.sihenzhang.crockpot.entity.VoltGoat;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class SpawnRestrictionEvent {
    @SubscribeEvent
    public static void onSpawnPlacementRegister(final RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.VOLT_GOAT.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                VoltGoat::checkVoltGoatSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }
}
