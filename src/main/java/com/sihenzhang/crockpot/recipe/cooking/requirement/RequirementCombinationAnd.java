package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipeInput;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public class RequirementCombinationAnd implements IRequirement {
    private final IRequirement first;
    private final IRequirement second;

    public RequirementCombinationAnd(IRequirement first, IRequirement second) {
        this.first = first;
        this.second = second;
    }

    public IRequirement getFirst() {
        return first;
    }

    public IRequirement getSecond() {
        return second;
    }

    @Override
    public boolean test(CrockPotCookingRecipeInput recipeInput) {
        return first.test(recipeInput) && second.test(recipeInput);
    }

    public static RequirementCombinationAnd fromJson(JsonObject object) {
        var first = IRequirement.fromJson(GsonHelper.getAsJsonObject(object, "first"));
        var second = IRequirement.fromJson(GsonHelper.getAsJsonObject(object, "second"));
        return new RequirementCombinationAnd(first, second);
    }

    @Override
    public JsonElement toJson() {
        var obj = new JsonObject();
        obj.addProperty("type", RequirementType.COMBINATION_AND.name());
        obj.add("first", first.toJson());
        obj.add("second", second.toJson());
        return obj;
    }

    public static RequirementCombinationAnd fromNetwork(FriendlyByteBuf buffer) {
        return new RequirementCombinationAnd(IRequirement.fromNetwork(buffer), IRequirement.fromNetwork(buffer));
    }

    @Override
    public void toNetwork(FriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.COMBINATION_AND);
        first.toNetwork(buffer);
        second.toNetwork(buffer);
    }
}
