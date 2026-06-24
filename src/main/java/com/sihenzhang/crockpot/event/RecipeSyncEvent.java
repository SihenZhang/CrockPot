package com.sihenzhang.crockpot.event;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;

@EventBusSubscriber(modid = CrockPot.MOD_ID)
public final class RecipeSyncEvent {
    private RecipeSyncEvent() {
    }

    @SubscribeEvent
    public static void onDatapackSync(final OnDatapackSyncEvent event) {
        event.sendRecipes(
                ModRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get(),
                ModRecipes.DRYING_RECIPE_TYPE.get(),
                ModRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get(),
                ModRecipes.FOOD_VALUES_RECIPE_TYPE.get(),
                ModRecipes.PARROT_FEEDING_RECIPE_TYPE.get(),
                ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get()
        );
    }
}
