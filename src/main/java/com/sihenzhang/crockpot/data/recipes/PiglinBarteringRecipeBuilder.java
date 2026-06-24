package com.sihenzhang.crockpot.data.recipes;

import com.sihenzhang.crockpot.recipe.PiglinBarteringRecipe;
import com.sihenzhang.crockpot.recipe.RangedItem;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public class PiglinBarteringRecipeBuilder extends AbstractRecipeBuilder {
    private final Ingredient ingredient;
    private final WeightedList.Builder<RangedItem> results = WeightedList.builder();

    public PiglinBarteringRecipeBuilder(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public static PiglinBarteringRecipeBuilder piglinBartering(Ingredient ingredient) {
        return new PiglinBarteringRecipeBuilder(ingredient);
    }

    public PiglinBarteringRecipeBuilder addResult(ItemLike result, int weight) {
        return this.addResult(result, 1, 1, weight);
    }

    public PiglinBarteringRecipeBuilder addResult(ItemLike result, int min, int max, int weight) {
        this.results.add(new RangedItem(result.asItem(), min, max), weight);
        return this;
    }

    @Override
    public Item getResult() {
        return Items.AIR;
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        output.accept(id, new PiglinBarteringRecipe(ingredient, results.build()), null);
    }
}
