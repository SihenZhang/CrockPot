package com.sihenzhang.crockpot.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class PiglinBarteringRecipe extends AbstractRecipe<RecipeInput> {
    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;
    private final SimpleWeightedRandomList<RangedItem> weightedResults;

    public PiglinBarteringRecipe(Ingredient ingredient, SimpleWeightedRandomList<RangedItem> weightedResults) {
        this.ingredient = ingredient;
        this.weightedResults = weightedResults;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public SimpleWeightedRandomList<RangedItem> getWeightedResults() {
        return weightedResults;
    }

    @Override
    public boolean matches(RecipeInput input, Level level) {
        return ingredient.test(input.getItem(0));
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return weightedResults.getRandomValue(RANDOM).map(rangedItem -> rangedItem.isRanged() ? new ItemStack(rangedItem.item, Mth.nextInt(RANDOM, rangedItem.min, rangedItem.max)) : new ItemStack(rangedItem.item, rangedItem.min)).orElse(ItemStack.EMPTY);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.PIGLIN_BARTERING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PiglinBarteringRecipe> {
        public static final MapCodec<PiglinBarteringRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(PiglinBarteringRecipe::getIngredient),
                        SimpleWeightedRandomList.wrappedCodecAllowingEmpty(RangedItem.CODEC).fieldOf("results").forGetter(PiglinBarteringRecipe::getWeightedResults)
                ).apply(instance, PiglinBarteringRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, PiglinBarteringRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<PiglinBarteringRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PiglinBarteringRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static PiglinBarteringRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            var builder = SimpleWeightedRandomList.<RangedItem>builder();
            var length = buffer.readVarInt();
            for (var i = 0; i < length; i++) {
                var rangedItem = RangedItem.STREAM_CODEC.decode(buffer);
                var weight = buffer.readVarInt();
                builder.add(rangedItem, weight);
            }
            return new PiglinBarteringRecipe(ingredient, builder.build());
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, PiglinBarteringRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getIngredient());
            var weightedRangedItems = recipe.getWeightedResults().unwrap();
            buffer.writeVarInt(weightedRangedItems.size());
            weightedRangedItems.forEach(weightedRangedItem -> {
                RangedItem.STREAM_CODEC.encode(buffer, weightedRangedItem.data());
                buffer.writeVarInt(weightedRangedItem.weight().asInt());
            });
        }
    }
}
