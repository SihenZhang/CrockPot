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

public class RequirementMustContainIngredient implements IRequirement {
    public static final MapCodec<RequirementMustContainIngredient> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(RequirementMustContainIngredient::getIngredient),
            Codec.INT.fieldOf("quantity").forGetter(RequirementMustContainIngredient::getQuantity)
    ).apply(instance, RequirementMustContainIngredient::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequirementMustContainIngredient> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            RequirementMustContainIngredient::getIngredient,
            ByteBufCodecs.VAR_INT,
            RequirementMustContainIngredient::getQuantity,
            RequirementMustContainIngredient::new
    );

    private final Ingredient ingredient;
    private final int quantity;

    public RequirementMustContainIngredient(Ingredient ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = Math.min(quantity, 4);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public RequirementType getType() {
        return RequirementType.MUST_CONTAIN_INGREDIENT;
    }

    @Override
    public boolean test(CrockPotCookingRecipe.Wrapper recipeWrapper) {
        return IntStream.range(0, recipeWrapper.size()).mapToObj(recipeWrapper::getItem).filter(ingredient).count() >= quantity;
    }

    @Override
    public void toNetwork(RegistryFriendlyByteBuf buffer) {
        STREAM_CODEC.encode(buffer, this);
    }
}
