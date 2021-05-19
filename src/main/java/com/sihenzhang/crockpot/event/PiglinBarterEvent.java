package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class PiglinBarterEvent {
    @SubscribeEvent
    public static void onAnimalAppear(EntityJoinWorldEvent event) {
//        if (event.getEntity() instanceof PiglinEntity) {
//            PiglinEntity piglinEntity = (PiglinEntity) event.getEntity();
//            piglinEntity.goalSelector.addGoal(1, new GenericBarterGoal<>(
//                    piglinEntity, input -> input.getItem() == CrockPotRegistry.collectedDust,
//                    input -> CrockPotRegistry.pepper.getDefaultInstance()));
//        }
    }
}
