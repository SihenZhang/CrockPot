package com.sihenzhang.crockpot.recipe.pot.requirement;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;

import java.util.Objects;

public class RequirementCombinationOr implements IRequirement {
    IRequirement first, second;

    public RequirementCombinationOr(IRequirement first, IRequirement second) {
        this.first = first;
        this.second = second;
    }

    public RequirementCombinationOr(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(CrockPotRecipeInput recipeInput) {
        return first.test(recipeInput) || second.test(recipeInput);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(RequirementConstants.TYPE, RequirementType.COMBINATION_OR.name().toLowerCase());
        nbt.put(RequirementConstants.FIRST, first.serializeNBT());
        nbt.put(RequirementConstants.SECOND, second.serializeNBT());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!RequirementType.COMBINATION_OR.name().equals(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            throw new IllegalArgumentException(RequirementConstants.REQUIREMENT_TYPE_NOT_MATCH);
        }
        this.first = RequirementUtil.deserialize(Objects.requireNonNull(nbt.get(RequirementConstants.FIRST)));
        this.second = RequirementUtil.deserialize(Objects.requireNonNull(nbt.get(RequirementConstants.SECOND)));
    }

    public static RequirementCombinationOr fromJson(JsonObject object) {
        IRequirement first = IRequirement.fromJson(JSONUtils.getAsJsonObject(object, "first"));
        IRequirement second = IRequirement.fromJson(JSONUtils.getAsJsonObject(object, "second"));
        return new RequirementCombinationOr(first, second);
    }

    public static RequirementCombinationOr fromNetwork(PacketBuffer buffer) {
        return new RequirementCombinationOr(IRequirement.fromNetwork(buffer), IRequirement.fromNetwork(buffer));
    }

    @Override
    public void toNetwork(PacketBuffer buffer) {
        buffer.writeEnum(RequirementType.COMBINATION_OR);
        this.first.toNetwork(buffer);
        this.second.toNetwork(buffer);
    }
}
