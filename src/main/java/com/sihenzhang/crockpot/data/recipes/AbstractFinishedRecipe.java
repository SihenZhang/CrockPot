package com.sihenzhang.crockpot.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public abstract class AbstractFinishedRecipe implements FinishedRecipe {
    private final ResourceLocation id;

    public AbstractFinishedRecipe(ResourceLocation id) {
        this.id = id;
    }

    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Nullable
    @Override
    public JsonObject serializeAdvancement() {
        return null;
    }

    @Nullable
    @Override
    public ResourceLocation getAdvancementId() {
        return null;
    }
}
