package com.sihenzhang.crockpot.data.recipes;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.recipe.RangedItem;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class PiglinBarteringRecipeBuilder extends AbstractRecipeBuilder {
    private final SimpleWeightedRandomList.Builder<RangedItem> weightedResults = SimpleWeightedRandomList.builder();
    private final Ingredient ingredient;

    public PiglinBarteringRecipeBuilder(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public static PiglinBarteringRecipeBuilder piglinBartering(Ingredient ingredient) {
        return new PiglinBarteringRecipeBuilder(ingredient);
    }

    public PiglinBarteringRecipeBuilder addResult(ItemLike result, int min, int max, int weight) {
        weightedResults.add(new RangedItem(result.asItem(), min, max), weight);
        return this;
    }

    public PiglinBarteringRecipeBuilder addResult(ItemLike result, int count, int weight) {
        weightedResults.add(new RangedItem(result.asItem(), count), weight);
        return this;
    }

    public PiglinBarteringRecipeBuilder addResult(ItemLike result, int weight) {
        weightedResults.add(new RangedItem(result.asItem(), 1), weight);
        return this;
    }

    @Override
    public Item getResult() {
        return null;
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, ResourceLocation pRecipeId) {
        pFinishedRecipeConsumer.accept(new Result(pRecipeId, ingredient, weightedResults.build()));
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        throw new UnsupportedOperationException("Piglin Bartering Recipe does not have a default recipe id");
    }

    @Override
    public void save(Consumer<FinishedRecipe> pFinishedRecipeConsumer, String pRecipeId) {
        this.save(pFinishedRecipeConsumer, new ResourceLocation(pRecipeId));
    }

    public static class Result extends AbstractFinishedRecipe {
        private final Ingredient ingredient;
        private final SimpleWeightedRandomList<RangedItem> weightedResults;

        public Result(ResourceLocation id, Ingredient ingredient, SimpleWeightedRandomList<RangedItem> weightedResults) {
            super(id);
            this.ingredient = ingredient;
            this.weightedResults = weightedResults;
        }

        @Override
        public void serializeRecipeData(JsonObject pJson) {
            pJson.add("ingredient", ingredient.toJson());
            var results = new JsonArray();
            weightedResults.unwrap().forEach(result -> {
                var rangedItemJson = result.getData().toJson();
                rangedItemJson.getAsJsonObject().addProperty("weight", result.getWeight().asInt());
                results.add(rangedItemJson);
            });
            pJson.add("results", results);
        }

        @Override
        public RecipeSerializer<?> getType() {
            return CrockPotRegistry.PIGLIN_BARTERING_RECIPE_SERIALIZER.get();
        }
    }
}
