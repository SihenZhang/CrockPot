package com.sihenzhang.crockpot.data.recipes;

import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public class ExplosionCraftingRecipeBuilder extends AbstractRecipeBuilder {
    private final Item result;
    private final Ingredient ingredient;
    private final int count;
    private float lossRate;
    private boolean onlyBlock;

    public ExplosionCraftingRecipeBuilder(ItemLike result, int count, Ingredient ingredient) {
        this.result = result.asItem();
        this.count = count;
        this.ingredient = ingredient;
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, Ingredient ingredient) {
        return explosionCrafting(result, 1, ingredient);
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, int count, Ingredient ingredient) {
        return new ExplosionCraftingRecipeBuilder(result, count, ingredient);
    }

    public ExplosionCraftingRecipeBuilder lossRate(float lossRate) {
        this.lossRate = lossRate;
        return this;
    }

    public ExplosionCraftingRecipeBuilder onlyBlock() {
        this.onlyBlock = true;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput output, ResourceKey<Recipe<?>> id) {
        output.accept(id, new ExplosionCraftingRecipe(ingredient, new ItemStackTemplate(result, count), lossRate, onlyBlock), null);
    }
}
