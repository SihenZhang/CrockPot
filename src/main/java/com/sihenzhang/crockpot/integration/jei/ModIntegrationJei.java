package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredient;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredientHelper;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredientRenderer;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.registry.FoodCategories;
import com.sihenzhang.crockpot.registry.ModRegistries;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    public static final String MOD_ID = "jei";
    public static final ResourceLocation ICONS = RLUtils.createRL("textures/gui/jei/icons.png");

    @Override
    public ResourceLocation getPluginUid() {
        return RLUtils.createRL("crock_pot");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        var level = Minecraft.getInstance().level;
        var categories = level.registryAccess().registryOrThrow(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY)
                .holders().map(FoodCategoryIngredient::new).toList();
        registration.register(FoodCategoryIngredient.TYPE, categories,
                new FoodCategoryIngredientHelper(), new FoodCategoryIngredientRenderer(), FoodCategoryIngredient.CODEC);
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
        registration.addRecipes(CrockPotCookingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).filter(r -> r.getResult().getItem() != CrockPotItems.AVAJ.get()).toList());
        registration.addRecipes(
                FoodValuesCategory.RECIPE_TYPE,
                level.registryAccess().registryOrThrow(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY).holders()
                        .flatMap(category ->
                                FoodValuesDefinitionCache.getMatchedItemsByValues(category).entrySet().stream()
                                        .sorted(Map.Entry.comparingByKey())
                                        .map(entry ->
                                                new FoodValuesCategory.FoodCategoryMatchedItems(
                                                        category, entry.getKey(), entry.getValue()
                                                )
                                        )
                        )
                        .toList()
        );
        registration.addRecipes(ExplosionCraftingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        var meat = level.registryAccess().registryOrThrow(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY).getHolderOrThrow(FoodCategories.MEAT);
        var meatsGroupByMonster = FoodValuesDefinitionCache.getMatchedItems(meat).stream()
                .collect(Collectors.groupingBy(item -> FoodValuesDefinition.getFoodValues(item.getDefaultInstance(), level).has(FoodCategories.MONSTER)));
        var parrotLayingEggsRecipes = List.of(
                new ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper(Ingredient.of(meatsGroupByMonster.getOrDefault(false, List.of()).stream().map(Item::getDefaultInstance)), 1, 1),
                new ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper(Ingredient.of(meatsGroupByMonster.getOrDefault(true, List.of()).stream().map(Item::getDefaultInstance)), 0, 1)
        );
        registration.addRecipes(ParrotLayingEggsRecipeCategory.RECIPE_TYPE, parrotLayingEggsRecipes);
        registration.addRecipes(ParrotFeedingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRecipes.PARROT_FEEDING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());
        registration.addRecipes(PiglinBarteringRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).toList());

        registration.addItemStackInfo(CrockPotItems.BIRDCAGE.get().getDefaultInstance(), I18nUtils.createIntegrationComponent("jei", "information.birdcage"));
        registration.addItemStackInfo(CrockPotItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get().getDefaultInstance(), I18nUtils.createIntegrationComponent("jei", "information.pot_upgrade_smithing_template"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        BuiltInRegistries.BLOCK.getTag(CrockPotBlockTags.CROCK_POTS).stream().flatMap(net.minecraft.core.HolderSet::stream).map(net.minecraft.core.Holder::value)
                .filter(CrockPotBlock.class::isInstance)
                .map(CrockPotBlock.class::cast)
                .map(block -> block.asItem().getDefaultInstance())
                .forEach(pot -> registration.addRecipeCatalyst(pot, CrockPotCookingRecipeCategory.RECIPE_TYPE));
        registration.addRecipeCatalyst(CrockPotItems.BIRDCAGE.get().getDefaultInstance(), ParrotLayingEggsRecipeCategory.RECIPE_TYPE, ParrotFeedingRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CrockPotScreen.class, 88, 29, 24, 11, CrockPotCookingRecipeCategory.RECIPE_TYPE);
    }
}
