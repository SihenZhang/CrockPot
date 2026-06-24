package com.sihenzhang.crockpot.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class ParrotFeedingRecipe extends AbstractRecipe<SingleRecipeInput> {
    private static final RandomSource RANDOM = RandomSource.create();
    public static final MapCodec<ParrotFeedingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ParrotFeedingRecipe::getIngredient),
            RangedItem.CODEC.fieldOf("result").forGetter(ParrotFeedingRecipe::getResult)
    ).apply(instance, ParrotFeedingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ParrotFeedingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            ParrotFeedingRecipe::getIngredient,
            RangedItem.STREAM_CODEC,
            ParrotFeedingRecipe::getResult,
            ParrotFeedingRecipe::new
    );

    private final Ingredient ingredient;
    private final RangedItem result;

    public ParrotFeedingRecipe(Ingredient ingredient, RangedItem result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(SingleRecipeInput pContainer, Level pLevel) {
        return ingredient.test(pContainer.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput pContainer) {
        return result.getInstance(RANDOM);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public RangedItem getResult() {
        return result;
    }

    @Override
    public RecipeSerializer<? extends ParrotFeedingRecipe> getSerializer() {
        return ModRecipes.PARROT_FEEDING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends ParrotFeedingRecipe> getType() {
        return ModRecipes.PARROT_FEEDING_RECIPE_TYPE.get();
    }
}
