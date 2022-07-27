package com.sihenzhang.crockpot.recipe.bartering;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.recipe.AbstractCrockPotRecipe;
import com.sihenzhang.crockpot.recipe.RangedItem;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class PiglinBarteringRecipe extends AbstractCrockPotRecipe {
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

    @Nullable
    public static PiglinBarteringRecipe getRecipeFor(ItemStack stack, RecipeManager recipeManager) {
        return stack.isEmpty() ? null : recipeManager.getAllRecipesFor(CrockPotRegistry.PIGLIN_BARTERING_RECIPE_TYPE.get()).stream()
                .filter(r -> r.matches(stack))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static PiglinBarteringRecipe getRecipeFor(Item item, RecipeManager recipeManager) {
        return item == null || item == Items.AIR ? null : recipeManager.getAllRecipesFor(CrockPotRegistry.PIGLIN_BARTERING_RECIPE_TYPE.get()).stream()
                .filter(r -> r.matches(item.getDefaultInstance()))
                .findFirst()
                .orElse(null);
    }

    public ItemStack assemble(Random rand) {
        Optional<RangedItem> result = weightedResults.getRandomValue(rand);
        if (result.isPresent()) {
            RangedItem rangedItem = result.get();
            if (rangedItem.isRanged()) {
                return new ItemStack(rangedItem.item, Mth.nextInt(rand, rangedItem.min, rangedItem.max));
            } else {
                return new ItemStack(rangedItem.item, rangedItem.min);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient);
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRegistry.PIGLIN_BARTERING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRegistry.PIGLIN_BARTERING_RECIPE_TYPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<PiglinBarteringRecipe> {
        @Override
        public PiglinBarteringRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            Ingredient ingredient = JsonUtils.getAsIngredient(serializedRecipe, "ingredient");
            SimpleWeightedRandomList.Builder<RangedItem> builder = SimpleWeightedRandomList.builder();
            JsonArray results = GsonHelper.getAsJsonArray(serializedRecipe, "results");
            results.forEach(result -> {
                RangedItem rangedItem = RangedItem.fromJson(result);
                if (rangedItem != null) {
                    int weight = GsonHelper.getAsInt(GsonHelper.convertToJsonObject(result, "weighted ranged item"), "weight", 1);
                    builder.add(rangedItem, weight);
                }
            });
            return new PiglinBarteringRecipe(recipeId, ingredient, builder.build());
        }

        @Nullable
        @Override
        public PiglinBarteringRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            SimpleWeightedRandomList.Builder<RangedItem> builder = SimpleWeightedRandomList.builder();
            int length = buffer.readVarInt();
            for (int i = 0; i < length; i++) {
                RangedItem rangedItem = RangedItem.fromNetwork(buffer);
                int weight = buffer.readVarInt();
                builder.add(rangedItem, weight);
            }
            return new PiglinBarteringRecipe(recipeId, ingredient, builder.build());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PiglinBarteringRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            List<WeightedEntry.Wrapper<RangedItem>> weightedRangedItems = recipe.getWeightedResults().unwrap();
            buffer.writeVarInt(weightedRangedItems.size());
            weightedRangedItems.forEach(weightedRangedItem -> {
                weightedRangedItem.getData().toNetwork(buffer);
                buffer.writeVarInt(weightedRangedItem.getWeight().asInt());
            });
        }
    }
}
