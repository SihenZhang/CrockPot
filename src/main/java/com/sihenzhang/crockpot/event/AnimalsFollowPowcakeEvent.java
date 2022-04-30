package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.SkeletonHorse;
import net.minecraft.world.item.crafting.Ingredient;
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
        if (!event.getWorld().isClientSide && event.getEntity() instanceof Animal animal) {
            // See GH-09
            if (animal instanceof SkeletonHorse) {
                return;
            }
            // Otherwise, it will throw IllegalArgumentException
            if ((animal.getNavigation() instanceof GroundPathNavigation) || (animal.getNavigation() instanceof FlyingPathNavigation)) {
                // Avoid adding duplicate TemptGoal
                if (animal.goalSelector.getAvailableGoals().stream()
                        .map(WrappedGoal::getGoal)
                        .filter(goal -> goal instanceof TemptGoal)
                        .map(TemptGoal.class::cast)
                        .noneMatch(goal -> goal.items.test(CrockPotRegistry.powCake.get().getDefaultInstance()))) {
                    try {
                        animal.goalSelector.addGoal(3, new TemptGoal(animal, 0.8, Ingredient.of(CrockPotRegistry.powCake.get()), false));
                    } catch (Exception ignored) {
                        LOGGER.debug("Error when adding TemptGoal to " + animal.getClass().getName() + " " + animal);
                    }
                }
            }
        }
    }
}
