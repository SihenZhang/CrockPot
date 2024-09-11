package com.sihenzhang.crockpot.integration.jei;

import com.google.common.collect.Streams;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.tag.ModBlockTags;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    public static final String MOD_ID = "jei";
    public static final ResourceLocation ICONS = RLUtils.mod("textures/gui/jei/icons.png");

    @Override
    public ResourceLocation getPluginUid() {
        return RLUtils.mod("crock_pot");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CrockPotCookingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new FoodValuesCategory(guiHelper));
        registration.addRecipeCategories(new ExplosionCraftingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ParrotLayingEggsRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ParrotFeedingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new PiglinBarteringRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        var recipeManager = level.getRecipeManager();
        FoodValuesDefinitionCache.regenerate(level);
        registration.addRecipes(CrockPotCookingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ModRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).filter(r -> r.getResult().getItem() != ModItems.AVAJ.get()).toList());
        registration.addRecipes(FoodValuesCategory.RECIPE_TYPE, Arrays.stream(FoodCategory.values()).map(category -> new FoodValuesCategory.FoodCategoryMatchedItems(category, FoodValuesDefinitionCache.getMatchedItems(category))).toList());
        registration.addRecipes(ExplosionCraftingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ModRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        var meatsGroupByMonster = FoodValuesDefinition.getMatchedItems(FoodCategory.MEAT, level).stream()
                .collect(Collectors.groupingBy(item -> FoodValuesDefinition.getFoodValues(item, Minecraft.getInstance().level).has(FoodCategory.MONSTER)));
        var parrotLayingEggsRecipes = List.of(
                new ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper(Ingredient.of(meatsGroupByMonster.get(false).stream()), 1, 1),
                new ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper(Ingredient.of(meatsGroupByMonster.get(true).stream()), 0, 1)
        );
        registration.addRecipes(ParrotLayingEggsRecipeCategory.RECIPE_TYPE, parrotLayingEggsRecipes);
        registration.addRecipes(ParrotFeedingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ModRecipes.PARROT_FEEDING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(PiglinBarteringRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addItemStackInfo(ModItems.BIRDCAGE.get().getDefaultInstance(), I18nUtils.createIntegrationComponent("jei", "information.birdcage"));
        registration.addItemStackInfo(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get().getDefaultInstance(), I18nUtils.createIntegrationComponent("jei", "information.pot_upgrade_smithing_template"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        Streams.stream(BuiltInRegistries.BLOCK.getTagOrEmpty(ModBlockTags.CROCK_POTS))
                .map(Holder::value)
                .filter(CrockPotBlock.class::isInstance)
                .map(CrockPotBlock.class::cast)
                .map(block -> block.asItem().getDefaultInstance())
                .forEach(pot -> registration.addRecipeCatalyst(pot, CrockPotCookingRecipeCategory.RECIPE_TYPE));
        registration.addRecipeCatalyst(ModItems.BIRDCAGE.get().getDefaultInstance(), ParrotLayingEggsRecipeCategory.RECIPE_TYPE, ParrotFeedingRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CrockPotScreen.class, 80, 43, 24, 18, CrockPotCookingRecipeCategory.RECIPE_TYPE);
    }
}
