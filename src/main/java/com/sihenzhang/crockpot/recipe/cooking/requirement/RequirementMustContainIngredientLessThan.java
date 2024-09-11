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

public record RequirementMustContainIngredientLessThan(Ingredient ingredient, int quantity) implements IRequirement {
    public static final MapCodec<RequirementMustContainIngredientLessThan> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(RequirementMustContainIngredientLessThan::ingredient),
                    Codec.intRange(0, 4).fieldOf("quantity").forGetter(RequirementMustContainIngredientLessThan::quantity)
            ).apply(instance, RequirementMustContainIngredientLessThan::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementMustContainIngredientLessThan> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            RequirementMustContainIngredientLessThan::ingredient,
            ByteBufCodecs.VAR_INT,
            RequirementMustContainIngredientLessThan::quantity,
            RequirementMustContainIngredientLessThan::new
    );

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return IntStream.range(0, recipeWrapper.size()).mapToObj(recipeWrapper::getItem).filter(ingredient).count() <= quantity;
    }

    @Override
    public RequirementType type() {
        return RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN;
    }
}
