package com.sihenzhang.crockpot.data.recipes;

import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.RangedItem;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

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
        return this.addResult(result, 1, weight);
    }

    @Override
    public Item getResult() {
        return null;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id, new PiglinBarteringRecipe(ingredient, weightedResults.build()), null);
    }

    @Override
    public void save(RecipeOutput recipeOutput) {
        throw new UnsupportedOperationException("Piglin Bartering Recipe does not have a default recipe id");
    }

    @Override
    public void save(RecipeOutput recipeOutput, String id) {
        this.save(recipeOutput, ResourceLocation.parse(id));
    }
}
