package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.data.recipes.CrockPotCookingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ExplosionCraftingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ParrotFeedingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.PiglinBarteringRecipeBuilder;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMinExclusive;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationAnd;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationOr;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredient;
import com.sihenzhang.crockpot.tag.CrockPotItemTags;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.crafting.CompoundIngredient;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Consumer;

public class CrockPotRecipeProvider extends RecipeProvider {
    public CrockPotRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CrockPotItems.BASIC_CROCK_POT.get())
                .define('B', ItemTags.STONE_BRICKS)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', Items.CHARCOAL)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("SCS")
                .unlockedBy("has_stone_bricks", has(ItemTags.STONE_BRICKS))
                .unlockedBy(getHasName(Items.CHARCOAL), has(Items.CHARCOAL))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotItems.BASIC_CROCK_POT.get()));
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CrockPotItems.ADVANCED_CROCK_POT.get())
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
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CrockPotItems.ULTIMATE_CROCK_POT.get())
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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, CrockPotItems.BIRDCAGE.get())
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', Tags.Items.INGOTS_GOLD)
                .pattern("NNN")
                .pattern("N N")
                .pattern("III")
                .unlockedBy("has_gold_ingots", has(Tags.Items.INGOTS_GOLD))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", CrockPotItems.BIRDCAGE.get()));

        ExplosionCraftingRecipeBuilder.explosionCrafting(CrockPotItems.BLACKSTONE_DUST.get(), Ingredient.of(Items.BLACKSTONE)).lossRate(0.75F).onlyBlock()
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("explosion_crafting", CrockPotItems.BLACKSTONE_DUST.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, CrockPotItems.COLLECTED_DUST.get())
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
            smeltingRecipe(pFinishedRecipeConsumer, input, RecipeCategory.FOOD, output, 0.35F, 200);
            smokingRecipe(pFinishedRecipeConsumer, input, RecipeCategory.FOOD, output, 0.35F, 100);
            campfireCookingRecipe(pFinishedRecipeConsumer, input, RecipeCategory.FOOD, output, 0.35F, 600);
        });
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(CrockPotItemTags.PARROT_EGGS), RecipeCategory.FOOD, CrockPotItems.COOKED_EGG.get(), 0.35F, 200)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("smelting", getItemName(CrockPotItems.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(CrockPotItemTags.PARROT_EGGS), RecipeCategory.FOOD, CrockPotItems.COOKED_EGG.get(), 0.35F, 100)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("smoking", getItemName(CrockPotItems.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(CrockPotItemTags.PARROT_EGGS), RecipeCategory.FOOD, CrockPotItems.COOKED_EGG.get(), 0.35F, 600)
                .unlockedBy("has_parrot_eggs", has(CrockPotItemTags.PARROT_EGGS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("campfire_cooking", getItemName(CrockPotItems.COOKED_EGG.get()) + "_by_parrot_eggs"));

        var vanillaSeedsRecipes = Map.of(
                Items.WHEAT, Items.WHEAT_SEEDS,
                Items.BEETROOT, Items.BEETROOT_SEEDS,
                Items.MELON_SLICE, Items.MELON_SEEDS
        );
        vanillaSeedsRecipes.forEach((input, output) -> ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 2, 4).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", output)));
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(Items.PUMPKIN), Items.PUMPKIN_SEEDS, 6, 8).save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", Items.PUMPKIN_SEEDS));

        var seedsRecipes = Map.of(
                CrockPotItems.ASPARAGUS.get(), CrockPotItems.ASPARAGUS_SEEDS.get(),
                CrockPotItems.CORN.get(), CrockPotItems.CORN_SEEDS.get(),
                CrockPotItems.EGGPLANT.get(), CrockPotItems.EGGPLANT_SEEDS.get(),
                CrockPotItems.GARLIC.get(), CrockPotItems.GARLIC_SEEDS.get(),
                CrockPotItems.ONION.get(), CrockPotItems.ONION_SEEDS.get(),
                CrockPotItems.PEPPER.get(), CrockPotItems.PEPPER_SEEDS.get(),
                CrockPotItems.TOMATO.get(), CrockPotItems.TOMATO_SEEDS.get()
        );
        seedsRecipes.forEach((input, output) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output)
                    .requires(Ingredient.of(input))
                    .unlockedBy(getHasName(input), has(input))
                    .save(pFinishedRecipeConsumer, getSimpleRecipeName("crafting", output));
            ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 2, 4)
                    .save(pFinishedRecipeConsumer, getSimpleRecipeName("parrot_feeding", output));
        });

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

        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.ASPARAGUS_SOUP.get(), 10, 10 * 20, 1)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_ASPARAGUS, CrockPotItemTags.CROPS_ASPARAGUS))
                .requirementCategoryMinExclusive(FoodCategory.VEGGIE, 2.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.ASPARAGUS_SOUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.AVAJ.get(), 30, 10 * 20, 1)
                .requirementCombinationOr(
                        new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 4),
                        new RequirementCombinationAnd(
                                new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 3),
                                new RequirementCombinationOr(
                                        new RequirementCategoryMinExclusive(FoodCategory.DAIRY, 0.0F),
                                        new RequirementCategoryMinExclusive(FoodCategory.SWEETENER, 0.0F)
                                )
                        )
                ).save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.AVAJ.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.BACON_EGGS.get(), 10, 30 * 20, 0)
                .requirementCategoryMinExclusive(FoodCategory.EGG, 1.0F)
                .requirementCategoryMinExclusive(FoodCategory.MEAT, 1.0F)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.BACON_EGGS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.BONE_SOUP.get(), 30, 30 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Tags.Items.BONES), 2)
                .requirementMustContainIngredientLessThan(Ingredient.of(Tags.Items.BONES), 2)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_ONION, CrockPotItemTags.CROPS_ONION))
                .requirementCategoryMaxExclusive(FoodCategory.INEDIBLE, 3.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.BONE_SOUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.BONE_STEW.get(), 0, 15 * 20, 0)
                .requirementCategoryMin(FoodCategory.MEAT, 3.0F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.BONE_STEW.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.BREAKFAST_SKILLET.get(), 1, 20 * 20, 0)
                .requirementCategoryMin(FoodCategory.EGG, 1.0F)
                .requirementCategoryMin(FoodCategory.VEGGIE, 1.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.DAIRY)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.BREAKFAST_SKILLET.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.BUNNY_STEW.get(), 1, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.RAW_RABBIT, CrockPotItemTags.COOKED_RABBIT))
                .requirementCategoryMin(FoodCategory.FROZEN, 2.0F)
                .requirementCategoryMax(FoodCategory.MEAT, 0.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.BUNNY_STEW.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.CALIFORNIA_ROLL.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.DRIED_KELP), 2)
                .requirementCategoryMin(FoodCategory.FISH, 1.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.CALIFORNIA_ROLL.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.CANDY.get(), 3, 15, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(CrockPotItems.SYRUP.get()))
                .requirementCategoryMin(FoodCategory.SWEETENER, 2.5F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.CANDY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.CEVICHE.get(), 20, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.FISH, 2.0F)
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.CEVICHE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.FISH_STICKS.get(), 10, 30 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.FISH)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementCategoryMax(FoodCategory.INEDIBLE, 1.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.FISH_STICKS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.FISH_TACOS.get(), 10, 10 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.FISH)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(CrockPotItemTags.VEGETABLES_CORN, CrockPotItemTags.CROPS_CORN), Ingredient.of(CrockPotItems.POPCORN.get())))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.FISH_TACOS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.FLOWER_SALAD.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.CHORUS_FLOWER))
                .requirementCategoryMin(FoodCategory.VEGGIE, 2.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .requirementWithoutCategory(FoodCategory.SWEETENER)
                .requirementWithoutCategory(FoodCategory.FRUIT)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.FLOWER_SALAD.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.FROGGLE_BUNWICH.get(), 1, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.RAW_FROGS, CrockPotItemTags.COOKED_FROGS))
                .requirementCategoryMin(FoodCategory.VEGGIE, 0.5F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.FROGGLE_BUNWICH.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.FRUIT_MEDLEY.get(), 0, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.FRUIT, 3.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.FRUIT_MEDLEY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.GAZPACHO.get(), 30, 10 * 20, 1)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_ASPARAGUS, CrockPotItemTags.CROPS_ASPARAGUS), 2)
                .requirementCategoryMin(FoodCategory.FROZEN, 2.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.GAZPACHO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.GLOW_BERRY_MOUSSE.get(), 30, 20 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.GLOW_BERRIES), 2)
                .requirementCategoryMin(FoodCategory.FRUIT, 2.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.GLOW_BERRY_MOUSSE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.HONEY_HAM.get(), 2, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.HONEYCOMB, Items.HONEY_BOTTLE))
                .requirementCategoryMinExclusive(FoodCategory.MEAT, 1.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.HONEY_HAM.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.HONEY_NUGGETS.get(), 2, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.HONEYCOMB, Items.HONEY_BOTTLE))
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementCategoryMax(FoodCategory.MEAT, 1.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.HONEY_NUGGETS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.HOT_CHILI.get(), 10, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.MEAT, 1.5F)
                .requirementCategoryMin(FoodCategory.VEGGIE, 1.5F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.HOT_CHILI.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.HOT_COCOA.get(), 30, 10 * 20, 0)
                .requirementCombinationOr(
                        new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 4),
                        new RequirementCombinationAnd(
                                new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 3),
                                new RequirementCombinationOr(
                                        new RequirementCategoryMinExclusive(FoodCategory.DAIRY, 0.0F),
                                        new RequirementCategoryMinExclusive(FoodCategory.SWEETENER, 0.0F)
                                )
                        )
                )
                .weight(199)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.HOT_COCOA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.ICE_CREAM.get(), 10, 10 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .requirementWithAnyCategory(FoodCategory.DAIRY)
                .requirementWithAnyCategory(FoodCategory.SWEETENER)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.ICE_CREAM.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.ICED_TEA.get(), 30, 10 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.FERN), 2)
                .requirementWithAnyCategory(FoodCategory.SWEETENER)
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.ICED_TEA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.JAMMY_PRESERVES.get(), 0, 10 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.FRUIT)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.JAMMY_PRESERVES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.KABOBS.get(), 5, 30 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementCategoryMax(FoodCategory.MONSTER, 1.0F)
                .requirementCategoryMax(FoodCategory.INEDIBLE, 1.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.KABOBS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.MASHED_POTATOES.get(), 20, 20 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(CrockPotItemTags.VEGETABLES_POTATO, Tags.Items.CROPS_POTATO), Ingredient.of(Items.BAKED_POTATO)), 2)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_GARLIC, CrockPotItemTags.CROPS_GARLIC))
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.MASHED_POTATOES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.MEAT_BALLS.get(), -1, 15 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.MEAT_BALLS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.MILKMADE_HAT.get(), 55, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(CrockPotItems.HOGLIN_NOSE.get()))
                .requirementMustContainIngredient(Ingredient.of(Items.BAMBOO))
                .requirementCategoryMin(FoodCategory.DAIRY, 1.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.MILKMADE_HAT.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.MONSTER_LASAGNA.get(), 10, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.MONSTER, 2.0F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.MONSTER_LASAGNA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.MONSTER_TARTARE.get(), 30, 20 * 30, 1)
                .requirementCategoryMin(FoodCategory.MONSTER, 2.0F)
                .requirementCategoryMin(FoodCategory.EGG, 1.0F)
                .requirementCategoryMin(FoodCategory.VEGGIE, 0.5F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.MONSTER_TARTARE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.MOQUECA.get(), 40, 20 * 30, 1)
                .requirementWithAnyCategory(FoodCategory.FISH)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_ONION, CrockPotItemTags.CROPS_ONION))
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_TOMATO, CrockPotItemTags.CROPS_TOMATO))
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.MOQUECA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.MUSHY_CAKE.get(), 55, 20 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.BROWN_MUSHROOM))
                .requirementMustContainIngredient(Ingredient.of(Items.RED_MUSHROOM))
                .requirementMustContainIngredient(Ingredient.of(Items.CRIMSON_FUNGUS))
                .requirementMustContainIngredient(Ingredient.of(Items.WARPED_FUNGUS))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.MUSHY_CAKE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.NETHEROSIA.get(), 100, 80 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(CrockPotItems.COLLECTED_DUST.get()))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.NETHEROSIA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.PEPPER_POPPER.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_PEPPER, CrockPotItemTags.CROPS_PEPPER))
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementCategoryMax(FoodCategory.MEAT, 1.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.PEPPER_POPPER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.PEROGIES.get(), 5, 20 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.EGG)
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementWithAnyCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.PEROGIES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.PLAIN_OMELETTE.get(), 1, 20 * 20, 0)
                .requirementCategoryMin(FoodCategory.EGG, 3.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.PLAIN_OMELETTE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.POTATO_SOUFFLE.get(), 30, 20 * 20, 1)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(CrockPotItemTags.VEGETABLES_POTATO, Tags.Items.CROPS_POTATO), Ingredient.of(Items.BAKED_POTATO)), 2)
                .requirementWithAnyCategory(FoodCategory.EGG)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.POTATO_SOUFFLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.POTATO_TORNADO.get(), 10, 15 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(CrockPotItemTags.VEGETABLES_POTATO, Tags.Items.CROPS_POTATO), Ingredient.of(Items.BAKED_POTATO)))
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementCategoryMax(FoodCategory.MONSTER, 1.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementCategoryMax(FoodCategory.INEDIBLE, 2.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.POTATO_TORNADO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.POW_CAKE.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementMustContainIngredient(Ingredient.of(Items.HONEYCOMB, Items.HONEY_BOTTLE))
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(CrockPotItemTags.VEGETABLES_CORN, CrockPotItemTags.CROPS_CORN), Ingredient.of(CrockPotItems.POPCORN.get())))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.POW_CAKE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.PUMPKIN_COOKIE.get(), 10, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_PUMPKIN))
                .requirementCategoryMin(FoodCategory.SWEETENER, 2.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.PUMPKIN_COOKIE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.RATATOUILLE.get(), 0, 20 * 20, 0)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithAnyCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.RATATOUILLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.SALMON_SUSHI.get(), 2, 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.RAW_FISHES_SALMON, CrockPotItemTags.COOKED_FISHES_SALMON))
                .requirementMustContainIngredient(Ingredient.of(Items.DRIED_KELP))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.SALMON_SUSHI.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.SALSA.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_TOMATO, CrockPotItemTags.CROPS_TOMATO))
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_ONION, CrockPotItemTags.CROPS_ONION))
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.SALSA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.SCOTCH_EGG.get(), 10, 20 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.TURTLE_EGG))
                .requirementCategoryMin(FoodCategory.VEGGIE, 1.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.SCOTCH_EGG.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.SEAFOOD_GUMBO.get(), 10, 20 * 20, 0)
                .requirementCategoryMinExclusive(FoodCategory.FISH, 2.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.SEAFOOD_GUMBO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.STUFFED_EGGPLANT.get(), 1, 30 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(CrockPotItemTags.VEGETABLES_EGGPLANT, CrockPotItemTags.CROPS_EGGPLANT), Ingredient.of(CrockPotItems.COOKED_EGGPLANT.get())))
                .requirementCategoryMinExclusive(FoodCategory.VEGGIE, 1.0F)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.STUFFED_EGGPLANT.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.SURF_N_TURF.get(), 30, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.MEAT, 2.5F)
                .requirementCategoryMin(FoodCategory.FISH, 1.5F)
                .requirementWithoutCategory(FoodCategory.FROZEN)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.SURF_N_TURF.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.SYRUP.get(), 40, 10 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(CrockPotItemTags.VEGETABLES_CORN, CrockPotItemTags.CROPS_CORN), Ingredient.of(CrockPotItems.POPCORN.get()), Ingredient.of(Items.HONEYCOMB)), 4)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.SYRUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.TAFFY.get(), 2, 10, 20 * 20, 0)
                .requirementCategoryMin(FoodCategory.SWEETENER, 3.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.TAFFY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.TEA.get(), 25, 10 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.FERN), 2)
                .requirementWithAnyCategory(FoodCategory.SWEETENER)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementCategoryMax(FoodCategory.VEGGIE, 0.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.TEA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.TROPICAL_BOUILLABAISSE.get(), 35, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.RAW_FISHES_TROPICAL_FISH), 2)
                .requirementCategoryMinExclusive(FoodCategory.FISH, 2.5F)
                .requirementWithAnyCategory(FoodCategory.VEGGIE)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.TROPICAL_BOUILLABAISSE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.TURKEY_DINNER.get(), 10, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(CrockPotItemTags.RAW_CHICKEN), 2)
                .requirementCategoryMinExclusive(FoodCategory.MEAT, 1.5F)
                .requirementCombinationOr(new RequirementCategoryMinExclusive(FoodCategory.VEGGIE, 0.0F), new RequirementCategoryMinExclusive(FoodCategory.FRUIT, 0.0F))
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.TURKEY_DINNER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.VEG_STINGER.get(), 15, 10 * 20, 1)
                .requirementMustContainIngredient(getIngredientFromTags(CrockPotItemTags.VEGETABLES_ASPARAGUS, CrockPotItemTags.CROPS_ASPARAGUS, CrockPotItemTags.VEGETABLES_TOMATO, CrockPotItemTags.CROPS_TOMATO))
                .requirementCategoryMinExclusive(FoodCategory.VEGGIE, 2.0F)
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.VEG_STINGER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.VOLT_GOAT_JELLY.get(), 40, 30 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(CrockPotItems.VOLT_GOAT_HORN.get()))
                .requirementCategoryMin(FoodCategory.SWEETENER, 2.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.VOLT_GOAT_JELLY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.WATERMELON_ICLE.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Items.MELON_SLICE), Ingredient.of(Items.MELON)))
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.WATERMELON_ICLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(CrockPotItems.WET_GOOP.get(), -2, 10 * 20, 0)
                .save(pFinishedRecipeConsumer, getSimpleRecipeName("crock_pot_cooking", CrockPotItems.WET_GOOP.get()));
    }

    protected static void smeltingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(pIngredient), category, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(pFinishedRecipeConsumer, getSimpleRecipeName("smelting", pResult));
    }

    protected static void smokingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(pIngredient), category, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(pFinishedRecipeConsumer, getSimpleRecipeName("smoking", pResult));
    }

    protected static void campfireCookingRecipe(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(pIngredient), category, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(pFinishedRecipeConsumer, getSimpleRecipeName("campfire_cooking", pResult));
    }

    @SafeVarargs
    protected static Ingredient getIngredientFromTags(TagKey<Item>... pTags) {
        return CompoundIngredient.of(Arrays.stream(pTags).map(Ingredient::of).toArray(Ingredient[]::new));
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
}
