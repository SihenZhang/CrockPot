package com.sihenzhang.crockpot.data;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.registry.FoodCategories;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.data.recipes.CrockPotCookingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.DryingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ExplosionCraftingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.ParrotFeedingRecipeBuilder;
import com.sihenzhang.crockpot.data.recipes.PiglinBarteringRecipeBuilder;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCategoryMinExclusive;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationAnd;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementCombinationOr;
import com.sihenzhang.crockpot.recipe.cooking.requirement.RequirementMustContainIngredient;
import com.sihenzhang.crockpot.registry.ModRegistries;
import com.sihenzhang.crockpot.tag.ModItemTags;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.crafting.CompoundIngredient;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
        super(registries, output);
    }

    @Override
    protected void buildRecipes() {
        this.shaped(RecipeCategory.MISC, ModItems.CROCK_POT.get())
                .define('B', Items.STONE)
                .define('S', Tags.Items.RODS_WOODEN)
                .define('C', ItemTags.COALS)
                .pattern("BBB")
                .pattern("BBB")
                .pattern("SCS")
                .unlockedBy(getHasName(Items.STONE), this.has(Items.STONE))
                .unlockedBy("has_coal", this.has(ItemTags.COALS))
                .save(this.output, getSimpleRecipeName("crafting", ModItems.CROCK_POT.get()));
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get()), Ingredient.of(ModItems.CROCK_POT.get()), this.tag(Tags.Items.STORAGE_BLOCKS_COPPER), RecipeCategory.MISC, ModItems.PORTABLE_CROCK_POT.get())
                .unlocks(getHasName(ModItems.CROCK_POT.get()), this.has(ModItems.CROCK_POT.get()))
                .save(this.output, getSimpleRecipeName("smithing", ModItems.PORTABLE_CROCK_POT.get()));

        this.shaped(RecipeCategory.MISC, ModItems.BIRDCAGE.get())
                .define('N', Tags.Items.NUGGETS_GOLD)
                .define('I', Tags.Items.INGOTS_GOLD)
                .pattern("NNN")
                .pattern("N N")
                .pattern("III")
                .unlockedBy("has_gold_ingots", this.has(Tags.Items.INGOTS_GOLD))
                .save(this.output, getSimpleRecipeName("crafting", ModItems.BIRDCAGE.get()));
        this.shaped(RecipeCategory.MISC, ModItems.DRYING_RACK.get())
                .define('P', ItemTags.WOODEN_SLABS)
                .define('R', Tags.Items.RODS_WOODEN)
                .define('S', Tags.Items.STRINGS)
                .pattern("PPP")
                .pattern("RSR")
                .pattern("R R")
                .unlockedBy("has_strings", this.has(Tags.Items.STRINGS))
                .save(this.output, getSimpleRecipeName("crafting", ModItems.DRYING_RACK.get()));

        this.shaped(RecipeCategory.MISC, ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get(), 2)
                .define('G', Tags.Items.INGOTS_GOLD)
                .define('S', ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get())
                .define('C', Tags.Items.STORAGE_BLOCKS_COPPER)
                .pattern("GSG")
                .pattern("GCG")
                .pattern("GGG")
                .unlockedBy(getHasName(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get()), this.has(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get()))
                .save(this.output, getSimpleRecipeName("crafting", ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get()));

        ExplosionCraftingRecipeBuilder.explosionCrafting(ModItems.BLACKSTONE_DUST.get(), Ingredient.of(Items.BLACKSTONE)).lossRate(0.75F).onlyBlock()
                .save(this.output, getSimpleRecipeName("explosion_crafting", ModItems.BLACKSTONE_DUST.get()));

        this.shapeless(RecipeCategory.MISC, ModItems.COLLECTED_DUST.get())
                .requires(Tags.Items.GEMS_QUARTZ)
                .requires(ModItems.BLACKSTONE_DUST.get(), 2)
                .requires(Tags.Items.DUSTS_GLOWSTONE)
                .unlockedBy(getHasName(ModItems.BLACKSTONE_DUST.get()), this.has(ModItems.BLACKSTONE_DUST.get()))
                .save(this.output, getSimpleRecipeName("crafting", ModItems.COLLECTED_DUST.get()));

        var cookingRecipes = Map.of(
                ModItems.CORN.get(), ModItems.POPCORN.get(),
                Items.EGG, ModItems.COOKED_EGG.get(),
                ModItems.EGGPLANT.get(), ModItems.COOKED_EGGPLANT.get(),
                ModItems.FROG_LEGS.get(), ModItems.COOKED_FROG_LEGS.get(),
                ModItems.HOGLIN_NOSE.get(), ModItems.COOKED_HOGLIN_NOSE.get()
        );
        cookingRecipes.forEach((input, output) -> {
            smeltingRecipe(input, RecipeCategory.FOOD, output, 0.35F, 200);
            smokingRecipe(input, RecipeCategory.FOOD, output, 0.35F, 100);
            campfireCookingRecipe(input, RecipeCategory.FOOD, output, 0.35F, 600);
        });
        SimpleCookingRecipeBuilder.smelting(this.tag(ModItemTags.PARROT_EGGS), RecipeCategory.FOOD, CookingBookCategory.FOOD, ModItems.COOKED_EGG.get(), 0.35F, 200)
                .unlockedBy("has_parrot_eggs", this.has(ModItemTags.PARROT_EGGS))
                .save(this.output, getSimpleRecipeName("smelting", getItemName(ModItems.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.smoking(this.tag(ModItemTags.PARROT_EGGS), RecipeCategory.FOOD, ModItems.COOKED_EGG.get(), 0.35F, 100)
                .unlockedBy("has_parrot_eggs", this.has(ModItemTags.PARROT_EGGS))
                .save(this.output, getSimpleRecipeName("smoking", getItemName(ModItems.COOKED_EGG.get()) + "_by_parrot_eggs"));
        SimpleCookingRecipeBuilder.campfireCooking(this.tag(ModItemTags.PARROT_EGGS), RecipeCategory.FOOD, ModItems.COOKED_EGG.get(), 0.35F, 600)
                .unlockedBy("has_parrot_eggs", this.has(ModItemTags.PARROT_EGGS))
                .save(this.output, getSimpleRecipeName("campfire_cooking", getItemName(ModItems.COOKED_EGG.get()) + "_by_parrot_eggs"));

        var meat = foodCategory(FoodCategories.MEAT);
        var monster = foodCategory(FoodCategories.MONSTER);
        var fish = foodCategory(FoodCategories.FISH);
        DryingRecipeBuilder.dryingByFoodValues(ModItems.SMALL_JERKY.get(), 5 * 60 * 20)
                .requirementCategoryMin(meat, 0.5F)
                .requirementCategoryMaxExclusive(meat, 1.0F)
                .requirementWithoutCategory(monster)
                .requirementWithoutCategory(fish)
                .save(this.output, getSimpleRecipeName("drying", ModItems.SMALL_JERKY.get()));
        DryingRecipeBuilder.dryingByFoodValues(ModItems.JERKY.get(), 10 * 60 * 20)
                .requirementCategoryMin(meat, 1.0F)
                .requirementWithoutCategory(monster)
                .requirementWithoutCategory(fish)
                .save(this.output, getSimpleRecipeName("drying", ModItems.JERKY.get()));
        DryingRecipeBuilder.dryingByFoodValues(ModItems.SMALL_MONSTER_JERKY.get(), 5 * 60 * 20)
                .requirementCategoryMin(meat, 0.5F)
                .requirementCategoryMaxExclusive(meat, 1.0F)
                .requirementWithAnyCategory(monster)
                .requirementWithoutCategory(fish)
                .save(this.output, getSimpleRecipeName("drying", ModItems.SMALL_MONSTER_JERKY.get()));
        DryingRecipeBuilder.dryingByFoodValues(ModItems.MONSTER_JERKY.get(), 10 * 60 * 20)
                .requirementCategoryMin(meat, 1.0F)
                .requirementWithAnyCategory(monster)
                .requirementWithoutCategory(fish)
                .save(this.output, getSimpleRecipeName("drying", ModItems.MONSTER_JERKY.get()));
        DryingRecipeBuilder.dryingByFoodValues(ModItems.SMALL_DRIED_FISH.get(), 5 * 60 * 20)
                .requirementCategoryMin(meat, 0.5F)
                .requirementCategoryMaxExclusive(meat, 1.0F)
                .requirementWithAnyCategory(fish)
                .requirementCategoryMax(fish, 0.5F)
                .requirementWithoutCategory(monster)
                .save(this.output, getSimpleRecipeName("drying", ModItems.SMALL_DRIED_FISH.get()));
        DryingRecipeBuilder.dryingByFoodValues(ModItems.DRIED_FISH.get(), 5 * 60 * 20)
                .requirementCategoryMin(meat, 0.5F)
                .requirementCategoryMaxExclusive(meat, 1.0F)
                .requirementCategoryMinExclusive(fish, 0.5F)
                .requirementCategoryMax(fish, 1.0F)
                .requirementWithoutCategory(monster)
                .save(this.output, getSimpleRecipeName("drying", ModItems.DRIED_FISH.get()));
        DryingRecipeBuilder.dryingByFoodValues(ModItems.LARGE_DRIED_FISH.get(), 10 * 60 * 20)
                .requirementCategoryMin(meat, 1.0F)
                .requirementCategoryMin(fish, 1.0F)
                .requirementWithoutCategory(monster)
                .save(this.output, getSimpleRecipeName("drying", ModItems.LARGE_DRIED_FISH.get()));
        DryingRecipeBuilder.drying(Ingredient.of(Items.KELP), Items.DRIED_KELP, 60 * 20)
                .save(this.output, getSimpleRecipeName("drying", Items.DRIED_KELP));

        var vanillaSeedsRecipes = Map.of(
                Items.WHEAT, Items.WHEAT_SEEDS,
                Items.BEETROOT, Items.BEETROOT_SEEDS
        );
        vanillaSeedsRecipes.forEach((input, output) -> ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 1, 3).save(this.output, getSimpleRecipeName("parrot_feeding", output)));
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(Items.PUMPKIN), Items.PUMPKIN_SEEDS, 4, 6).save(this.output, getSimpleRecipeName("parrot_feeding", Items.PUMPKIN_SEEDS));
        ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(Items.MELON_SLICE), Items.MELON_SEEDS, 1, 2).save(this.output, getSimpleRecipeName("parrot_feeding", Items.MELON_SEEDS));
        var snifferSeedsRecipes = Map.of(
                Items.TORCHFLOWER, Items.TORCHFLOWER_SEEDS,
                Items.PITCHER_PLANT, Items.PITCHER_POD
        );
        snifferSeedsRecipes.forEach((input, output) -> ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 1, 2).save(this.output, getSimpleRecipeName("parrot_feeding", output)));

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
            this.shapeless(RecipeCategory.MISC, output)
                    .requires(Ingredient.of(input))
                    .unlockedBy(getHasName(input), this.has(input))
                    .save(this.output, getSimpleRecipeName("crafting", output));
            ParrotFeedingRecipeBuilder.parrotFeeding(Ingredient.of(input), output, 2, 4)
                    .save(this.output, getSimpleRecipeName("parrot_feeding", output));
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
                .save(this.output, getSimpleRecipeName("piglin_bartering", ModItems.NETHEROSIA.get()));

        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.ASPARAGUS_SOUP.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_ASPARAGUS))
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 2.5F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.ASPARAGUS_SOUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.AVAJ.get(), 30, 10 * 20, 1)
                .requirementCombinationOr(
                        new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 4),
                        new RequirementCombinationAnd(
                                new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 3),
                                new RequirementCombinationOr(
                                        new RequirementCategoryMinExclusive(foodCategory(FoodCategories.DAIRY), 0.0F),
                                        new RequirementCategoryMinExclusive(foodCategory(FoodCategories.SWEETENER), 0.0F)
                                )
                        )
                ).save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.AVAJ.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BACON_EGGS.get(), 10, 30 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.EGG), 2.0F)
                .requirementCategoryMinExclusive(foodCategory(FoodCategories.MEAT), 1.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.VEGGIE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.BACON_EGGS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BONE_SOUP.get(), 30, 30 * 20, 1)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_BONE), 2)
                .requirementMustContainIngredientLessThan(getIngredientFromTags(ModItemTags.INGREDIENTS_BONE), 2)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_ONION))
                .requirementCategoryMax(foodCategory(FoodCategories.INEDIBLE), 2.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.BONE_SOUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BONE_STEW.get(), 0, 15 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.MEAT), 3.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.BONE_STEW.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BREAKFAST_SKILLET.get(), 1, 20 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.EGG), 1.0F)
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 1.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.DAIRY))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.BREAKFAST_SKILLET.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.BUNNY_STEW.get(), 1, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_RABBIT))
                .requirementCategoryMin(foodCategory(FoodCategories.FROZEN), 2.0F)
                .requirementCategoryMax(foodCategory(FoodCategories.MEAT), 0.5F)
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.BUNNY_STEW.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.CALIFORNIA_ROLL.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_KELP), 2)
                .requirementCategoryMin(foodCategory(FoodCategories.FISH), 1.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.CALIFORNIA_ROLL.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.CANDY.get(), 15, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_SYRUP))
                .requirementCategoryMin(foodCategory(FoodCategories.SWEETENER), 2.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.VEGGIE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.CANDY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.CEVICHE.get(), 20, 10 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.FISH), 2.0F)
                .requirementWithAnyCategory(foodCategory(FoodCategories.FROZEN))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .requirementWithoutCategory(foodCategory(FoodCategories.EGG))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.CEVICHE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FISH_STICKS.get(), 10, 30 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.FISH))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_STICK))
                .requirementCategoryMax(foodCategory(FoodCategories.INEDIBLE), 1.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.FISH_STICKS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FISH_TACOS.get(), 10, 10 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.FISH))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_CORN))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.FISH_TACOS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FLOWER_SALAD.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(Items.CHORUS_FLOWER))
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 2.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .requirementWithoutCategory(foodCategory(FoodCategories.EGG))
                .requirementWithoutCategory(foodCategory(FoodCategories.SWEETENER))
                .requirementWithoutCategory(foodCategory(FoodCategories.FRUIT))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.FLOWER_SALAD.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FROGGLE_BUNWICH.get(), 1, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_FROG))
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 0.5F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.FROGGLE_BUNWICH.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.FRUIT_MEDLEY.get(), 0, 10 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.FRUIT), 3.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.VEGGIE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.FRUIT_MEDLEY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.GAZPACHO.get(), 30, 10 * 20, 1)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_ASPARAGUS), 2)
                .requirementCategoryMin(foodCategory(FoodCategories.FROZEN), 2.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.GAZPACHO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.GLOW_BERRY_MOUSSE.get(), 30, 20 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.GLOW_BERRIES), 2)
                .requirementCategoryMin(foodCategory(FoodCategories.FRUIT), 2.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.GLOW_BERRY_MOUSSE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.GUMMY_CAKE.get(), 1, 30 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.SWEETENER))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_SLIME))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.GUMMY_CAKE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.HONEY_HAM.get(), 2, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_HONEY))
                .requirementCategoryMin(foodCategory(FoodCategories.MEAT), 1.5F)
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.HONEY_HAM.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.HONEY_NUGGETS.get(), 2, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_HONEY))
                .requirementWithAnyCategory(foodCategory(FoodCategories.MEAT))
                .requirementCategoryMaxExclusive(foodCategory(FoodCategories.MEAT), 1.5F)
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.HONEY_NUGGETS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.HOT_CHILI.get(), 10, 10 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.MEAT), 1.5F)
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 1.5F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.HOT_CHILI.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.HOT_COCOA.get(), 30, 10 * 20, 0)
                .requirementCombinationOr(
                        new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 4),
                        new RequirementCombinationAnd(
                                new RequirementMustContainIngredient(Ingredient.of(Items.COCOA_BEANS), 3),
                                new RequirementCombinationOr(
                                        new RequirementCategoryMinExclusive(foodCategory(FoodCategories.DAIRY), 0.0F),
                                        new RequirementCategoryMinExclusive(foodCategory(FoodCategories.SWEETENER), 0.0F)
                                )
                        )
                )
                .weight(199)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.HOT_COCOA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.ICE_CREAM.get(), 10, 10 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.FROZEN))
                .requirementWithAnyCategory(foodCategory(FoodCategories.DAIRY))
                .requirementWithAnyCategory(foodCategory(FoodCategories.SWEETENER))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.VEGGIE))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .requirementWithoutCategory(foodCategory(FoodCategories.EGG))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.ICE_CREAM.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.ICED_TEA.get(), 30, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_TEA), 2)
                .requirementWithAnyCategory(foodCategory(FoodCategories.SWEETENER))
                .requirementWithAnyCategory(foodCategory(FoodCategories.FROZEN))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.ICED_TEA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.JAMMY_PRESERVES.get(), 0, 10 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.FRUIT))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.VEGGIE))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.JAMMY_PRESERVES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.KABOBS.get(), 5, 30 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.MEAT))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_STICK))
                .requirementCategoryMax(foodCategory(FoodCategories.MONSTER), 1.0F)
                .requirementCategoryMax(foodCategory(FoodCategories.INEDIBLE), 1.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.KABOBS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MASHED_POTATOES.get(), 20, 20 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_POTATO), 2)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_GARLIC))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.MASHED_POTATOES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MEAT_BALLS.get(), -1, 15 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.MEAT_BALLS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MILKMADE_HAT.get(), 55, 30 * 20, 0)
                .requirementMustContainIngredient(Ingredient.of(ModItems.HOGLIN_NOSE.get()))
                .requirementMustContainIngredient(Ingredient.of(Items.BAMBOO))
                .requirementCategoryMin(foodCategory(FoodCategories.DAIRY), 1.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.MILKMADE_HAT.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MONSTER_LASAGNA.get(), 10, 10 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.MONSTER), 2.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.MONSTER_LASAGNA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MONSTER_TARTARE.get(), 30, 20 * 30, 1)
                .requirementCategoryMin(foodCategory(FoodCategories.MONSTER), 2.0F)
                .requirementCategoryMin(foodCategory(FoodCategories.EGG), 1.0F)
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 0.5F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.MONSTER_TARTARE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MOQUECA.get(), 40, 20 * 30, 1)
                .requirementWithAnyCategory(foodCategory(FoodCategories.FISH))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_ONION))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_TOMATO))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.MOQUECA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.MUSHY_CAKE.get(), 55, 20 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(Items.BROWN_MUSHROOM))
                .requirementMustContainIngredient(Ingredient.of(Items.RED_MUSHROOM))
                .requirementMustContainIngredient(Ingredient.of(Items.CRIMSON_FUNGUS))
                .requirementMustContainIngredient(Ingredient.of(Items.WARPED_FUNGUS))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.MUSHY_CAKE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.NETHEROSIA.get(), 100, 80 * 20, 1)
                .requirementMustContainIngredient(Ingredient.of(ModItems.COLLECTED_DUST.get()))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.NETHEROSIA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.PEPPER_POPPER.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_RED_PEPPER))
                .requirementWithAnyCategory(foodCategory(FoodCategories.MEAT))
                .requirementCategoryMax(foodCategory(FoodCategories.MEAT), 1.5F)
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.PEPPER_POPPER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.PEROGIES.get(), 5, 20 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.EGG))
                .requirementWithAnyCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithAnyCategory(foodCategory(FoodCategories.VEGGIE))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.PEROGIES.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.PLAIN_OMELETTE.get(), 1, 20 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.EGG), 3.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.PLAIN_OMELETTE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.POTATO_SOUFFLE.get(), 30, 20 * 20, 1)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_POTATO), 2)
                .requirementWithAnyCategory(foodCategory(FoodCategories.EGG))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.POTATO_SOUFFLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.POTATO_TORNADO.get(), 10, 15 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_POTATO))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_STICK))
                .requirementCategoryMax(foodCategory(FoodCategories.MONSTER), 1.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementCategoryMax(foodCategory(FoodCategories.INEDIBLE), 2.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.POTATO_TORNADO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.POW_CAKE.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_STICK))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_HONEY))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_CORN))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.POW_CAKE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.PUMPKIN_COOKIE.get(), 10, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_PUMPKIN))
                .requirementCategoryMin(foodCategory(FoodCategories.SWEETENER), 2.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.PUMPKIN_COOKIE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.RATATOUILLE.get(), 0, 20 * 20, 0)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithAnyCategory(foodCategory(FoodCategories.VEGGIE))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.RATATOUILLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SALMON_SUSHI.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_SALMON))
                .requirementMustContainIngredient(Ingredient.of(Items.DRIED_KELP))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.SALMON_SUSHI.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SALSA.get(), 20, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_TOMATO))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_ONION))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .requirementWithoutCategory(foodCategory(FoodCategories.EGG))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.SALSA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SCOTCH_EGG.get(), 10, 20 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_LARGE_EGG))
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 1.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.SCOTCH_EGG.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SEAFOOD_GUMBO.get(), 10, 20 * 20, 0)
                .requirementCategoryMinExclusive(foodCategory(FoodCategories.FISH), 2.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.SEAFOOD_GUMBO.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SNAKE_BONE_SOUP.get(), 2, 20 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_BONE), 2)
                .requirementCategoryMin(foodCategory(FoodCategories.MEAT), 2.0F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.SNAKE_BONE_SOUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.STEAMED_HAM_SANDWICH.get(), 5, 30 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 2.0F)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_HAM))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_SANDWICH_LEAF))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.STEAMED_HAM_SANDWICH.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.STEAMED_STICKS.get(), -5, 10 * 20, 0)
                .requirementWithAnyCategory(foodCategory(FoodCategories.INEDIBLE))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.FISH))
                .requirementWithoutCategory(foodCategory(FoodCategories.EGG))
                .requirementWithoutCategory(foodCategory(FoodCategories.MONSTER))
                .requirementWithoutCategory(foodCategory(FoodCategories.DAIRY))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.STEAMED_STICKS.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.STUFFED_EGGPLANT.get(), 1, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_EGGPLANT))
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 1.5F)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.STUFFED_EGGPLANT.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SURF_N_TURF.get(), 30, 10 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.MEAT), 2.5F)
                .requirementCategoryMin(foodCategory(FoodCategories.FISH), 1.5F)
                .requirementWithoutCategory(foodCategory(FoodCategories.FROZEN))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.SURF_N_TURF.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.SYRUP.get(), 40, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_SUGARY), 4)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.SYRUP.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.TAFFY.get(), 10, 20 * 20, 0)
                .requirementCategoryMin(foodCategory(FoodCategories.SWEETENER), 3.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.TAFFY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.TEA.get(), 25, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_TEA), 2)
                .requirementWithAnyCategory(foodCategory(FoodCategories.SWEETENER))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementCategoryMax(foodCategory(FoodCategories.VEGGIE), 1.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.TEA.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.TROPICAL_BOUILLABAISSE.get(), 35, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_TROPICAL_FISH), 2)
                .requirementCategoryMin(foodCategory(FoodCategories.FISH), 2.5F)
                .requirementWithAnyCategory(foodCategory(FoodCategories.VEGGIE))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.TROPICAL_BOUILLABAISSE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.TURKEY_DINNER.get(), 10, 30 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_DRUMSTICK), 2)
                .requirementCategoryMinExclusive(foodCategory(FoodCategories.MEAT), 1.0F)
                .requirementCombinationOr(new RequirementCategoryMinExclusive(foodCategory(FoodCategories.VEGGIE), 0.0F), new RequirementCategoryMinExclusive(foodCategory(FoodCategories.FRUIT), 0.0F))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.TURKEY_DINNER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.VEG_STINGER.get(), 15, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_STINGER), 2)
                .requirementCategoryMin(foodCategory(FoodCategories.VEGGIE), 2.5F)
                .requirementWithAnyCategory(foodCategory(FoodCategories.FROZEN))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.INEDIBLE))
                .requirementWithoutCategory(foodCategory(FoodCategories.EGG))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.VEG_STINGER.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.VOLT_GOAT_JELLY.get(), 40, 30 * 20, 1)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_VOLT))
                .requirementCategoryMin(foodCategory(FoodCategories.SWEETENER), 2.0F)
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.VOLT_GOAT_JELLY.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.WATERMELON_ICLE.get(), 10, 10 * 20, 0)
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_WATERMELON))
                .requirementWithAnyCategory(foodCategory(FoodCategories.FROZEN))
                .requirementMustContainIngredient(getIngredientFromTags(ModItemTags.INGREDIENTS_STICK))
                .requirementWithoutCategory(foodCategory(FoodCategories.MEAT))
                .requirementWithoutCategory(foodCategory(FoodCategories.VEGGIE))
                .requirementWithoutCategory(foodCategory(FoodCategories.EGG))
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.WATERMELON_ICLE.get()));
        CrockPotCookingRecipeBuilder.crockPotCooking(ModItems.WET_GOOP.get(), -10, 10 * 20, 0)
                .save(this.output, getSimpleRecipeName("crock_pot_cooking", ModItems.WET_GOOP.get()));
    }

    protected void smeltingRecipe(ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(pIngredient), category, CookingBookCategory.FOOD, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), this.has(pIngredient)).save(this.output, getSimpleRecipeName("smelting", pResult));
    }

    protected void smokingRecipe(ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(pIngredient), category, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), this.has(pIngredient)).save(this.output, getSimpleRecipeName("smoking", pResult));
    }

    protected void campfireCookingRecipe(ItemLike pIngredient, RecipeCategory category, ItemLike pResult, float pExperience, int pCookingTime) {
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(pIngredient), category, pResult, pExperience, pCookingTime).unlockedBy(getHasName(pIngredient), this.has(pIngredient)).save(this.output, getSimpleRecipeName("campfire_cooking", pResult));
    }

    @SafeVarargs
    protected final Ingredient getIngredientFromTags(TagKey<Item>... pTags) {
        return CompoundIngredient.of(Arrays.stream(pTags).map(this::tag).toArray(Ingredient[]::new));
    }

    protected Holder.Reference<FoodCategory> foodCategory(ResourceKey<FoodCategory> category) {
        return this.registries.lookupOrThrow(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY).getOrThrow(category);
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

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> registries) {
            super(packOutput, registries);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider registries, RecipeOutput output) {
            return new ModRecipeProvider(registries, output);
        }

        @Override
        public String getName() {
            return "CrockPot Recipes";
        }
    }
}
