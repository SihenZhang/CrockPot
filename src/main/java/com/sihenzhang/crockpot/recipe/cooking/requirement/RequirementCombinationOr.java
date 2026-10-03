package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;

public class RequirementCombinationOr implements IRequirement {
    public static final MapCodec<RequirementCombinationOr> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Codec.lazyInitialized(() -> IRequirement.CODEC).fieldOf("first").forGetter(RequirementCombinationOr::getFirst),
            Codec.lazyInitialized(() -> IRequirement.CODEC).fieldOf("second").forGetter(RequirementCombinationOr::getSecond)
    ).apply(instance, RequirementCombinationOr::new));

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
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return first.test(recipeWrapper) || second.test(recipeWrapper);
    }

    @Override
    public RequirementType getType() {
        return RequirementType.COMBINATION_OR;
    }

    public static RequirementCombinationOr fromNetwork(RegistryFriendlyByteBuf buffer) {
        return new RequirementCombinationOr(IRequirement.fromNetwork(buffer), IRequirement.fromNetwork(buffer));
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        buffer.writeEnum(RequirementType.COMBINATION_OR);
        first.toNetwork(buffer);
        second.toNetwork(buffer);
    }
}
