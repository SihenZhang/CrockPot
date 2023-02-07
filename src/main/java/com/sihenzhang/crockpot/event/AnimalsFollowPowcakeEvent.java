package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Animal;
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
            // Avoid adding duplicate TemptGoal
            if (animal.goalSelector.getAvailableGoals().stream()
                    .map(WrappedGoal::getGoal)
                    .filter(TemptGoal.class::isInstance)
                    .map(TemptGoal.class::cast)
                    .noneMatch(goal -> goal.items.test(CrockPotItems.POW_CAKE.get().getDefaultInstance()))) {
                try {
                    animal.goalSelector.addGoal(3, new TemptGoal(animal, 0.8, Ingredient.of(CrockPotItems.POW_CAKE.get()), false));
                } catch (Exception ignored) {
                    LOGGER.error("Error when adding TemptGoal to {} {}", animal.getClass().getName(), animal);
                }
            }
        }
    }
}
