package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class RequirementCombinationOr implements IRequirement {
    public static final MapCodec<RequirementCombinationOr> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.lazyInitialized(() -> IRequirement.CODEC).fieldOf("first").forGetter(RequirementCombinationOr::getFirst),
            Codec.lazyInitialized(() -> IRequirement.CODEC).fieldOf("second").forGetter(RequirementCombinationOr::getSecond)
    ).apply(instance, RequirementCombinationOr::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCombinationOr> STREAM_CODEC = StreamCodec.composite(
            IRequirement.STREAM_CODEC,
            RequirementCombinationOr::getFirst,
            IRequirement.STREAM_CODEC,
            RequirementCombinationOr::getSecond,
            RequirementCombinationOr::new
    );

    private final IRequirement first;
    private final IRequirement second;

    public RequirementCombinationOr(IRequirement first, IRequirement second) {
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
    public RequirementType getType() {
        return RequirementType.COMBINATION_OR;
    }

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return first.test(recipeWrapper) || second.test(recipeWrapper);
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }
}
