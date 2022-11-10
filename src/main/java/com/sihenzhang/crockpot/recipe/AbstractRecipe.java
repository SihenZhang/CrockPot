package com.sihenzhang.crockpot.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public abstract class AbstractRecipe implements Recipe<Container> {
    protected final ResourceLocation id;

    protected AbstractRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public boolean canCraftInDimensions(int pWidth, int pHeight) {
        return true;
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }
}
