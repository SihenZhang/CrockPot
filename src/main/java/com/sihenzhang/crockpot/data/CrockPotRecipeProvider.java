package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.data.recipes.CrockPotCookingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ExplosionCraftingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ParrotFeedingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.PiglinBarteringRecipeBuilder;
import com.sihenzhang.crockpot.item.CrockPotItems;
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
        ShapedRecipeBuilder.shaped(CrockPotItems.BASIC_CROCK_POT.get())
                .define('B', ItemTags.STONE_BRICKS)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Items.CHARCOAL)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("SCS")
                .unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
                .unlockedBy(getHasName(Items.CHARCOAL), has(Items.CHARCOAL))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotItems.BASIC_CROCK_POT.get()));
        ShapedRecipeBuilder.shaped(CrockPotItems.ADVANCED_CROCK_POT.get())
                .define('B', Items.NETHER_BRICK)
                .define('C', CrockPotItems.BASIC_CROCK_POT.get())
                .define('R', Tags.Items.RODS_BLAZE)
                .define('P', Items.BLAZE_POWDER)
                .pattern("BBB")
                .pattern("BCB")
                .pattern("RPR")
                .unlockedBy(getHasName(Items.NETHER_BRICK), has(Items.NETHER_BRICK))
                .unlockedBy(getHasName(CrockPotItems.BASIC_CROCK_POT.get()), has(CrockPotItems.BASIC_CROCK_POT.get()))
                .unlockedBy("has_blaze_rods", has(Tags.Items.RODS_BLAZE))
                .unlockedBy(getHasName(Items.BLAZE_POWDER), has(Items.BLAZE_POWDER))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotItems.ADVANCED_CROCK_POT.get()));
        ShapedRecipeBuilder.shaped(CrockPotItems.ULTIMATE_CROCK_POT.get())
                .define('B', Items.PRISMARINE_BRICKS)
                .define('C', CrockPotItems.ADVANCED_CROCK_POT.get())
                .define('D', Items.DARK_PRISMARINE)
                .define('H', Items.HEART_OF_THE_SEA)
                .pattern("BBB")
                .pattern("BCB")
                .pattern("DHD")
                .unlockedBy(getHasName(Items.PRISMARINE_BRICKS), has(Items.PRISMARINE_BRICKS))
                .unlockedBy(getHasName(CrockPotItems.ADVANCED_CROCK_POT.get()), has(CrockPotItems.ADVANCED_CROCK_POT.get()))
                .unlockedBy(getHasName(Items.DARK_PRISMARINE), has(Items.DARK_PRISMARINE))
                .unlockedBy(getHasName(Items.HEART_OF_THE_SEA), has(Items.HEART_OF_THE_SEA))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotItems.ULTIMATE_CROCK_POT.get()));

        ShapedRecipeBuilder.shaped(CrockPotItems.BIRDCAGE.get())
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', Tags.Items.INGOTS_GOLD)
                .pattern("NNN")
                .pattern("N N")
                .pattern("III")
                .unlockedBy("has_gold_ingots", has(Tags.Items.INGOTS_GOLD))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotItems.BIRDCAGE.get()));

        ExplosionCraftingRecipeBuilder.explosionCrafting(CrockPotItems.BLACKSTONE_DUST.get(), Ingredient.of(Items.BLACKSTONE)).lossRate(0.75F).onlyBlock()
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("explosion_crafting", CrockPotItems.BLACKSTONE_DUST.get()));

        ShapelessRecipeBuilder.shapeless(CrockPotItems.COLLECTED_DUST.get())
                .requires(Tags.Items.GEMS_QUARTZ)
                .requires(CrockPotItems.BLACKSTONE_DUST.get(), 2)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .unlockedBy(getHasName(CrockPotItems.BLACKSTONE_DUST.get()), has(Tags.Items.GEMS_QUARTZ))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotItems.COLLECTED_DUST.get()));

        var cookingRecipes = Map.of(
                CrockPotItems.CORN.get(), CrockPotItems.POPCORN.get(),
                Items.EGG, CrockPotItems.COOKED_EGG.get(),
                CrockPotItems.EGGPLANT.get(), CrockPotItems.COOKED_EGGPLANT.get(),
                CrockPotItems.FROG_LEGS.get(), CrockPotItems.COOKED_FROG_LEGS.get(),
                CrockPotItems.HOGLIN_NOSE.get(), CrockPotItems.COOKED_HOGLIN_NOSE.get()
        );
        cookingRecipes.forEach((input, output) -> {
            smeltingRecipe(pFinishedRecipeConsumer, input, output, 0.35F, 200);
            smokingRecipe(pFinishedRecipeConsumer, input, output, 0.35F, 100);
            campfireCookingRecipe(pFinishedRecipeConsumer, input, output, 0.35F, 600);
        });
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CrockPotItemTags.PARROT_EGGS), CrockPotItems.COOKED_EGG.get(), 0.35F, 200)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("smelting", getItemName(CrockPotItems.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(CrockPotItemTags.PARROT_EGGS), CrockPotItems.COOKED_EGG.get(), 0.35F, 100)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("smoking", getItemName(CrockPotItems.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(CrockPotItemTags.PARROT_EGGS), CrockPotItems.COOKED_EGG.get(), 0.35F, 600)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("campfire_cooking", getItemName(CrockPotItems.COOKED_EGG.get()) + "_by_parrot_eggs"));

        var seedsRecipes = Map.of(
                Items.WHEAT, Items.WHEAT_SEEDS,
                Items.BEETROOT, Items.BEETROOT_SEEDS,
                Items.MELON_SLICE, Items.MELON_SEEDS,
                CrockPotItems.ASPARAGUS.get(), CrockPotItems.ASPARAGUS_SEEDS.get(),
                CrockPotItems.CORN.get(), CrockPotItems.CORN_SEEDS.get(),
                CrockPotItems.EGGPLANT.get(), CrockPotItems.EGGPLANT_SEEDS.get(),
                CrockPotItems.GARLIC.get(), CrockPotItems.GARLIC_SEEDS.get(),
                CrockPotItems.ONION.get(), CrockPotItems.ONION_SEEDS.get(),
                CrockPotItems.PEPPER.get(), CrockPotItems.PEPPER_SEEDS.get(),
                CrockPotItems.TOMATO.get(), CrockPotItems.TOMATO_SEEDS.get()
        );
        seedsRecipes.forEach((input, output) -> ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 1, 2).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", output)));
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(Items.PUMPKIN), Items.PUMPKIN_SEEDS, 4, 6).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", Items.PUMPKIN_SEEDS));

        PiglinBarteringRecipeBuilder.piglinBartering(Ingredient.of(CrockPotItems.NETHEROSIA.get()))
                .addResult(CrockPotItems.HOGLIN_NOSE.get(), 1, 2, 20)
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
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("piglin_bartering", CrockPotItems.NETHEROSIA.get()));

        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.BREAKFAST_SKILLET.get(), 1, 20 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.EGG)
                .requirementWithAnyCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.DAIRY)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.BREAKFAST_SKILLET.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.GLOW_BERRY_MOUSSE.get(), 30, 40 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.GLOW_BERRIES), 2)
                .requirementWithAnyCategory(FoodCategory.FRUIT)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.GLOW_BERRY_MOUSSE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.PLAIN_OMELETTE.get(), 1, 20 * 20, 0)
                .requirementCategoryMin(FoodCategory.EGG, 3.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.PLAIN_OMELETTE.get()));
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
