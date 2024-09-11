package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

import java.util.function.Predicate;

public interface IRequirement extends Predicate<CrockPotCookingRecipe.Wrapper> {
    Codec<IRequirement> CODEC = RequirementType.CODEC.dispatch(IRequirement::type, RequirementType::codec);
    StreamCodec<RegistryFriendlyByteBuf, IRequirement> STREAM_CODEC = NeoForgeStreamCodecs.<RegistryFriendlyByteBuf, RequirementType>enumCodec(RequirementType.class).dispatch(IRequirement::type, RequirementType::streamCodec);

    RequirementType type();
}
