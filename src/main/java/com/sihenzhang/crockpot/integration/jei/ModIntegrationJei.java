package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.client.ClientRecipes;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.registry.FoodCategories;
import com.sihenzhang.crockpot.integration.jei.category.CrockPotCookingRecipeCategory;
import com.sihenzhang.crockpot.integration.jei.category.DryingRecipeCategory;
import com.sihenzhang.crockpot.integration.jei.category.ExplosionCraftingRecipeCategory;
import com.sihenzhang.crockpot.integration.jei.category.FoodValuesCategory;
import com.sihenzhang.crockpot.integration.jei.category.ParrotFeedingRecipeCategory;
import com.sihenzhang.crockpot.integration.jei.category.ParrotLayingEggsRecipeCategory;
import com.sihenzhang.crockpot.integration.jei.category.PiglinBarteringRecipeCategory;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredient;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredientHelper;
import com.sihenzhang.crockpot.integration.jei.ingredient.FoodCategoryIngredientRenderer;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.registry.ModRegistries;
import com.sihenzhang.crockpot.util.I18nUtil;
import com.sihenzhang.crockpot.util.IdUtil;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IModIngredientRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {
    public static final String MOD_ID = "jei";
    public static final Identifier ICONS = IdUtil.mod("textures/gui/jei/icons.png");

    @Override
    public Identifier getPluginUid() {
        return IdUtil.mod("crock_pot");
    }

    @Override
    public void registerIngredients(IModIngredientRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) {
            throw new IllegalStateException("Cannot register food category ingredients before the client level is available");
        }

        var foodCategories = level.registryAccess().lookupOrThrow(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY);
        var ingredients = foodCategories.listElements()
                .map(FoodCategoryIngredient::new)
                .toList();
        registration.register(
                FoodCategoryIngredient.TYPE,
                ingredients,
                new FoodCategoryIngredientHelper(),
                new FoodCategoryIngredientRenderer(),
                FoodCategoryIngredient.CODEC
        );
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        var guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CrockPotCookingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new DryingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new FoodValuesCategory(guiHelper));
        registration.addRecipeCategories(new ExplosionCraftingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ParrotLayingEggsRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ParrotFeedingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new PiglinBarteringRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        var level = Minecraft.getInstance().level;
        if (level == null) {
            throw new IllegalStateException("Cannot register JEI recipes before the client level is available");
        }

        FoodValuesDefinitionCache.regenerate(level);
        registration.addRecipes(
                RecipeTypes.CROCK_POT_COOKING,
                ClientRecipes.getRecipes(ModRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get()).stream()
                        .filter(holder -> !holder.value().getResultItem().is(ModItems.AVAJ.get()))
                .toList()
        );
        registration.addRecipes(
                RecipeTypes.DRYING,
                ClientRecipes.getRecipes(ModRecipes.DRYING_RECIPE_TYPE.get()).stream()
                        .filter(holder -> !DryingRecipeCategory.findMatchedInputs(holder.value()).isEmpty())
                        .toList()
        );
        var foodCategories = level.registryAccess().lookupOrThrow(ModRegistries.FOOD_CATEGORY_REGISTRY_KEY);
        registration.addRecipes(
                RecipeTypes.FOOD_VALUES,
                foodCategories.listElements()
                        .flatMap(category -> FoodValuesDefinitionCache.getMatchedItemsByValues(category).entrySet().stream()
                                .sorted(Map.Entry.comparingByKey())
                                .map(entry -> new FoodValuesCategory.FoodCategoryMatchedItems(category, entry.getKey(), entry.getValue()))
                        )
                        .toList()
        );
        registration.addRecipes(RecipeTypes.EXPLOSION_CRAFTING, ClientRecipes.getRecipes(ModRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get()));
        var meatCategory = level.registryAccess().getOrThrow(FoodCategories.MEAT);
        var monsterCategory = level.registryAccess().getOrThrow(FoodCategories.MONSTER);
        var meatsGroupByMonster = FoodValuesDefinitionCache.getMatchedItems(meatCategory).stream()
                .collect(Collectors.groupingBy(stack -> FoodValuesDefinition.getFoodValues(stack, level).has(monsterCategory)));
        var parrotLayingEggsRecipes = List.of(
                new ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper(meatsGroupByMonster.getOrDefault(false, List.of()), 1, 1),
                new ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper(meatsGroupByMonster.getOrDefault(true, List.of()), 0, 1)
        );
        registration.addRecipes(RecipeTypes.PARROT_LAYING_EGGS, parrotLayingEggsRecipes);
        registration.addRecipes(RecipeTypes.PARROT_FEEDING, ClientRecipes.getRecipes(ModRecipes.PARROT_FEEDING_RECIPE_TYPE.get()));
        registration.addRecipes(RecipeTypes.PIGLIN_BARTERING, ClientRecipes.getRecipes(ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get()));

        registration.addItemStackInfo(ModItems.BIRDCAGE.get().getDefaultInstance(), I18nUtil.integration(MOD_ID, "information.birdcage"));
        registration.addItemStackInfo(ModItems.CROCK_POT_UPGRADE_SMITHING_TEMPLATE.get().getDefaultInstance(), I18nUtil.integration(MOD_ID, "information.pot_upgrade_smithing_template"));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addCraftingStation(RecipeTypes.CROCK_POT_COOKING, ModItems.CROCK_POT.get(), ModItems.PORTABLE_CROCK_POT.get());
        registration.addCraftingStation(RecipeTypes.DRYING, ModItems.DRYING_RACK.get());
        registration.addCraftingStation(RecipeTypes.PARROT_LAYING_EGGS, ModItems.BIRDCAGE.get());
        registration.addCraftingStation(RecipeTypes.PARROT_FEEDING, ModItems.BIRDCAGE.get());
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CrockPotScreen.class, 88, 29, 24, 11, RecipeTypes.CROCK_POT_COOKING);
    }
}
