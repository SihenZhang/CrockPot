package com.sihenzhang.crockpot.recipe.bartering;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.recipe.AbstractRecipe;
import com.sihenzhang.crockpot.recipe.CrockPotRecipeTypes;
import com.sihenzhang.crockpot.recipe.WeightedItem;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.*;

public class PiglinBarteringRecipe extends AbstractRecipe {
    private final Ingredient ingredient;
    private final List<WeightedItem> weightedResults;

    public PiglinBarteringRecipe(ResourceLocation id, Ingredient ingredient, List<WeightedItem> weightedResults) {
        super(id);
        this.ingredient = ingredient;
        List<WeightedItem> tmpWeightedResults = new ArrayList<>();
        weightedResults.forEach(weightedItem -> {
            WeightedItem dummy = null;
            Iterator<WeightedItem> iterator = tmpWeightedResults.iterator();
            while (iterator.hasNext()) {
                WeightedItem e = iterator.next();
                if (e.item == weightedItem.item && e.min == weightedItem.min && e.max == weightedItem.max) {
                    dummy = new WeightedItem(e.item, e.min, e.max, e.weight + weightedItem.weight);
                    iterator.remove();
                }
            }
            tmpWeightedResults.add(dummy != null ? dummy : weightedItem);
        });
        tmpWeightedResults.sort(Comparator.comparingInt((WeightedItem e) -> e.weight).reversed());
        this.weightedResults = ImmutableList.copyOf(tmpWeightedResults);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public List<WeightedItem> getWeightedResults() {
        return weightedResults;
    }

    public boolean matches(ItemStack stack) {
        return this.ingredient.test(stack);
    }

    @Nullable
    public static PiglinBarteringRecipe getRecipeFor(ItemStack stack, RecipeManager recipeManager) {
        return stack.isEmpty() ? null : recipeManager.getAllRecipesFor(CrockPotRecipeTypes.PIGLIN_BARTERING_RECIPE_TYPE).stream()
                .filter(r -> r.matches(stack))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static PiglinBarteringRecipe getRecipeFor(Item item, RecipeManager recipeManager) {
        return item == null || item == Items.AIR ? null : recipeManager.getAllRecipesFor(CrockPotRecipeTypes.PIGLIN_BARTERING_RECIPE_TYPE).stream()
                .filter(r -> r.matches(item.getDefaultInstance()))
                .findFirst()
                .orElse(null);
    }

    public ItemStack assemble(Random rand) {
        WeightedItem weightedItem = WeightedRandom.getRandomItem(rand, this.weightedResults);
        if (weightedItem.isRanged()) {
            return new ItemStack(weightedItem.item, MathHelper.nextInt(rand, weightedItem.min, weightedItem.max));
        } else {
            return new ItemStack(weightedItem.item, weightedItem.min);
        }
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient);
        return ingredients;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return CrockPotRegistry.piglinBartering;
    }

    @Override
    public IRecipeType<?> getType() {
        return CrockPotRecipeTypes.PIGLIN_BARTERING_RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PiglinBarteringRecipe> {
        @Override
        public PiglinBarteringRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            Ingredient ingredient = JsonUtils.getAsIngredient(serializedRecipe, "ingredient");
            List<WeightedItem> weightedResults = new ArrayList<>();
            JsonArray results = JSONUtils.getAsJsonArray(serializedRecipe, "results");
            results.forEach(result -> {
                WeightedItem weightedItem = WeightedItem.fromJson(result);
                if (weightedItem != null && !weightedItem.isEmpty()) {
                    weightedResults.add(weightedItem);
                }
            });
            return new PiglinBarteringRecipe(recipeId, ingredient, weightedResults);
        }

        @Nullable
        @Override
        public PiglinBarteringRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            List<WeightedItem> weightedResults = new ArrayList<>();
            int length = buffer.readVarInt();
            for (int i = 0; i < length; i++) {
                weightedResults.add(WeightedItem.fromNetwork(buffer));
            }
            return new PiglinBarteringRecipe(recipeId, ingredient, weightedResults);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, PiglinBarteringRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeVarInt(recipe.getWeightedResults().size());
            recipe.getWeightedResults().forEach(weightedItem -> weightedItem.toNetwork(buffer));
        }
    }
}
