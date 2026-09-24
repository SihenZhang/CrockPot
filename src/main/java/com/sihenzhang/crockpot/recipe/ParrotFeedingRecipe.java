package com.sihenzhang.crockpot.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.item.CrockPotItems;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
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

    private final Ingredient ingredient;
    private final RangedItem result;

    public ParrotFeedingRecipe(Ingredient ingredient, RangedItem result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(SingleRecipeInput pContainer, Level pLevel) {
        return ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput pContainer, HolderLookup.Provider registryAccess) {
        return result.getInstance(RANDOM);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public RangedItem getResult() {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return new ItemStack(result.item, result.max);
    }

    @Override
    public ItemStack getToastSymbol() {
        return CrockPotItems.BIRDCAGE.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.PARROT_FEEDING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.PARROT_FEEDING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ParrotFeedingRecipe> {
        private static final MapCodec<ParrotFeedingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ParrotFeedingRecipe::getIngredient),
                RecipeCodecs.json(RangedItem::fromJson, RangedItem::toJson).fieldOf("result").forGetter(ParrotFeedingRecipe::getResult)
        ).apply(instance, ParrotFeedingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, ParrotFeedingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<ParrotFeedingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ParrotFeedingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ParrotFeedingRecipe fromNetwork(RegistryFriendlyByteBuf pBuffer) {
            var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(pBuffer);
            var result = RangedItem.fromNetwork(pBuffer);
            return new ParrotFeedingRecipe(ingredient, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf pBuffer, ParrotFeedingRecipe pRecipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(pBuffer, pRecipe.ingredient);
            pRecipe.result.toNetwork(pBuffer);
        }
    }
}
