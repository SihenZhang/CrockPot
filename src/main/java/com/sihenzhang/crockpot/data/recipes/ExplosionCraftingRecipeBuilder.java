package com.sihenzhang.crockpot.data.recipes;

import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class ExplosionCraftingRecipeBuilder extends AbstractRecipeBuilder {
    private final Item result;
    private final int resultCount;
    private final Ingredient ingredient;
    private float lossRate = 0.0F;
    private boolean onlyBlock = false;

    public ExplosionCraftingRecipeBuilder(ItemLike result, int resultCount, Ingredient ingredient) {
        this.result = result.asItem();
        this.resultCount = resultCount;
        this.ingredient = ingredient;
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, int resultCount, Ingredient ingredient) {
        return new ExplosionCraftingRecipeBuilder(result, resultCount, ingredient);
    }

    public static ExplosionCraftingRecipeBuilder explosionCrafting(ItemLike result, Ingredient ingredient) {
        return explosionCrafting(result, 1, ingredient);
    }

    public ExplosionCraftingRecipeBuilder lossRate(float lossRate) {
        this.lossRate = Mth.clamp(lossRate, 0.0F, 1.0F);
        return this;
    }

    public ExplosionCraftingRecipeBuilder onlyBlock() {
        onlyBlock = true;
        return this;
    }

    @Override
    public Item getResult() {
        return result;
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        recipeOutput.accept(id, new ExplosionCraftingRecipe(ingredient, new ItemStack(result, resultCount), lossRate, onlyBlock), null);
    }
}
