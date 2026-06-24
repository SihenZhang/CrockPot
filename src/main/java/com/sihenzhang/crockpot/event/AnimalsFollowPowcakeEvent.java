package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.block.food.PowCakeBlock;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public class AnimalsFollowPowcakeEvent {
    @SubscribeEvent
    public static void onAnimalAppear(EntityJoinLevelEvent event) {
        if (!event.getLevel().isClientSide() && event.getEntity() instanceof Animal animal) {
            var hasTemptGoal = false;
            var hasEatGoal = false;
            for (var wrappedGoal : animal.goalSelector.getAvailableGoals()) {
                if (wrappedGoal != null) {
                    var goal = wrappedGoal.getGoal();
                    hasTemptGoal = hasTemptGoal || isTemptGoal(goal);
                    hasEatGoal = hasEatGoal || isEatGoal(goal);
                }
            }
            // Avoid adding duplicate TemptGoal
            if (!hasTemptGoal && animal.getAttributes().hasAttribute(Attributes.TEMPT_RANGE)) {
                try {
                    animal.goalSelector.addGoal(3, new PowCakeTemptGoal(animal));
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
        return goal instanceof PowCakeTemptGoal;
    }

    private static boolean isEatGoal(Goal goal) {
        return goal instanceof PowCakeBlock.AnimalEatPowCakeGoal;
    }

    private static class PowCakeTemptGoal extends TemptGoal {
        PowCakeTemptGoal(Animal animal) {
            super(animal, 0.8, PowCakeTemptGoal::isPowCake, false);
        }

        private static boolean isPowCake(ItemStack stack) {
            return stack.is(ModItems.POW_CAKE.get());
        }
    }
}
