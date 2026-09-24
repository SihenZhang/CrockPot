package com.sihenzhang.crockpot.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;


public class PiglinBarteringRecipe extends AbstractRecipe<SingleRecipeInput> {
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
    public boolean matches(SingleRecipeInput pContainer, Level pLevel) {
        return ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput pContainer, HolderLookup.Provider registryAccess) {
        return weightedResults.getRandomValue(RANDOM).map(rangedItem -> rangedItem.isRanged() ? new ItemStack(rangedItem.item, Mth.nextInt(RANDOM, rangedItem.min, rangedItem.max)) : new ItemStack(rangedItem.item, rangedItem.min)).orElse(ItemStack.EMPTY);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider p_267052_) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.PIGLIN_BARTERING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PiglinBarteringRecipe> {
        private static final MapCodec<PiglinBarteringRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(PiglinBarteringRecipe::getIngredient),
                RecipeCodecs.json(Serializer::readResults, Serializer::writeResults).fieldOf("results").forGetter(PiglinBarteringRecipe::getWeightedResults)
        ).apply(instance, PiglinBarteringRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, PiglinBarteringRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<PiglinBarteringRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, PiglinBarteringRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static SimpleWeightedRandomList<RangedItem> readResults(com.google.gson.JsonElement json) {
            var builder = SimpleWeightedRandomList.<RangedItem>builder();
            json.getAsJsonArray().forEach(result -> {
                var rangedItem = RangedItem.fromJson(result);
                if (rangedItem != null) {
                    var weight = GsonHelper.getAsInt(GsonHelper.convertToJsonObject(result, "weighted ranged item"), "weight", 1);
                    builder.add(rangedItem, weight);
                }
            });
            return builder.build();
        }

        private static com.google.gson.JsonElement writeResults(SimpleWeightedRandomList<RangedItem> results) {
            var array = new com.google.gson.JsonArray();
            results.unwrap().forEach(result -> {
                var json = result.data().toJson().getAsJsonObject();
                json.addProperty("weight", result.getWeight().asInt());
                array.add(json);
            });
            return array;
        }

        private static PiglinBarteringRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            var builder = SimpleWeightedRandomList.<RangedItem>builder();
            var length = buffer.readVarInt();
            for (var i = 0; i < length; i++) {
                var rangedItem = RangedItem.fromNetwork(buffer);
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
                weightedRangedItem.data().toNetwork(buffer);
                buffer.writeVarInt(weightedRangedItem.getWeight().asInt());
            });
        }
    }
}
