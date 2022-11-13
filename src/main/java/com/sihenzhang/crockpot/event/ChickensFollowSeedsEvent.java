package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.animal.Chicken;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrockPot.MOD_ID)
public class ChickensFollowSeedsEvent {
    @SubscribeEvent
    public static void onChickenAppear(EntityJoinWorldEvent event) {
        if (!event.getWorld().isClientSide && event.getEntity() instanceof Chicken chicken) {
            CrockPotItems.SEEDS.get().forEach(seed -> {
                // Avoid adding duplicate TemptGoal
                if (chicken.goalSelector.getAvailableGoals().stream()
                        .map(WrappedGoal::getGoal)
                        .filter(TemptGoal.class::isInstance)
                        .map(TemptGoal.class::cast)
                        .noneMatch(goal -> goal.items.test(seed.getDefaultInstance()))) {
                    chicken.goalSelector.addGoal(3, new TemptGoal(chicken, 1.0, Ingredient.of(seed), false));
                }
            });
        }
    }
}
