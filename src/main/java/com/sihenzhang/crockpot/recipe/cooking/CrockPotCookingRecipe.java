package com.sihenzhang.crockpot.recipe.cooking;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.recipe.AbstractRecipe;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

public class CrockPotCookingRecipe extends AbstractRecipe<CrockPotCookingRecipe.Wrapper> {
    private static final RandomSource RANDOM = RandomSource.create();

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

    @Override
    public boolean matches(CrockPotCookingRecipe.Wrapper pContainer, Level pLevel) {
        return pContainer.getPotLevel() >= potLevel && requirements.stream().allMatch(r -> r.test(pContainer));
    }

    @Override
    public ItemStack assemble(CrockPotCookingRecipe.Wrapper pContainer, RegistryAccess pRegistryAccess) {
        return result.copy();
    }

    public static Optional<CrockPotCookingRecipe> getRecipeFor(CrockPotCookingRecipe.Wrapper container, Level level) {
        var recipes = level.getRecipeManager().getRecipesFor(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get(), container, level);
        var optionalMaxPriority = recipes.stream().mapToInt(CrockPotCookingRecipe::getPriority).max();
        if (optionalMaxPriority.isPresent()) {
            var maxPriority = optionalMaxPriority.getAsInt();
            var matchedRecipes = SimpleWeightedRandomList.<CrockPotCookingRecipe>builder();
            recipes.stream().filter(r -> r.getPriority() == maxPriority).forEach(r -> matchedRecipes.add(r, r.getWeight()));
            return matchedRecipes.build().getRandomValue(RANDOM);
        } else {
            return Optional.empty();
        }
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

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return result;
    }

    @Override
    public ItemStack getToastSymbol() {
        return CrockPotItems.BASIC_CROCK_POT.get().getDefaultInstance();
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

    public static class Wrapper extends SimpleContainer {
        private final FoodValues foodValues;
        private final int potLevel;

        public Wrapper(List<ItemStack> items, FoodValues foodValues, int potLevel) {
            super(items.toArray(new ItemStack[0]));
            this.foodValues = foodValues;
            this.potLevel = potLevel;
        }

        public FoodValues getFoodValues() {
            return foodValues;
        }

        public int getPotLevel() {
            return potLevel;
        }
    }
}
