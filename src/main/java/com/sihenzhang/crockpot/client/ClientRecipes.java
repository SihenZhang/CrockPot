package com.sihenzhang.crockpot.client;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.world.item.crafting.*;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;

import java.util.List;

@EventBusSubscriber(value = Dist.CLIENT, modid = CrockPot.MOD_ID)
public final class ClientRecipes {
    private static RecipeMap recipes = RecipeMap.EMPTY;

    private ClientRecipes() {
    }

    public static RecipeMap getRecipeMap() {
        return recipes;
    }

    public static <I extends RecipeInput, T extends Recipe<I>> List<RecipeHolder<T>> getRecipes(RecipeType<T> type) {
        return List.copyOf(recipes.byType(type));
    }

    @SubscribeEvent
    public static void onRecipesReceived(final RecipesReceivedEvent event) {
        recipes = event.getRecipeMap();
    }

    @SubscribeEvent
    public static void onLoggingOut(final ClientPlayerNetworkEvent.LoggingOut event) {
        recipes = RecipeMap.EMPTY;
    }
}
