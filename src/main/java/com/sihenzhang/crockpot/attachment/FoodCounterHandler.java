package com.sihenzhang.crockpot.attachment;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.advancement.ModCriterionTriggers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class FoodCounterHandler {
    @SubscribeEvent
    public static void onFoodEaten(final LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player) || !event.getItem().has(DataComponents.FOOD)) {
            return;
        }
        var foodCounter = player.getData(ModAttachmentTypes.FOOD_COUNTER);
        var food = event.getItem().getItem();
        foodCounter.addFood(food);
        ModCriterionTriggers.EAT_FOOD_TRIGGER.get().trigger(player, event.getItem(), foodCounter.getCount(food));
        player.setData(ModAttachmentTypes.FOOD_COUNTER, foodCounter);
    }
}
