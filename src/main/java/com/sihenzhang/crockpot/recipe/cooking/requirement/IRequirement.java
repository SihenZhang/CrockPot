package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;

import java.util.function.Predicate;

public interface IRequirement extends Predicate<CrockPotCookingRecipe.Wrapper> {
    Codec<IRequirement> CODEC = RequirementType.CODEC.dispatch("type", IRequirement::getType, RequirementType::codec);

    RequirementType getType();

    static IRequirement fromNetwork(RegistryFriendlyByteBuf buffer) {
        var type = buffer.readEnum(RequirementType.class);
        return switch (type) {
            case CATEGORY_MAX -> RequirementCategoryMax.fromNetwork(buffer);
            case CATEGORY_MAX_EXCLUSIVE -> RequirementCategoryMaxExclusive.fromNetwork(buffer);
            case CATEGORY_MIN -> RequirementCategoryMin.fromNetwork(buffer);
            case CATEGORY_MIN_EXCLUSIVE -> RequirementCategoryMinExclusive.fromNetwork(buffer);
            case MUST_CONTAIN_INGREDIENT -> RequirementMustContainIngredient.fromNetwork(buffer);
            case MUST_CONTAIN_INGREDIENT_LESS_THAN -> RequirementMustContainIngredientLessThan.fromNetwork(buffer);
            case COMBINATION_AND -> RequirementCombinationAnd.fromNetwork(buffer);
            case COMBINATION_OR -> RequirementCombinationOr.fromNetwork(buffer);
        };
    }

    void toNetwork(RegistryFriendlyByteBuf buffer);
}
