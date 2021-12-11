package com.sihenzhang.crockpot.integration.kubejs;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.recipe.RangedItem;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.MapJS;

import javax.annotation.Nullable;

public abstract class AbstractCrockPotRecipeJS extends RecipeJS {
    public RangedItem parseRangedItem(@Nullable Object o) {
        if (o instanceof JsonElement) {
            return RangedItem.fromJson((JsonElement) o);
        }
        if (o instanceof CharSequence) {
            JsonObject json = new JsonObject();
            json.addProperty("item", o.toString());
            return RangedItem.fromJson(json);
        }
        JsonObject json = MapJS.of(o).toJson();
        return RangedItem.fromJson(json);
    }

    public FoodValues parseFoodValues(@Nullable Object o) {
        if (o instanceof JsonElement) {
            return FoodValues.fromJson((JsonElement) o);
        }
        JsonObject json = MapJS.of(o).toJson();
        return FoodValues.fromJson(json);
    }

    public IRequirement parseRequirement(@Nullable Object o) {
        if (o instanceof JsonElement) {
            return IRequirement.fromJson((JsonElement) o);
        }
        JsonObject json = MapJS.of(o).toJson();
        return IRequirement.fromJson(json);
    }
}
