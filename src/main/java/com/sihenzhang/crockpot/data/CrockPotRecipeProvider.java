package com.sihenzhang.crockpot.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class CrockPotRecipeProvider extends RecipeProvider {
    public CrockPotRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        var cookingRecipes = ImmutableList.of(
                Pair.of(CrockPotRegistry.CORN.get(), CrockPotRegistry.POPCORN.get()),
                Pair.of(Items.EGG, CrockPotRegistry.COOKED_EGG.get()),
                Pair.of(CrockPotRegistry.EGGPLANT.get(), CrockPotRegistry.COOKED_EGGPLANT.get()),
                Pair.of(CrockPotRegistry.FROG_LEGS.get(), CrockPotRegistry.COOKED_FROG_LEGS.get()),
                Pair.of(CrockPotRegistry.HOGLIN_NOSE.get(), CrockPotRegistry.COOKED_HOGLIN_NOSE.get())
        );
        cookingRecipes.forEach(p -> {
            smeltingRecipe(pFinishedRecipeConsumer, p.getFirst(), p.getSecond(), 0.35F, 200);
            smokingRecipe(pFinishedRecipeConsumer, p.getFirst(), p.getSecond(), 0.35F, 100);
            campfireCookingRecipe(pFinishedRecipeConsumer, p.getFirst(), p.getSecond(), 0.35F, 600);
        });
    }

    protected static void smeltingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(pIngredient), pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(pFinishedRecipeConsumer, getSimpleRecipeName("smelting", pResult));
    }

    protected static void smokingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(pIngredient), pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(pFinishedRecipeConsumer, getSimpleRecipeName("smoking", pResult));
    }

    protected static void campfireCookingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(pIngredient), pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(pFinishedRecipeConsumer, getSimpleRecipeName("campfire_cooking", pResult));
    }

    protected static String getSimpleRecipeName(ItemLike pItemLike) {
        return getSimpleRecipeName(getItemName(pItemLike));
    }

    protected static String getSimpleRecipeName(String name) {
        return CrockPot.MOD_ID + ":" + name;
    }

    protected static String getSimpleRecipeName(String pRecipeType, ItemLike pItemLike) {
        return getSimpleRecipeName(pRecipeType, getItemName(pItemLike));
    }

    protected static String getSimpleRecipeName(String pRecipeType, String name) {
        return CrockPot.MOD_ID + ":" + pRecipeType + "/" + name;
    }

    @Override
    public String getName() {
        return "CrockPot Recipes";
    }
}
