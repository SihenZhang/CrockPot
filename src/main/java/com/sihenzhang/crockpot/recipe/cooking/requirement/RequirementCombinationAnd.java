package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class RequirementCombinationAnd implements IRequirement {
    public static final MapCodec<RequirementCombinationAnd> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.lazyInitialized(() -> IRequirement.CODEC).fieldOf("first").forGetter(RequirementCombinationAnd::getFirst),
            Codec.lazyInitialized(() -> IRequirement.CODEC).fieldOf("second").forGetter(RequirementCombinationAnd::getSecond)
    ).apply(instance, RequirementCombinationAnd::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementCombinationAnd> STREAM_CODEC = StreamCodec.composite(
            IRequirement.STREAM_CODEC,
            RequirementCombinationAnd::getFirst,
            IRequirement.STREAM_CODEC,
            RequirementCombinationAnd::getSecond,
            RequirementCombinationAnd::new
    );

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
    public RequirementType getType() {
        return RequirementType.COMBINATION_AND;
    }

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return first.test(recipeWrapper) && second.test(recipeWrapper);
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }
}
