package com.sihenzhang.crockpot.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ParrotFeedingRecipe extends AbstractRecipe<RecipeInput> {
    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;
    private final RangedItem result;

    public ParrotFeedingRecipe(Ingredient ingredient, RangedItem result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
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
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return new ItemStack(result.item, result.max);
    }

    @Override
    public ItemStack getToastSymbol() {
        return ModItems.BIRDCAGE.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PARROT_FEEDING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.PARROT_FEEDING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ParrotFeedingRecipe> {
        public static final MapCodec<ParrotFeedingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(ParrotFeedingRecipe::getIngredient),
                        RangedItem.CODEC.fieldOf("result").forGetter(ParrotFeedingRecipe::getResult)
                ).apply(instance, ParrotFeedingRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ParrotFeedingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<ParrotFeedingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ParrotFeedingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ParrotFeedingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            var result = RangedItem.STREAM_CODEC.decode(buffer);
            return new ParrotFeedingRecipe(ingredient, result);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ParrotFeedingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getIngredient());
            RangedItem.STREAM_CODEC.encode(buffer, recipe.getResult());
        }
    }
}
