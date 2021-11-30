package com.sihenzhang.crockpot.integration.kubejs;

import com.google.gson.JsonArray;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.util.JSONUtils;

import java.util.ArrayList;
import java.util.List;

public class CrockPotCookingRecipeJS extends AbstractCrockPotRecipeJS {
    public List<IRequirement> requirements = new ArrayList<>();

    @Override
    public void create(ListJS args) {
        this.outputItems.add(this.parseResultItem(args.get(0)));
        if (args.size() >= 5) {
            this.priority(((Number) args.get(1)).intValue());
            this.weight(((Number) args.get(2)).intValue());
            this.cookingTime(((Number) args.get(3)).intValue());
            this.potLevel(((Number) args.get(4)).intValue());
        } else {
            this.priority(((Number) args.get(1)).intValue());
            this.cookingTime(((Number) args.get(2)).intValue());
            this.potLevel(((Number) args.get(3)).intValue());
        }
    }

    @Override
    public void deserialize() {
        outputItems.add(this.parseResultItem(json.get("result")));
        JSONUtils.getAsJsonArray(json, "requirements").forEach(this::requirement);
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }
        if (serializeInputs) {
            JsonArray arr = new JsonArray();
            requirements.forEach(requirement -> arr.add(requirement.toJson()));
            json.add("requirements", arr);
        }
    }

    public CrockPotCookingRecipeJS requirement(Object requirement) {
        requirements.add(this.parseRequirement(requirement));
        return this;
    }

    public CrockPotCookingRecipeJS priority(int priority) {
        json.addProperty("priority", priority);
        return this;
    }

    public CrockPotCookingRecipeJS weight(int weight) {
        json.addProperty("weight", Math.max(weight, 1));
        return this;
    }

    public CrockPotCookingRecipeJS cookingTime(int cookingTime) {
        json.addProperty("cookingtime", cookingTime);
        return this;
    }

    public CrockPotCookingRecipeJS potLevel(int potLevel) {
        json.addProperty("potlevel", potLevel);
        return this;
    }
}
