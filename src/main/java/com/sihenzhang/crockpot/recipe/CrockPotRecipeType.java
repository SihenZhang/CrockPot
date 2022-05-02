package com.sihenzhang.crockpot.recipe;

import com.sihenzhang.crockpot.CrockPot;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class CrockPotRecipeType<T extends Recipe<?>> implements RecipeType<T> {
    private final String name;

    public CrockPotRecipeType(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return CrockPot.MOD_ID + ":" + this.name;
    }
}
