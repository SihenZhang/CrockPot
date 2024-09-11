package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.ModEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.goat.Goat;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.EntityStruckByLightningEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class GoatConversionEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGoatStruckByLightning(EntityStruckByLightningEvent event) {
        var lightning = event.getLightning();
        if (lightning.level() instanceof ServerLevel level && event.getEntity() instanceof Goat goat && !event.isCanceled()) {
            if (EventHooks.canLivingConvert(goat, ModEntities.VOLT_GOAT.get(), (timer) -> {
            })) {
                var voltGoat = ModEntities.VOLT_GOAT.get().create(level);
                if (voltGoat != null) {
                    voltGoat.moveTo(goat.getX(), goat.getY(), goat.getZ(), goat.getYRot(), goat.getXRot());
                    voltGoat.setLastLightningBolt(lightning.getUUID());
                    voltGoat.setNoAi(goat.isNoAi());
                    voltGoat.setBaby(goat.isBaby());
                    if (goat.hasCustomName()) {
                        voltGoat.setCustomName(goat.getCustomName());
                        voltGoat.setCustomNameVisible(goat.isCustomNameVisible());
                    }
                    voltGoat.setPersistenceRequired();
                    EventHooks.onLivingConvert(goat, voltGoat);
                    level.addFreshEntity(voltGoat);
                    goat.discard();
                }
            }
        }
    }
}
