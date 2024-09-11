package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record RequirementCombinationAnd(IRequirement first, IRequirement second) implements IRequirement {
    public static final MapCodec<RequirementCombinationAnd> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    IRequirement.CODEC.fieldOf("first").forGetter(RequirementCombinationAnd::first),
                    IRequirement.CODEC.fieldOf("second").forGetter(RequirementCombinationAnd::second)
            ).apply(instance, RequirementCombinationAnd::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCombinationAnd> STREAM_CODEC = StreamCodec.composite(
            IRequirement.STREAM_CODEC,
            RequirementCombinationAnd::first,
            IRequirement.STREAM_CODEC,
            RequirementCombinationAnd::second,
            RequirementCombinationAnd::new
    );

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return first.test(recipeWrapper) && second.test(recipeWrapper);
    }

    @Override
    public RequirementType type() {
        return RequirementType.COMBINATION_AND;
    }
}
