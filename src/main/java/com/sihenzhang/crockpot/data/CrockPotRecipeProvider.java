package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.data.recipes.CrockPotCookingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ExplosionCraftingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ParrotFeedingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.PiglinBarteringRecipeBuilder;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMinExclusive;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationAnd;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationOr;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredient;
import com.sihenzhang.crockpot.tag.ModItemTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class CrockPotRecipeProvider extends RecipeProvider {
    public CrockPotRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CROCK_POT.get())
                .define('B', Items.STONE)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', ItemTags.COALS)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("SCS")
                .unlockedBy(getHasName(Items.STONE), has(Items.STONE))
                .unlockedBy("has_coal", has(ItemTags.COALS))
                .save(recipeOutput, getSimpleRecipeName("crafting", ModItems.CROCK_POT.get()));
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get()), Ingredient.of(ModItems.CROCK_POT.get()), Ingredient.of(Tags.Items.STORAGE_BLOCKS_COPPER), RecipeCategory.MISC, ModItems.PORTABLE_CROCK_POT.get())
                .unlocks(getHasName(ModItems.CROCK_POT.get()), has(ModItems.CROCK_POT.get()))
                .save(recipeOutput, getSimpleRecipeName("smithing", ModItems.PORTABLE_CROCK_POT.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.BIRDCAGE.get())
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', Tags.Items.INGOTS_GOLD)
                .pattern("NNN")
                .pattern("N N")
                .pattern("III")
                .unlockedBy("has_gold_ingots", has(Tags.Items.INGOTS_GOLD))
                .save(recipeOutput, getSimpleRecipeName("crafting", ModItems.BIRDCAGE.get()));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('S', ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get())
                .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
                .pattern("GSG")
                .pattern("GCG")
                .pattern("GGG")
                .unlockedBy(getHasName(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get()), has(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(recipeOutput, getSimpleRecipeName("crafting", ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get()));

        ExplosionCraftingRecipeBuilder.explosionCrafting(ModItems.BLACKSTONE_DUST.get(), Ingredient.of(Items.BLACKSTONE)).lossRate(0.75F).onlyBlock()
                .save(recipeOutput, getSimpleRecipeName("explosion_crafting", ModItems.BLACKSTONE_DUST.get()));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.COLLECTED_DUST.get())
                .requires(Tags.Items.GEMS_QUARTZ)
                .requires(ModItems.BLACKSTONE_DUST.get(), 2)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .unlockedBy(getHasName(ModItems.BLACKSTONE_DUST.get()), has(ModItems.BLACKSTONE_DUST.get()))
                .save(recipeOutput, getSimpleRecipeName("crafting", ModItems.COLLECTED_DUST.get()));

        var cookingRecipes = Map.of(
                ModItems.CORN.get(), ModItems.POPCORN.get(),
                Items.EGG, ModItems.COOKED_EGG.get(),
                ModItems.EGGPLANT.get(), ModItems.COOKED_EGGPLANT.get(),
                ModItems.FROG_LEGS.get(), ModItems.COOKED_FROG_LEGS.get(),
                ModItems.HOGLIN_NOSE.get(), ModItems.COOKED_HOGLIN_NOSE.get()
        );
        cookingRecipes.forEach((input, output) -> {
            smeltingRecipe(recipeOutput, input, RecipeCategory.FOOD, output, 0.35F, 200);
            smokingRecipe(recipeOutput, input, RecipeCategory.FOOD, output, 0.35F, 100);
            campfireCookingRecipe(recipeOutput, input, RecipeCategory.FOOD, output, 0.35F, 600);
        });
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ModItemTags.PARROT_EGGS), RecipeCategory.FOOD, ModItems.COOKED_EGG.get(), 0.35F, 200)
                .unlockedBy("has_parrot_eggs", has(ModItemTags.PARROT_EGGS))
                .save(recipeOutput, getSimpleRecipeName("smelting", getItemName(ModItems.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(ModItemTags.PARROT_EGGS), RecipeCategory.FOOD, ModItems.COOKED_EGG.get(), 0.35F, 100)
                .unlockedBy("has_parrot_eggs", has(ModItemTags.PARROT_EGGS))
                .save(recipeOutput, getSimpleRecipeName("smoking", getItemName(ModItems.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ModItemTags.PARROT_EGGS), RecipeCategory.FOOD, ModItems.COOKED_EGG.get(), 0.35F, 600)
                .unlockedBy("has_parrot_eggs", has(ModItemTags.PARROT_EGGS))
                .save(recipeOutput, getSimpleRecipeName("campfire_cooking", getItemName(ModItems.COOKED_EGG.get()) + "_by_parrot_eggs"));

        var vanillaSeedsRecipes = Map.of(
                Items.WHEAT, Items.WHEAT_SEEDS,
                Items.BEETROOT, Items.BEETROOT_SEEDS
        );
        vanillaSeedsRecipes.forEach((input, output) -> ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 1, 3).save(recipeOutput, getSimpleRecipeName("parrot_feeding", output)));
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(Items.PUMPKIN), Items.PUMPKIN_SEEDS, 4, 6).save(recipeOutput, getSimpleRecipeName("parrot_feeding", Items.PUMPKIN_SEEDS));
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(Items.MELON_SLICE), Items.MELON_SEEDS, 1, 2).save(recipeOutput, getSimpleRecipeName("parrot_feeding", Items.MELON_SEEDS));
        var snifferSeedsRecipes = Map.of(
                Items.TORCHFLOWER, Items.TORCHFLOWER_SEEDS,
                Items.PITCHER_PLANT, Items.PITCHER_POD
        );
        snifferSeedsRecipes.forEach((input, output) -> ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 1, 2).save(recipeOutput, getSimpleRecipeName("parrot_feeding", output)));

        var seedsRecipes = Map.of(
                ModItems.ASPARAGUS.get(), ModItems.ASPARAGUS_SEEDS.get(),
                ModItems.CORN.get(), ModItems.CORN_SEEDS.get(),
                ModItems.EGGPLANT.get(), ModItems.EGGPLANT_SEEDS.get(),
                ModItems.GARLIC.get(), ModItems.GARLIC_SEEDS.get(),
                ModItems.ONION.get(), ModItems.ONION_SEEDS.get(),
                ModItems.PEPPER.get(), ModItems.PEPPER_SEEDS.get(),
                ModItems.TOMATO.get(), ModItems.TOMATO_SEEDS.get()
        );
        seedsRecipes.forEach((input, output) -> {
            ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, output)
                    .requires(Ingredient.of(input))
                    .unlockedBy(getHasName(input), has(input))
                    .save(recipeOutput, getSimpleRecipeName("crafting", output));
            ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 2, 4)
                    .save(recipeOutput, getSimpleRecipeName("parrot_feeding", output));
        });

        PiglinBarteringRecipeBuilder.piglinBartering(Ingredient.of(ModItems.NETHEROSIA.get()))
                .addResult(ModItems.HOGLIN_NOSE.get(), 1, 2, 20)
                .addResult(Items.PORKCHOP, 2, 4, 20)
                .addResult(Items.LEATHER, 2, 4, 20)
                .addResult(Items.BONE, 2, 4, 20)
                .addResult(Items.STRING, 2, 4, 20)
                .addResult(Items.MAGMA_CREAM, 2, 3, 15)
                .addResult(Items.BLAZE_POWDER, 2, 3, 15)
                .addResult(Items.ENDER_PEARL, 2, 3, 15)
                .addResult(Items.GHAST_TEAR, 15)
                .addResult(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 15)
                .addResult(Items.BROWN_MUSHROOM, 2, 4, 6)
                .addResult(Items.RED_MUSHROOM, 2, 4, 6)
                .addResult(Items.CRIMSON_FUNGUS, 2, 4, 6)
                .addResult(Items.WARPED_FUNGUS, 2, 4, 6)
                .addResult(Items.WITHER_SKELETON_SKULL, 1)
                .save(recipeOutput, getSimpleRecipeName("piglin_bartering", ModItems.NETHEROSIA.get()));

        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.ASPARAGUS_SOUP.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_ASPARAGUS, ModItemTags.CROPS_ASPARAGUS))
                .requirementCategoryMinExclusive(FoodCategory.VEGGIE, 2.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.ASPARAGUS_SOUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.AVAJ.get(), 30, 10 * 20, 1)
                .requirementCombinationOr(
                        new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 4),
                        new RequirementCombinationAnd(
                                new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 3),
                                new RequirementCombinationOr(
                                        new RequirementCategoryMinExclusive(FoodCategory.DAIRY, 0.0F),
                                        new RequirementCategoryMinExclusive(FoodCategory.SWEETENER, 0.0F)
                                )
                        )
                ).save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.AVAJ.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BACON_EGGS.get(), 10, 30 * 20, 0)
                .requirementCategoryMinExclusive(FoodCategory.EGG, 1.0F)
                .requirementCategoryMinExclusive(FoodCategory.MEAT, 1.0F)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.BACON_EGGS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BONE_SOUP.get(), 30, 30 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Tags.Items.BONES), 2)
                .requirementMustContainIngredientLessThan(Ingredient.of(Tags.Items.BONES), 2)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_ONION, ModItemTags.CROPS_ONION))
                .requirementCategoryMaxExclusive(FoodCategory.INEDIBLE, 3.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.BONE_SOUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BONE_STEW.get(), 0, 15 * 20, 0)
                .requirementCategoryMin(FoodCategory.MEAT, 3.0F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.BONE_STEW.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BREAKFAST_SKILLET.get(), 1, 20 * 20, 0)
                .requirementCategoryMin(FoodCategory.EGG, 1.0F)
                .requirementCategoryMin(FoodCategory.VEGGIE, 1.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.DAIRY)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.BREAKFAST_SKILLET.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BUNNY_STEW.get(), 1, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.RAW_RABBIT, ModItemTags.COOKED_RABBIT))
                .requirementCategoryMin(FoodCategory.FROZEN, 2.0F)
                .requirementCategoryMax(FoodCategory.MEAT, 0.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.BUNNY_STEW.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.CALIFORNIA_ROLL.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.DRIED_KELP), 2)
                .requirementCategoryMin(FoodCategory.FISH, 1.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.CALIFORNIA_ROLL.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.CANDY.get(), 3, 15, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(ModItems.SYRUP.get()))
                .requirementCategoryMin(FoodCategory.SWEETENER, 2.5F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.CANDY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.CEVICHE.get(), 20, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.FISH, 2.0F)
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.CEVICHE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FISH_STICKS.get(), 10, 30 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.FISH)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementCategoryMax(FoodCategory.INEDIBLE, 1.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.FISH_STICKS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FISH_TACOS.get(), 10, 10 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.FISH)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(ModItemTags.VEGETABLES_CORN, ModItemTags.CROPS_CORN), Ingredient.of(ModItems.POPCORN.get())))
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.FISH_TACOS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FLOWER_SALAD.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.CHORUS_FLOWER))
                .requirementCategoryMin(FoodCategory.VEGGIE, 2.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .requirementWithoutCategory(FoodCategory.SWEETENER)
                .requirementWithoutCategory(FoodCategory.FRUIT)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.FLOWER_SALAD.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FROGGLE_BUNWICH.get(), 1, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.RAW_FROGS, ModItemTags.COOKED_FROGS))
                .requirementCategoryMin(FoodCategory.VEGGIE, 0.5F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.FROGGLE_BUNWICH.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FRUIT_MEDLEY.get(), 0, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.FRUIT, 3.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.FRUIT_MEDLEY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.GAZPACHO.get(), 30, 10 * 20, 1)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_ASPARAGUS, ModItemTags.CROPS_ASPARAGUS), 2)
                .requirementCategoryMin(FoodCategory.FROZEN, 2.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.GAZPACHO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.GLOW_BERRY_MOUSSE.get(), 30, 20 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.GLOW_BERRIES), 2)
                .requirementCategoryMin(FoodCategory.FRUIT, 2.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.GLOW_BERRY_MOUSSE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.HONEY_HAM.get(), 2, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.HONEYCOMB, Items.HONEY_BOTTLE))
                .requirementCategoryMinExclusive(FoodCategory.MEAT, 1.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.HONEY_HAM.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.HONEY_NUGGETS.get(), 2, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.HONEYCOMB, Items.HONEY_BOTTLE))
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementCategoryMax(FoodCategory.MEAT, 1.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.HONEY_NUGGETS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.HOT_CHILI.get(), 10, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.MEAT, 1.5F)
                .requirementCategoryMin(FoodCategory.VEGGIE, 1.5F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.HOT_CHILI.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.HOT_COCOA.get(), 30, 10 * 20, 0)
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
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.HOT_COCOA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.ICE_CREAM.get(), 10, 10 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .requirementWithAnyCategory(FoodCategory.DAIRY)
                .requirementWithAnyCategory(FoodCategory.SWEETENER)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.ICE_CREAM.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.ICED_TEA.get(), 30, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.FERN), 2)
                .requirementWithAnyCategory(FoodCategory.SWEETENER)
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.ICED_TEA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.JAMMY_PRESERVES.get(), 0, 10 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.FRUIT)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.JAMMY_PRESERVES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.KABOBS.get(), 5, 30 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementCategoryMax(FoodCategory.MONSTER, 1.0F)
                .requirementCategoryMax(FoodCategory.INEDIBLE, 1.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.KABOBS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MASHED_POTATOES.get(), 20, 20 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(ModItemTags.VEGETABLES_POTATO, Tags.Items.CROPS_POTATO), Ingredient.of(Items.BAKED_POTATO)), 2)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_GARLIC, ModItemTags.CROPS_GARLIC))
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.MASHED_POTATOES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MEAT_BALLS.get(), -1, 15 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.MEAT_BALLS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MILKMADE_HAT.get(), 55, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(ModItems.HOGLIN_NOSE.get()))
                .requirementMustContainIngredient(Ingredient.of(Items.BAMBOO))
                .requirementCategoryMin(FoodCategory.DAIRY, 1.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.MILKMADE_HAT.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MONSTER_LASAGNA.get(), 10, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.MONSTER, 2.0F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.MONSTER_LASAGNA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MONSTER_TARTARE.get(), 30, 20 * 30, 1)
                .requirementCategoryMin(FoodCategory.MONSTER, 2.0F)
                .requirementCategoryMin(FoodCategory.EGG, 1.0F)
                .requirementCategoryMin(FoodCategory.VEGGIE, 0.5F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.MONSTER_TARTARE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MOQUECA.get(), 40, 20 * 30, 1)
                .requirementWithAnyCategory(FoodCategory.FISH)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_ONION, ModItemTags.CROPS_ONION))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_TOMATO, ModItemTags.CROPS_TOMATO))
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.MOQUECA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MUSHY_CAKE.get(), 55, 20 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.BROWN_MUSHROOM))
                .requirementMustContainIngredient(Ingredient.of(Items.RED_MUSHROOM))
                .requirementMustContainIngredient(Ingredient.of(Items.CRIMSON_FUNGUS))
                .requirementMustContainIngredient(Ingredient.of(Items.WARPED_FUNGUS))
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.MUSHY_CAKE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.NETHEROSIA.get(), 100, 80 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(ModItems.COLLECTED_DUST.get()))
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.NETHEROSIA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.PEPPER_POPPER.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_PEPPER, ModItemTags.CROPS_PEPPER))
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementCategoryMax(FoodCategory.MEAT, 1.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.PEPPER_POPPER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.PEROGIES.get(), 5, 20 * 20, 0)
                .requirementWithAnyCategory(FoodCategory.EGG)
                .requirementWithAnyCategory(FoodCategory.MEAT)
                .requirementWithAnyCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.PEROGIES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.PLAIN_OMELETTE.get(), 1, 20 * 20, 0)
                .requirementCategoryMin(FoodCategory.EGG, 3.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.PLAIN_OMELETTE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.POTATO_SOUFFLE.get(), 30, 20 * 20, 1)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(ModItemTags.VEGETABLES_POTATO, Tags.Items.CROPS_POTATO), Ingredient.of(Items.BAKED_POTATO)), 2)
                .requirementWithAnyCategory(FoodCategory.EGG)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.POTATO_SOUFFLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.POTATO_TORNADO.get(), 10, 15 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(ModItemTags.VEGETABLES_POTATO, Tags.Items.CROPS_POTATO), Ingredient.of(Items.BAKED_POTATO)))
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementCategoryMax(FoodCategory.MONSTER, 1.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementCategoryMax(FoodCategory.INEDIBLE, 2.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.POTATO_TORNADO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.POW_CAKE.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementMustContainIngredient(Ingredient.of(Items.HONEYCOMB, Items.HONEY_BOTTLE))
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(ModItemTags.VEGETABLES_CORN, ModItemTags.CROPS_CORN), Ingredient.of(ModItems.POPCORN.get())))
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.POW_CAKE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.PUMPKIN_COOKIE.get(), 10, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_PUMPKIN))
                .requirementCategoryMin(FoodCategory.SWEETENER, 2.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.PUMPKIN_COOKIE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.RATATOUILLE.get(), 0, 20 * 20, 0)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithAnyCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.RATATOUILLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SALMON_SUSHI.get(), 2, 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.RAW_FISHES_SALMON, ModItemTags.COOKED_FISHES_SALMON))
                .requirementMustContainIngredient(Ingredient.of(Items.DRIED_KELP))
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.SALMON_SUSHI.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SALSA.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_TOMATO, ModItemTags.CROPS_TOMATO))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_ONION, ModItemTags.CROPS_ONION))
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.SALSA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SCOTCH_EGG.get(), 10, 20 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.TURTLE_EGG))
                .requirementCategoryMin(FoodCategory.VEGGIE, 1.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.SCOTCH_EGG.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SEAFOOD_GUMBO.get(), 10, 20 * 20, 0)
                .requirementCategoryMinExclusive(FoodCategory.FISH, 2.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.SEAFOOD_GUMBO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.STUFFED_EGGPLANT.get(), 1, 30 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(ModItemTags.VEGETABLES_EGGPLANT, ModItemTags.CROPS_EGGPLANT), Ingredient.of(ModItems.COOKED_EGGPLANT.get())))
                .requirementCategoryMinExclusive(FoodCategory.VEGGIE, 1.0F)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.STUFFED_EGGPLANT.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SURF_N_TURF.get(), 30, 10 * 20, 0)
                .requirementCategoryMin(FoodCategory.MEAT, 2.5F)
                .requirementCategoryMin(FoodCategory.FISH, 1.5F)
                .requirementWithoutCategory(FoodCategory.FROZEN)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.SURF_N_TURF.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SYRUP.get(), 40, 10 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(getIngredientFromTags(ModItemTags.VEGETABLES_CORN, ModItemTags.CROPS_CORN), Ingredient.of(ModItems.POPCORN.get()), Ingredient.of(Items.HONEYCOMB)), 4)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.SYRUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.TAFFY.get(), 2, 10, 20 * 20, 0)
                .requirementCategoryMin(FoodCategory.SWEETENER, 3.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.TAFFY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.TEA.get(), 25, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.FERN), 2)
                .requirementWithAnyCategory(FoodCategory.SWEETENER)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementCategoryMax(FoodCategory.VEGGIE, 0.5F)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.TEA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.TROPICAL_BOUILLABAISSE.get(), 35, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.RAW_FISHES_TROPICAL_FISH), 2)
                .requirementCategoryMinExclusive(FoodCategory.FISH, 2.5F)
                .requirementWithAnyCategory(FoodCategory.VEGGIE)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.TROPICAL_BOUILLABAISSE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.TURKEY_DINNER.get(), 10, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(ModItemTags.RAW_CHICKEN), 2)
                .requirementCategoryMinExclusive(FoodCategory.MEAT, 1.0F)
                .requirementCombinationOr(new RequirementCategoryMinExclusive(FoodCategory.VEGGIE, 0.0F), new RequirementCategoryMinExclusive(FoodCategory.FRUIT, 0.0F))
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.TURKEY_DINNER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.VEG_STINGER.get(), 15, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.VEGETABLES_ASPARAGUS, ModItemTags.CROPS_ASPARAGUS, ModItemTags.VEGETABLES_TOMATO, ModItemTags.CROPS_TOMATO))
                .requirementCategoryMinExclusive(FoodCategory.VEGGIE, 2.0F)
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.INEDIBLE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.VEG_STINGER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.VOLT_GOAT_JELLY.get(), 40, 30 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(ModItems.VOLT_GOAT_HORN.get()))
                .requirementCategoryMin(FoodCategory.SWEETENER, 2.0F)
                .requirementWithoutCategory(FoodCategory.MEAT)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.VOLT_GOAT_JELLY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.WATERMELON_ICLE.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Items.MELON_SLICE), Ingredient.of(Items.MELON)))
                .requirementWithAnyCategory(FoodCategory.FROZEN)
                .requirementMustContainIngredient(CompoundIngredient.of(Ingredient.of(Tags.Items.RODS_WOODEN), Ingredient.of(Items.BAMBOO)))
                .requirementWithoutCategory(FoodCategory.MEAT)
                .requirementWithoutCategory(FoodCategory.VEGGIE)
                .requirementWithoutCategory(FoodCategory.EGG)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.WATERMELON_ICLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.WET_GOOP.get(), -10, 10 * 20, 0)
                .save(recipeOutput, getSimpleRecipeName("crock_pot_cooking", ModItems.WET_GOOP.get()));
    }

    protected static void smeltingRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(pIngredient), category, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(recipeOutput, getSimpleRecipeName("smelting", pResult));
    }

    protected static void smokingRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(pIngredient), category, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(recipeOutput, getSimpleRecipeName("smoking", pResult));
    }

    protected static void campfireCookingRecipe(RecipeOutput recipeOutput, ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(pIngredient), category, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), has(pIngredient)).save(recipeOutput, getSimpleRecipeName("campfire_cooking", pResult));
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
