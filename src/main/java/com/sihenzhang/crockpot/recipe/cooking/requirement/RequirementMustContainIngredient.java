package com.sihenzhang.crockpot.recipe.cooking.requirement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.stream.IntStream;

public record RequirementMustContainIngredient(Ingredient ingredient, int quantity) implements IRequirement {
    public static final MapCodec<RequirementMustContainIngredient> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(RequirementMustContainIngredient::ingredient),
                    Codec.intRange(0, 4).fieldOf("quantity").forGetter(RequirementMustContainIngredient::quantity)
            ).apply(instance, RequirementMustContainIngredient::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementMustContainIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            RequirementMustContainIngredient::ingredient,
            ByteBufCodecs.VAR_INT,
            RequirementMustContainIngredient::quantity,
            RequirementMustContainIngredient::new
    );

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return IntStream.range(0, recipeWrapper.size()).mapToObj(recipeWrapper::getItem).filter(ingredient).count() >= quantity;
    }

    @Override
    public RequirementType type() {
        return RequirementType.MUST_CONTAIN_INGREDIENT;
    }
}
