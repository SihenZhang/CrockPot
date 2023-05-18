package com.sihenzhang.crockpot.recipe.cooking;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.recipe.AbstractCrockPotRecipe;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;

import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class CrockPotCookingRecipe extends AbstractCrockPotRecipe {
    private final List<IRequirement> requirements;
    private final ItemStack result;
    private final int priority;
    private final int weight;
    private final int cookingTime;
    private final int potLevel;

    public CrockPotCookingRecipe(ResourceLocation id, List<IRequirement> requirements, ItemStack result, int priority, int weight, int cookingTime, int potLevel) {
        super(id);
        this.requirements = ImmutableList.copyOf(requirements);
        this.result = result;
        this.priority = priority;
        this.weight = Math.max(weight, 1);
        this.cookingTime = cookingTime;
        this.potLevel = potLevel;
    }

    public List<IRequirement> getRequirements() {
        return requirements;
    }

    public ItemStack getResult() {
        return result;
    }

    public int getPriority() {
        return priority;
    }

    public int getWeight() {
        return weight;
    }

    public int getCookingTime() {
        return cookingTime;
    }

    public int getPotLevel() {
        return potLevel;
    }

    public boolean matches(CrockPotCookingRecipeInput input) {
        return input.potLevel() >= potLevel && requirements.stream().allMatch(r -> r.test(input));
    }

    @Nullable
    public static CrockPotCookingRecipe getRecipeFor(CrockPotCookingRecipeInput input, RandomSource random, RecipeManager recipeManager) {
        var recipes = recipeManager.getAllRecipesFor(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get());
        recipes.sort(Comparator.comparing(CrockPotCookingRecipe::getPriority).reversed());
        var matchedRecipes = SimpleWeightedRandomList.<CrockPotCookingRecipe>builder();
        var isFirst = true;
        var priority = 0;
        for (var recipe : recipes) {
            if (isFirst) {
                if (recipe.matches(input)) {
                    priority = recipe.getPriority();
                    matchedRecipes.add(recipe, recipe.getWeight());
                    isFirst = false;
                }
            } else {
                if (recipe.getPriority() != priority) {
                    break;
                } else {
                    if (recipe.matches(input)) {
                        matchedRecipes.add(recipe, recipe.getWeight());
                    }
                }
            }
        }
        return matchedRecipes.build().getRandomValue(random).orElse(null);
    }

    public ItemStack assemble() {
        return result.copy();
    }

    @Override
    public ItemStack getResultItem() {
        return result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.CROCK_POT_COOKING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CrockPotCookingRecipe> {
        @Override
        public CrockPotCookingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            var requirements = Streams.stream(GsonHelper.getAsJsonArray(serializedRecipe, "requirements")).map(IRequirement::fromJson).toList();
            var result = JsonUtils.getAsItemStack(serializedRecipe, "result");
            var priority = GsonHelper.getAsInt(serializedRecipe, "priority");
            var weight = GsonHelper.getAsInt(serializedRecipe, "weight", 1);
            var cookingTime = GsonHelper.getAsInt(serializedRecipe, "cookingtime");
            var potLevel = GsonHelper.getAsInt(serializedRecipe, "potlevel");
            return new CrockPotCookingRecipe(recipeId, requirements, result, priority, weight, cookingTime, potLevel);
        }

        @Nullable
        @Override
        public CrockPotCookingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var length = buffer.readVarInt();
            var requirements = IntStream.range(0, length).mapToObj(i -> IRequirement.fromNetwork(buffer)).toList();
            var result = buffer.readItem();
            var priority = buffer.readVarInt();
            var weight = buffer.readVarInt();
            var cookingTime = buffer.readVarInt();
            var potLevel = buffer.readByte();
            return new CrockPotCookingRecipe(recipeId, requirements, result, priority, weight, cookingTime, potLevel);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrockPotCookingRecipe recipe) {
            buffer.writeVarInt(recipe.getRequirements().size());
            recipe.getRequirements().forEach(requirement -> requirement.toNetwork(buffer));
            buffer.writeItem(recipe.getResult());
            buffer.writeVarInt(recipe.getPriority());
            buffer.writeVarInt(recipe.getWeight());
            buffer.writeVarInt(recipe.getCookingTime());
            buffer.writeByte(recipe.getPotLevel());
        }
    }
}
