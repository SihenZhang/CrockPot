package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.pathfinding.FlyingPathNavigator;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class AnimalsFollowPowcakeEvent {
    private static final Logger LOGGER = LogManager.getLogger();

    @SubscribeEvent
    public static void onAnimalAppear(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof AnimalEntity) {
            AnimalEntity animalEntity = (AnimalEntity) event.getEntity();
            // See GH-09
            if (animalEntity instanceof SkeletonHorseEntity) {
                return;
            }
            // Otherwise, it will throw IllegalArgumentException
            if ((animalEntity.getNavigator() instanceof GroundPathNavigator) || (animalEntity.getNavigator() instanceof FlyingPathNavigator)) {
                // Avoid adding duplicate TemptGoal
                if (animalEntity.goalSelector.goals.stream().map(PrioritizedGoal::getGoal).filter(goal -> goal instanceof TemptGoal).map(TemptGoal.class::cast).noneMatch(goal -> goal.isTempting(CrockPotRegistry.powCake.getDefaultInstance()))) {
                    try {
                        animalEntity.goalSelector.addGoal(3, new TemptGoal(animalEntity, 0.8, false, Ingredient.fromItems(CrockPotRegistry.powCake)));
                    } catch (Exception ignored) {
                        LOGGER.debug("Error when adding TemptGoal to " + animalEntity.getClass().getName() + " " + animalEntity);
                    }
                }
            }
        }
    }
}
