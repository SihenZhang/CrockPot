package com.sihenzhang.crockpot.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class AbstractRecipe implements IRecipe<RecipeWrapper> {
    protected final ResourceLocation id;

    protected AbstractRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    /* Used to check if a recipe matches current crafting inventory */
    public boolean matches(RecipeWrapper container, World level) {
        return true;
    }

    @Override
    /* Returns an Item that is the result of this recipe */
    public ItemStack assemble(RecipeWrapper container) {
        return ItemStack.EMPTY;
    }

    @Override
    /* Used to determine if this recipe can fit in a grid of the given width/height */
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    /* Get the result of this recipe, usually for display purposes (e.g. recipe book). If your recipe has more than one possible result (e.g. it's dynamic and depends on its inputs), then return an empty stack. */
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(RecipeWrapper container) {
        return NonNullList.create();
    }

    @Override
    /* If true, this recipe does not appear in the recipe book and does not respect recipe unlocking (and the doLimitedCrafting gamerule) */
    public boolean isSpecial() {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }
}
