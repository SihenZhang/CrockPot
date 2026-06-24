package com.sihenzhang.crockpot.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

public class PiglinBarteringRecipe extends AbstractRecipe<SingleRecipeInput> {
    private static final RandomSource RANDOM = RandomSource.create();
    public static final MapCodec<PiglinBarteringRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(PiglinBarteringRecipe::getIngredient),
            WeightedList.nonEmptyCodec(RangedItem.MAP_CODEC).fieldOf("results").forGetter(PiglinBarteringRecipe::getWeightedResults)
    ).apply(instance, PiglinBarteringRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, PiglinBarteringRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            PiglinBarteringRecipe::getIngredient,
            WeightedList.streamCodec(RangedItem.STREAM_CODEC),
            PiglinBarteringRecipe::getWeightedResults,
            PiglinBarteringRecipe::new
    );

    private final Ingredient ingredient;
    private final WeightedList<RangedItem> weightedResults;

    public PiglinBarteringRecipe(Ingredient ingredient, WeightedList<RangedItem> weightedResults) {
        this.ingredient = ingredient;
        this.weightedResults = weightedResults;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public WeightedList<RangedItem> getWeightedResults() {
        return weightedResults;
    }

    @Override
    public boolean matches(SingleRecipeInput pContainer, Level pLevel) {
        return ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput pContainer) {
        return weightedResults.getRandom(RANDOM).map(rangedItem -> rangedItem.getInstance(RANDOM)).orElse(ItemStack.EMPTY);
    }

    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<? extends PiglinBarteringRecipe> getSerializer() {
        return ModRecipes.PIGLIN_BARTERING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends PiglinBarteringRecipe> getType() {
        return ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get();
    }
}
