package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.entity.CrockPotEntities;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.animal.goat.Goat;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.entity.EntityStruckByLightningEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class GoatConversionEvent {
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onGoatStruckByLightning(EntityStruckByLightningEvent event) {
        var lightning = event.getLightning();
        if (lightning.level() instanceof ServerLevel level && event.getEntity() instanceof Goat goat && !event.isCanceled()) {
            if (ForgeEventFactory.canLivingConvert(goat, CrockPotEntities.VOLT_GOAT.get(), (timer) -> {
            })) {
                var voltGoat = CrockPotEntities.VOLT_GOAT.get().create(level);
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
                    ForgeEventFactory.onLivingConvert(goat, voltGoat);
                    level.addFreshEntity(voltGoat);
                    goat.discard();
                }
            }
        }
    }
}
