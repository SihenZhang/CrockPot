package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.data.recipes.ExplosionCraftingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ParrotFeedingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.PiglinBarteringRecipeBuilder;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;

import java.util.Map;
import java.util.function.Consumer;

public class CrockPotRecipeProvider extends RecipeProvider {
    public CrockPotRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shaped(CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get())
                .define('B', ItemTags.STONE_BRICKS)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Items.CHARCOAL)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("SCS")
                .unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
                .unlockedBy(getHasName(Items.CHARCOAL), has(Items.CHARCOAL))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get()));
        ShapedRecipeBuilder.shaped(CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK_ITEM.get())
                .define('B', Items.NETHER_BRICK)
                .define('C', CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get())
                .define('R', Tags.Items.RODS_BLAZE)
                .define('P', Items.BLAZE_POWDER)
                .pattern("BBB")
                .pattern("BCB")
                .pattern("RPR")
                .unlockedBy(getHasName(Items.NETHER_BRICK), has(Items.NETHER_BRICK))
                .unlockedBy(getHasName(CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get()), has(CrockPotRegistry.BASIC_CROCK_POT_BLOCK_ITEM.get()))
                .unlockedBy("has_blaze_rods", has(Tags.Items.RODS_BLAZE))
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK_ITEM.get()));
        ShapedRecipeBuilder.shaped(CrockPotRegistry.ULTIMATE_CROCK_POT_BLOCK_ITEM.get())
                .define('B', Items.PRISMARINE_BRICKS)
                .define('C', CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK_ITEM.get())
                .define('D', Items.DARK_PRISMARINE)
                .define('H', Items.HEART_OF_THE_SEA)
                .pattern("BBB")
                .pattern("BCB")
                .pattern("DHD")
                .unlockedBy(getHasName(Items.PRISMARINE_BRICKS), has(Items.PRISMARINE_BRICKS))
                .unlockedBy(getHasName(CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK_ITEM.get()), has(CrockPotRegistry.ADVANCED_CROCK_POT_BLOCK_ITEM.get()))
                .unlockedBy(getHasName(Items.DARK_PRISMARINE), has(Items.DARK_PRISMARINE))
                .unlockedBy(getHasName(Items.HEART_OF_THE_SEA), has(Items.HEART_OF_THE_SEA))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotRegistry.ULTIMATE_CROCK_POT_BLOCK_ITEM.get()));

        ShapedRecipeBuilder.shaped(CrockPotRegistry.BIRDCAGE_BLOCK_ITEM.get())
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', Tags.Items.INGOTS_GOLD)
                .pattern("NNN")
                .pattern("N N")
                .pattern("III")
                .unlockedBy("has_gold_ingots", has(Tags.Items.INGOTS_GOLD))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotRegistry.BIRDCAGE_BLOCK_ITEM.get()));

        ExplosionCraftingRecipeBuilder.explosionCrafting(CrockPotRegistry.BLACKSTONE_DUST.get(), Ingredient.of(Items.BLACKSTONE)).lossRate(0.75F).onlyBlock()
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("explosion_crafting", CrockPotRegistry.BLACKSTONE_DUST.get()));

        ShapelessRecipeBuilder.shapeless(CrockPotRegistry.COLLECTED_DUST.get())
                .requires(Tags.Items.GEMS_QUARTZ)
                .requires(CrockPotRegistry.BLACKSTONE_DUST.get(), 2)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .unlockedBy(getHasName(CrockPotRegistry.BLACKSTONE_DUST.get()), has(Tags.Items.GEMS_QUARTZ))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotRegistry.COLLECTED_DUST.get()));

        var cookingRecipes = Map.of(
                CrockPotRegistry.CORN.get(), CrockPotRegistry.POPCORN.get(),
                Items.EGG, CrockPotRegistry.COOKED_EGG.get(),
                CrockPotRegistry.EGGPLANT.get(), CrockPotRegistry.COOKED_EGGPLANT.get(),
                CrockPotRegistry.FROG_LEGS.get(), CrockPotRegistry.COOKED_FROG_LEGS.get(),
                CrockPotRegistry.HOGLIN_NOSE.get(), CrockPotRegistry.COOKED_HOGLIN_NOSE.get()
        );
        cookingRecipes.forEach((input, output) -> {
            smeltingRecipe(pFinishedRecipeConsumer, input, output, 0.35F, 200);
            smokingRecipe(pFinishedRecipeConsumer, input, output, 0.35F, 100);
            campfireCookingRecipe(pFinishedRecipeConsumer, input, output, 0.35F, 600);
        });
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CrockPotItemTags.PARROT_EGGS), CrockPotRegistry.COOKED_EGG.get(), 0.35F, 200)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("smelting", getItemName(CrockPotRegistry.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(CrockPotItemTags.PARROT_EGGS), CrockPotRegistry.COOKED_EGG.get(), 0.35F, 100)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("smoking", getItemName(CrockPotRegistry.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(CrockPotItemTags.PARROT_EGGS), CrockPotRegistry.COOKED_EGG.get(), 0.35F, 600)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("campfire_cooking", getItemName(CrockPotRegistry.COOKED_EGG.get()) + "_by_parrot_eggs"));

        var seedsRecipes = Map.of(
                Items.WHEAT, Items.WHEAT_SEEDS,
                Items.BEETROOT, Items.BEETROOT_SEEDS,
                Items.MELON_SLICE, Items.MELON_SEEDS,
                CrockPotRegistry.ASPARAGUS.get(), CrockPotRegistry.ASPARAGUS_SEEDS.get(),
                CrockPotRegistry.CORN.get(), CrockPotRegistry.CORN_SEEDS.get(),
                CrockPotRegistry.EGGPLANT.get(), CrockPotRegistry.EGGPLANT_SEEDS.get(),
                CrockPotRegistry.GARLIC.get(), CrockPotRegistry.GARLIC_SEEDS.get(),
                CrockPotRegistry.ONION.get(), CrockPotRegistry.ONION_SEEDS.get(),
                CrockPotRegistry.PEPPER.get(), CrockPotRegistry.PEPPER_SEEDS.get(),
                CrockPotRegistry.TOMATO.get(), CrockPotRegistry.TOMATO_SEEDS.get()
        );
        seedsRecipes.forEach((input, output) -> ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 1, 2).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", output)));
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(Items.PUMPKIN), Items.PUMPKIN_SEEDS, 4, 6).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", Items.PUMPKIN_SEEDS));

        PiglinBarteringRecipeBuilder.piglinBartering(Ingredient.of(CrockPotRegistry.NETHEROSIA.get()))
                .addResult(CrockPotRegistry.HOGLIN_NOSE.get(), 1, 2, 20)
                .addResult(Items.PORKCHOP, 2, 4, 20)
                .addResult(Items.LEATHER, 2, 4, 20)
                .addResult(Items.BONE, 2, 4, 20)
                .addResult(Items.STRING, 2, 4, 20)
                .addResult(Items.MAGMA_CREAM, 2, 3, 15)
                .addResult(Items.BLAZE_POWDER, 2, 3, 15)
                .addResult(Items.ENDER_PEARL, 2, 3, 15)
                .addResult(Items.GHAST_TEAR, 15)
                .addResult(Items.NETHERITE_SCRAP, 1, 5, 15)
                .addResult(Items.BROWN_MUSHROOM, 2, 4, 6)
                .addResult(Items.RED_MUSHROOM, 2, 4, 6)
                .addResult(Items.CRIMSON_FUNGUS, 2, 4, 6)
                .addResult(Items.WARPED_FUNGUS, 2, 4, 6)
                .addResult(Items.WITHER_SKELETON_SKULL, 1)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("piglin_bartering", "netherosia"));
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
