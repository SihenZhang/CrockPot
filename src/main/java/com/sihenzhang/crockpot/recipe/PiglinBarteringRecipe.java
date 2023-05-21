package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class PiglinBarteringRecipe extends AbstractRecipe {
    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;
    private final SimpleWeightedRandomList<RangedItem> weightedResults;

    public PiglinBarteringRecipe(ResourceLocation id, Ingredient ingredient, SimpleWeightedRandomList<RangedItem> weightedResults) {
        super(id);
        this.ingredient = ingredient;
        this.weightedResults = weightedResults;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public SimpleWeightedRandomList<RangedItem> getWeightedResults() {
        return weightedResults;
    }

    public boolean matches(ItemStack stack) {
        return this.ingredient.test(stack);
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess registryAccess) {
        var result = weightedResults.getRandomValue(RANDOM);
        if (result.isPresent()) {
            var rangedItem = result.get();
            if (rangedItem.isRanged()) {
                return new ItemStack(rangedItem.item, Mth.nextInt(RANDOM, rangedItem.min, rangedItem.max));
            }
            return new ItemStack(rangedItem.item, rangedItem.min);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess p_267052_) {
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
        @Override
        public PiglinBarteringRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            var ingredient = JsonUtils.getAsIngredient(serializedRecipe, "ingredient");
            var builder = SimpleWeightedRandomList.<RangedItem>builder();
            var results = GsonHelper.getAsJsonArray(serializedRecipe, "results");
            results.forEach(result -> {
                var rangedItem = RangedItem.fromJson(result);
                if (rangedItem != null) {
                    var weight = GsonHelper.getAsInt(GsonHelper.convertToJsonObject(result, "weighted ranged item"), "weight", 1);
                    builder.add(rangedItem, weight);
                }
            });
            return new PiglinBarteringRecipe(recipeId, ingredient, builder.build());
        }

        @Nullable
        @Override
        public PiglinBarteringRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var ingredient = Ingredient.fromNetwork(buffer);
            var builder = SimpleWeightedRandomList.<RangedItem>builder();
            var length = buffer.readVarInt();
            for (var i = 0; i < length; i++) {
                var rangedItem = RangedItem.fromNetwork(buffer);
                var weight = buffer.readVarInt();
                builder.add(rangedItem, weight);
            }
            return new PiglinBarteringRecipe(recipeId, ingredient, builder.build());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PiglinBarteringRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            var weightedRangedItems = recipe.getWeightedResults().unwrap();
            buffer.writeVarInt(weightedRangedItems.size());
            weightedRangedItems.forEach(weightedRangedItem -> {
                weightedRangedItem.getData().toNetwork(buffer);
                buffer.writeVarInt(weightedRangedItem.getWeight().asInt());
            });
        }
    }
}
