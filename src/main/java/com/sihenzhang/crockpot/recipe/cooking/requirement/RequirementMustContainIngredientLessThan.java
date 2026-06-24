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

public class RequirementMustContainIngredientLessThan implements IRequirement {
    public static final MapCodec<RequirementMustContainIngredientLessThan> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(RequirementMustContainIngredientLessThan::getIngredient),
            Codec.INT.fieldOf("quantity").forGetter(RequirementMustContainIngredientLessThan::getQuantity)
    ).apply(instance, RequirementMustContainIngredientLessThan::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementMustContainIngredientLessThan> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            RequirementMustContainIngredientLessThan::getIngredient,
            ByteBufCodecs.VAR_INT,
            RequirementMustContainIngredientLessThan::getQuantity,
            RequirementMustContainIngredientLessThan::new
    );

    private final Ingredient ingredient;
    private final int quantity;

    public RequirementMustContainIngredientLessThan(Ingredient ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN;
    }

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return IntStream.range(0, recipeWrapper.size()).mapToObj(recipeWrapper::getItem).filter(ingredient).count() <= quantity;
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }
}
