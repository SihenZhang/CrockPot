package com.sihenzhang.crockpot.recipe.cooking;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.core.FoodValues;
import com.sihenzhang.crockpot.item.ModItems;
import com.sihenzhang.crockpot.recipe.AbstractRecipe;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class CrockPotCookingRecipe extends AbstractRecipe<CrockPotCookingRecipe.Wrapper> {
    private static final RandomSource RANDOM = RandomSource.create();
    public static final MapCodec<CrockPotCookingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            IRequirement.CODEC.listOf().fieldOf("requirements").forGetter(CrockPotCookingRecipe::getRequirements),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(CrockPotCookingRecipe::getResult),
            Codec.INT.fieldOf("priority").forGetter(CrockPotCookingRecipe::getPriority),
            Codec.INT.optionalFieldOf("weight", 1).forGetter(CrockPotCookingRecipe::getWeight),
            Codec.INT.fieldOf("cookingtime").forGetter(CrockPotCookingRecipe::getCookingTime),
            Codec.INT.fieldOf("potlevel").forGetter(CrockPotCookingRecipe::getPotLevel)
    ).apply(instance, CrockPotCookingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, CrockPotCookingRecipe> STREAM_CODEC = StreamCodec.composite(
            IRequirement.STREAM_CODEC.apply(ByteBufCodecs.list()),
            CrockPotCookingRecipe::getRequirements,
            ItemStackTemplate.STREAM_CODEC,
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

    private final List<IRequirement> requirements;
    private final ItemStackTemplate result;
    private final int priority;
    private final int weight;
    private final int cookingTime;
    private final int potLevel;

    public CrockPotCookingRecipe(List<IRequirement> requirements, ItemStackTemplate result, int priority, int weight, int cookingTime, int potLevel) {
        this.requirements = ImmutableList.copyOf(requirements);
        this.result = result;
        this.priority = priority;
        this.weight = Math.max(weight, 1);
        this.cookingTime = cookingTime;
        this.potLevel = potLevel;
    }

    @Override
    public boolean matches(CrockPotCookingRecipe.Wrapper pContainer, Level pLevel) {
        return pContainer.potLevel() >= potLevel && requirements.stream().allMatch(r -> r.test(pContainer));
    }

    @Override
    public ItemStack assemble(CrockPotCookingRecipe.Wrapper pContainer) {
        return result.create();
    }

    public static Optional<CrockPotCookingRecipe> getRecipeFor(CrockPotCookingRecipe.Wrapper container, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return Optional.empty();
        }
        var recipes = serverLevel.recipeAccess().recipeMap()
                .getRecipesFor(ModRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get(), container, level)
                .map(RecipeHolder::value)
                .toList();
        var optionalMaxPriority = recipes.stream().mapToInt(CrockPotCookingRecipe::getPriority).max();
        if (optionalMaxPriority.isPresent()) {
            var maxPriority = optionalMaxPriority.getAsInt();
            var matchedRecipes = WeightedList.<CrockPotCookingRecipe>builder();
            recipes.stream().filter(r -> r.getPriority() == maxPriority).forEach(r -> matchedRecipes.add(r, r.getWeight()));
            return matchedRecipes.build().getRandom(RANDOM);
        }
        return Optional.empty();
    }

    public List<IRequirement> getRequirements() {
        return requirements;
    }

    public ItemStackTemplate getResult() {
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

    public ItemStack getResultItem() {
        return result.create();
    }

    public NonNullList<ItemStack> getRemainingItems(Wrapper input) {
        NonNullList<ItemStack> remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);
        for (int i = 0; i < input.size(); i++) {
            var remainder = input.getItem(i).getItem().getCraftingRemainder(input.getItem(i));
            if (remainder != null) {
                remainingItems.set(i, remainder.create());
            }
        }
        return remainingItems;
    }

    public ItemStack getToastSymbol() {
        return ModItems.CROCK_POT.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<? extends CrockPotCookingRecipe> getSerializer() {
        return ModRecipes.CROCK_POT_COOKING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends CrockPotCookingRecipe> getType() {
        return ModRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get();
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
