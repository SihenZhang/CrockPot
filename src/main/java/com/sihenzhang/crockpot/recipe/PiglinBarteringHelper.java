package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.client.ClientRecipes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.Optional;

public final class PiglinBarteringHelper {
    private PiglinBarteringHelper() {
    }

    public static boolean isBarterInput(Level level, ItemStack stack) {
        return getRecipe(level, stack).isPresent();
    }

    public static Optional<RecipeHolder<PiglinBarteringRecipe>> getRecipe(Level level, ItemStack stack) {
        if (stack.isEmpty() || stack.is(Items.GOLD_NUGGET) || stack.is(ItemTags.PIGLIN_REPELLENTS) || stack.is(ItemTags.PIGLIN_FOOD)) {
            return Optional.empty();
        }
        var input = new SingleRecipeInput(stack);
        if (level instanceof ServerLevel serverLevel) {
            return serverLevel.recipeAccess().getRecipeFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), input, serverLevel);
        }
        return ClientRecipes.getRecipeMap().getRecipesFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get(), input, level).findFirst();
    }

    public static ItemStack assemble(Level level, ItemStack stack) {
        return assembleOptional(level, stack).orElse(ItemStack.EMPTY);
    }

    public static Optional<ItemStack> assembleOptional(Level level, ItemStack stack) {
        return getRecipe(level, stack)
                .map(recipe -> recipe.value().assemble(new SingleRecipeInput(stack)));
    }
}
