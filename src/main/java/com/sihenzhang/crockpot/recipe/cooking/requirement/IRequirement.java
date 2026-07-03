package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

import java.util.function.Predicate;

public interface IRequirement extends Predicate<CrockPotCookingRecipe.Wrapper> {
    Codec<IRequirement> CODEC = Codec.STRING.dispatch("type", requirement -> requirement.getType().name(), IRequirement::codecForType);
    StreamCodec<RegistryFriendlyByteBuf, IRequirement> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public IRequirement decode(RegistryFriendlyByteBuf buffer) {
            return switch (buffer.readEnum(RequirementType.class)) {
                case CATEGORY_MAX -> RequirementCategoryMax.STREAM_CODEC.decode(buffer);
                case CATEGORY_MAX_EXCLUSIVE -> RequirementCategoryMaxExclusive.STREAM_CODEC.decode(buffer);
                case CATEGORY_MIN -> RequirementCategoryMin.STREAM_CODEC.decode(buffer);
                case CATEGORY_MIN_EXCLUSIVE -> RequirementCategoryMinExclusive.STREAM_CODEC.decode(buffer);
                case MUST_CONTAIN_INGREDIENT -> RequirementMustContainIngredient.STREAM_CODEC.decode(buffer);
                case MUST_CONTAIN_INGREDIENT_LESS_THAN ->
                        RequirementMustContainIngredientLessThan.STREAM_CODEC.decode(buffer);
                case COMBINATION_AND -> RequirementCombinationAnd.STREAM_CODEC.decode(buffer);
                case COMBINATION_OR -> RequirementCombinationOr.STREAM_CODEC.decode(buffer);
            };
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, IRequirement requirement) {
            buffer.writeEnum(requirement.getType());
            requirement.toNetwork(buffer);
        }
    };

    private static MapCodec<? extends IRequirement> codecForType(String typeName) {
        return RequirementType.valueOf(typeName).codec();
    }

    RequirementType getType();

    void toNetwork(RegistryFriendlyByteBuf buffer);
}
