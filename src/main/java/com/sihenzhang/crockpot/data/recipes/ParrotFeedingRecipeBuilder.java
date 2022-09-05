package com.sihenzhang.crockpot.data.recipes;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.recipe.RangedItem;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ParrotFeedingRecipeBuilder implements RecipeBuilder {
    private final Item result;
    private final int resultMinimumCount;
    private final int resultMaximumCount;
    private final Ingredient ingredient;

    public ParrotFeedingRecipeBuilder(ItemLike result, int resultMinimumCount, int resultMaximumCount, Ingredient ingredient) {
        this.result = result.asItem();
        this.resultMinimumCount = resultMinimumCount;
        this.resultMaximumCount = resultMaximumCount;
        this.ingredient = ingredient;
    }

    public static ParrotFeedingRecipeBuilder parrotFeeding(Ingredient ingredient, ItemLike result, int resultMinimumCount, int resultMaximumCount) {
        return new ParrotFeedingRecipeBuilder(result, resultMinimumCount, resultMaximumCount, ingredient);
    }

    public static ParrotFeedingRecipeBuilder parrotFeeding(Ingredient ingredient, ItemLike result, int resultCount) {
        return parrotFeeding(ingredient, result, resultCount, resultCount);
    }

    public static ParrotFeedingRecipeBuilder parrotFeeding(Ingredient ingredient, ItemLike result) {
        return parrotFeeding(ingredient, result, 1);
    }

    @Override
    public RecipeBuilder unlockedBy(String pCriterionName, CriterionTriggerInstance pCriterionTrigger) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String pGroupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, ingredient, result, resultMinimumCount, resultMaximumCount));
    }

    public static class Result implements FinishedRecipe {
        private final ResourceLocation id;
        private final Ingredient ingredient;
        private final RangedItem result;

        public Result(ResourceLocation id, Ingredient ingredient, Item result, int resultMinimumCount, int resultMaximumCount) {
            this.id = id;
            this.ingredient = ingredient;
            this.result = new RangedItem(result, resultMinimumCount, resultMaximumCount);
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            pJson.add("result", result.toJson());
        }

        @Override
        public ResourceLocation getId() {
            return id;
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CrockPotRegistry.PARROT_FEEDING_RECIPE_SERIALIZER.get();
        }

        @Nullable
        @Override
        public JsonObject serializeAdvancement() {
            return null;
        }

        @Nullable
        @Override
        public ResourceLocation getAdvancementId() {
            return null;
        }
    }
}
