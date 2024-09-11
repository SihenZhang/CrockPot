package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.food.PowCakeBlock;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class AnimalsFollowPowcakeEvent {
    @SubscribeEvent
    public static void onAnimalAppear(final EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide && event.getEntity() instanceof Animal animal) {
            var hasTemptGoal = false;
            var hasEatGoal = false;
            for (var wrappedGoal : animal.goalSelector.getAvailableGoals()) {
                var goal = wrappedGoal.getGoal();
                hasTemptGoal = hasTemptGoal || isTemptGoal(goal);
                hasEatGoal = hasEatGoal || isEatGoal(goal);
            }
            // Avoid adding duplicate TemptGoal
            if (!hasTemptGoal) {
                try {
                    animal.goalSelector.addGoal(3, new TemptGoal(animal, 0.8, Ingredient.of(ModItems.POW_CAKE.get()), false));
                } catch (Exception ignored) {
                    CrockPot.LOGGER.error("Error when adding TemptGoal to {} {}", animal.getClass().getName(), animal);
                }
            }
            // Avoid adding duplicate AnimalEatPowCakeGoal
            if (!hasEatGoal) {
                try {
                    animal.goalSelector.addGoal(4, new PowCakeBlock.AnimalEatPowCakeGoal(animal, 0.8, 3));
                } catch (Exception ignored) {
                    CrockPot.LOGGER.error("Error when adding AnimalEatPowCakeGoal to {} {}", animal.getClass().getName(), animal);
                }
            }
        }
    }

    private static boolean isTemptGoal(Goal goal) {
        return goal instanceof TemptGoal temptGoal && temptGoal.items.test(ModItems.POW_CAKE.get().getDefaultInstance());
    }

    private static boolean isEatGoal(Goal goal) {
        return goal instanceof PowCakeBlock.AnimalEatPowCakeGoal;
    }
}
