package com.sihenzhang.crockpot.util;

import com.sihenzhang.crockpot.client.ClientRecipes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

public final class RecipeUtil {
    private RecipeUtil() {}

    public static <I extends RecipeInput, T extends Recipe<I>> Optional<RecipeHolder<T>> getRecipeFor(
            RecipeType<T> type, I input, Level level
    ) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess().getRecipeFor(type, input, serverLevel);
        }
        return ClientRecipes.getRecipeMap().getRecipesFor(type, input, level).findFirst();
    }

    public static <I extends RecipeInput, T extends Recipe<I>> Stream<RecipeHolder<T>> getRecipesFor(
            RecipeType<T> type, I input, Level level
    ) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess().recipeMap().getRecipesFor(type, input, serverLevel);
        }
        return ClientRecipes.getRecipeMap().getRecipesFor(type, input, level);
    }

    public static <I extends RecipeInput, T extends Recipe<I>> Collection<RecipeHolder<T>> byType(
            RecipeType<T> type, Level level
    ) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess().recipeMap().byType(type);
        }
        return ClientRecipes.getRecipeMap().byType(type);
    }

    public static Optional<RecipeHolder<?>> byKey(ResourceKey<Recipe<?>> recipeId, Level level) {
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess().byKey(recipeId);
        }
        return Optional.ofNullable(ClientRecipes.getRecipeMap().byKey(recipeId));
    }
}
