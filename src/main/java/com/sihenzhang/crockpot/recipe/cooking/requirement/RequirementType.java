package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;

import java.util.Locale;

public enum RequirementType {
    CATEGORY_MAX,
    CATEGORY_MAX_EXCLUSIVE,
    CATEGORY_MIN,
    CATEGORY_MIN_EXCLUSIVE,
    MUST_CONTAIN_INGREDIENT,
    MUST_CONTAIN_INGREDIENT_LESS_THAN,
    COMBINATION_AND,
    COMBINATION_OR;

    public static final Codec<RequirementType> CODEC = Codec.STRING.comapFlatMap(name -> {
        try {
            return DataResult.success(valueOf(name.toUpperCase(Locale.ROOT)));
        } catch (IllegalArgumentException exception) {
            return DataResult.error(() -> "Unknown cooking requirement type: " + name);
        }
    }, RequirementType::name);

    public MapCodec<? extends IRequirement> codec() {
        return switch (this) {
            case CATEGORY_MAX -> RequirementCategoryMax.CODEC;
            case CATEGORY_MAX_EXCLUSIVE -> RequirementCategoryMaxExclusive.CODEC;
            case CATEGORY_MIN -> RequirementCategoryMin.CODEC;
            case CATEGORY_MIN_EXCLUSIVE -> RequirementCategoryMinExclusive.CODEC;
            case MUST_CONTAIN_INGREDIENT -> RequirementMustContainIngredient.CODEC;
            case MUST_CONTAIN_INGREDIENT_LESS_THAN -> RequirementMustContainIngredientLessThan.CODEC;
            case COMBINATION_AND -> RequirementCombinationAnd.CODEC;
            case COMBINATION_OR -> RequirementCombinationOr.CODEC;
        };
    }
}
