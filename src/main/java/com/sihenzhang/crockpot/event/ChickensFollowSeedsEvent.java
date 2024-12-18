package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.ChickenEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ChickensFollowSeedsEvent {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onChickenAppear(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide && event.getEntity() instanceof ChickenEntity) {
            ChickenEntity chickenEntity = (ChickenEntity) event.getEntity();
            CrockPotRegistry.seeds.forEach(seed -> {
                // Avoid adding duplicate TemptGoal
                if (chickenEntity.goalSelector.availableGoals.stream()
                        .map(PrioritizedGoal::getGoal)
                        .filter(goal -> goal instanceof TemptGoal)
                        .map(TemptGoal.class::cast)
                        .noneMatch(goal -> goal.shouldFollowItem(seed.getDefaultInstance()))) {
                    try {
                        chickenEntity.goalSelector.addGoal(3, new TemptGoal(chickenEntity, 1.0, false, Ingredient.of(seed)));
                    } catch (Exception ignored) {
                        LOGGER.debug("Error when adding TemptGoal to " + chickenEntity.getClass().getName() + " " + chickenEntity);
                    }
                }
            });
        }
    }
}
