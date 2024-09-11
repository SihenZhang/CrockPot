package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record RequirementCombinationOr(IRequirement first, IRequirement second) implements IRequirement {
    public static final MapCodec<RequirementCombinationOr> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    IRequirement.CODEC.fieldOf("first").forGetter(RequirementCombinationOr::first),
                    IRequirement.CODEC.fieldOf("second").forGetter(RequirementCombinationOr::second)
            ).apply(instance, RequirementCombinationOr::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCombinationOr> STREAM_CODEC = StreamCodec.composite(
            IRequirement.STREAM_CODEC,
            RequirementCombinationOr::first,
            IRequirement.STREAM_CODEC,
            RequirementCombinationOr::second,
            RequirementCombinationOr::new
    );

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return first.test(recipeWrapper) || second.test(recipeWrapper);
    }

    @Override
    public RequirementType type() {
        return RequirementType.COMBINATION_OR;
    }
}
