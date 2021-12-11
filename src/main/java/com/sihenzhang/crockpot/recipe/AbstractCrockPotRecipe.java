package com.sihenzhang.crockpot.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public abstract class AbstractCrockPotRecipe implements Recipe<RecipeWrapper> {
    protected final ResourceLocation id;

    protected AbstractCrockPotRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    /* Used to check if a recipe matches current crafting inventory */
    public boolean matches(RecipeWrapper container, Level level) {
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
