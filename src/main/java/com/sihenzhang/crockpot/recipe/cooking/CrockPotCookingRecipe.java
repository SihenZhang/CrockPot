package com.sihenzhang.crockpot.recipe.cooking;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.recipe.AbstractRecipe;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CrockPotCookingRecipe extends AbstractRecipe<CrockPotCookingRecipe.Wrapper> {
    private static final RandomSource RANDOM = RandomSource.create();

    private final List<IRequirement> requirements;
    private final ItemStack result;
    private final int priority;
    private final int weight;
    private final int cookingTime;
    private final int potLevel;

    public CrockPotCookingRecipe(List<IRequirement> requirements, ItemStack result, int priority, int weight, int cookingTime, int potLevel) {
        this.requirements = ImmutableList.copyOf(requirements);
        this.result = result;
        this.priority = priority;
        this.weight = Math.max(weight, 1);
        this.cookingTime = cookingTime;
        this.potLevel = potLevel;
    }


    @Override
    public boolean matches(Wrapper input, Level level) {
        return input.potLevel() >= potLevel && requirements.stream().allMatch(r -> r.test(input));
    }

    @Override
    public ItemStack assemble(Wrapper input, HolderLookup.Provider registries) {
        return result.copy();
    }

    public static Optional<CrockPotCookingRecipe> getRecipeFor(Wrapper container, Level level) {
        var recipes = level.getRecipeManager().getRecipesFor(ModRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get(), container, level);
        var optionalMaxPriority = recipes.stream().map(RecipeHolder::value).mapToInt(CrockPotCookingRecipe::getPriority).max();
        if (optionalMaxPriority.isPresent()) {
            var maxPriority = optionalMaxPriority.getAsInt();
            var matchedRecipes = SimpleWeightedRandomList.<CrockPotCookingRecipe>builder();
            recipes.stream().map(RecipeHolder::value).filter(r -> r.getPriority() == maxPriority).forEach(r -> matchedRecipes.add(r, r.getWeight()));
            return matchedRecipes.build().getRandomValue(RANDOM);
        }
        return Optional.empty();
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
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return result;
    }

    @Override
    public ItemStack getToastSymbol() {
        return ModItems.CROCK_POT.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.CROCK_POT_COOKING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CrockPotCookingRecipe> {
        public static final MapCodec<CrockPotCookingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        IRequirement.CODEC.listOf().fieldOf("requirements").forGetter(CrockPotCookingRecipe::getRequirements),
                        ItemStack.CODEC.fieldOf("result").forGetter(CrockPotCookingRecipe::getResult),
                        Codec.INT.fieldOf("priority").forGetter(CrockPotCookingRecipe::getPriority),
                        Codec.intRange(1, Integer.MAX_VALUE).fieldOf("weight").orElse(1).forGetter(CrockPotCookingRecipe::getWeight),
                        Codec.INT.fieldOf("cookingtime").forGetter(CrockPotCookingRecipe::getCookingTime),
                        Codec.INT.fieldOf("potlevel").forGetter(CrockPotCookingRecipe::getPotLevel)
                ).apply(instance, CrockPotCookingRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, CrockPotCookingRecipe> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.collection(ArrayList::new, IRequirement.STREAM_CODEC),
                CrockPotCookingRecipe::getRequirements,
                ItemStack.STREAM_CODEC,
                CrockPotCookingRecipe::getResult,
                ByteBufCodecs.VAR_INT,
                CrockPotCookingRecipe::getPriority,
                ByteBufCodecs.VAR_INT,
                CrockPotCookingRecipe::getWeight,
                ByteBufCodecs.VAR_INT,
                CrockPotCookingRecipe::getCookingTime,
                ByteBufCodecs.VAR_INT,
                CrockPotCookingRecipe::getPotLevel,
                CrockPotCookingRecipe::new
        );

        @Override
        public MapCodec<CrockPotCookingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrockPotCookingRecipe> streamCodec() {
            return STREAM_CODEC;
        }
    }

    public record Wrapper(List<ItemStack> items, FoodValues foodValues, int potLevel) implements RecipeInput {
        @Override
        public ItemStack getItem(int index) {
            return items.get(index);
        }

        @Override
        public int size() {
            return items.size();
        }
    }
}
