package com.sihenzhang.crockpot.data.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

public abstract class AbstractRecipeBuilder {
    protected static ResourceKey<Recipe<?>> recipeKey(String id) {
        return ResourceKey.create(Registries.RECIPE, Identifier.parse(id));
    }

    public abstract ItemLike getResult();

    public void save(RecipeOutput output, String id) {
        this.save(output, recipeKey(id));
    }

    public abstract void save(RecipeOutput output, ResourceKey<Recipe<?>> id);
}
