package com.sihenzhang.crockpot.data;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.data.recipes.ParrotFeedingRecipeBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
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

        var seedsRecipes = ImmutableList.of(
                Pair.of(CrockPotRegistry.ASPARAGUS.get(), CrockPotRegistry.ASPARAGUS_SEEDS.get()),
                Pair.of(CrockPotRegistry.CORN.get(), CrockPotRegistry.CORN_SEEDS.get()),
                Pair.of(CrockPotRegistry.EGGPLANT.get(), CrockPotRegistry.EGGPLANT_SEEDS.get()),
                Pair.of(CrockPotRegistry.GARLIC.get(), CrockPotRegistry.GARLIC_SEEDS.get()),
                Pair.of(CrockPotRegistry.ONION.get(), CrockPotRegistry.ONION_SEEDS.get()),
                Pair.of(CrockPotRegistry.PEPPER.get(), CrockPotRegistry.PEPPER_SEEDS.get()),
                Pair.of(CrockPotRegistry.TOMATO.get(), CrockPotRegistry.TOMATO_SEEDS.get())
        );
        seedsRecipes.forEach(p -> {
            ShapelessRecipeBuilder.shapeless(p.getSecond()).requires(p.getFirst()).unlockedBy(getHasName(p.getFirst()), has(p.getFirst())).save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", p.getSecond()));
            parrotFeedingRecipe(pFinishedRecipeConsumer, p.getFirst(), p.getSecond(), 1, 2);
        });
        parrotFeedingRecipe(pFinishedRecipeConsumer, Items.WHEAT, Items.WHEAT_SEEDS, 1, 2);
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

    protected static void parrotFeedingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, int pMinResultCount, int pMaxResultCount) {
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(pIngredient), pResult, pMinResultCount, pMaxResultCount).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", pResult));
    }

    protected static void parrotFeedingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult, int pResultCount) {
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(pIngredient), pResult, pResultCount).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", pResult));
    }

    protected static void parrotFeedingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, ItemLike pResult) {
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(pIngredient), pResult).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", pResult));
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
