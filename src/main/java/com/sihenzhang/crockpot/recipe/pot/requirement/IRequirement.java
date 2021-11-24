package com.sihenzhang.crockpot.recipe.pot.requirement;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.pot.CrockPotRecipeInput;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Predicate;

public interface IRequirement extends INBTSerializable<CompoundNBT>, Predicate<CrockPotRecipeInput> {
    void toNetwork(PacketBuffer buffer);

    static IRequirement fromJson(JsonObject object) {
        RequirementType type = JsonUtils.getAsEnum(object, "type", RequirementType.class);
        switch (type) {
            case CATEGORY_MAX:
                return RequirementCategoryMax.fromJson(object);
            case CATEGORY_MAX_EXCLUSIVE:
                return RequirementCategoryMaxExclusive.fromJson(object);
            case CATEGORY_MIN:
                return RequirementCategoryMin.fromJson(object);
            case CATEGORY_MIN_EXCLUSIVE:
                return RequirementCategoryMinExclusive.fromJson(object);
            case MUST_CONTAIN_INGREDIENT:
                return RequirementMustContainIngredient.fromJson(object);
            case MUST_CONTAIN_INGREDIENT_LESS_THAN:
                return RequirementMustContainIngredientLessThan.fromJson(object);
            case COMBINATION_AND:
                return RequirementCombinationAnd.fromJson(object);
            case COMBINATION_OR:
                return RequirementCombinationOr.fromJson(object);
            default:
                throw new IllegalArgumentException("No valid requirement type was found");
        }
    }

    static IRequirement fromNetwork(PacketBuffer buffer) {
        RequirementType type = buffer.readEnum(RequirementType.class);
        switch (type) {
            case CATEGORY_MAX:
                return RequirementCategoryMax.fromNetwork(buffer);
            case CATEGORY_MAX_EXCLUSIVE:
                return RequirementCategoryMaxExclusive.fromNetwork(buffer);
            case CATEGORY_MIN:
                return RequirementCategoryMin.fromNetwork(buffer);
            case CATEGORY_MIN_EXCLUSIVE:
                return RequirementCategoryMinExclusive.fromNetwork(buffer);
            case MUST_CONTAIN_INGREDIENT:
                return RequirementMustContainIngredient.fromNetwork(buffer);
            case MUST_CONTAIN_INGREDIENT_LESS_THAN:
                return RequirementMustContainIngredientLessThan.fromNetwork(buffer);
            case COMBINATION_AND:
                return RequirementCombinationAnd.fromNetwork(buffer);
            case COMBINATION_OR:
                return RequirementCombinationOr.fromNetwork(buffer);
            default:
                throw new IllegalArgumentException("No valid requirement type was found");
        }
    }
}
