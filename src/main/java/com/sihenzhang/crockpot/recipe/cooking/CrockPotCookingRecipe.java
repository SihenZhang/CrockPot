package com.sihenzhang.crockpot.recipe.cooking;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.recipe.AbstractRecipe;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.RecipeCodecs;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

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

    public CrockPotCookingRecipe(List<IRequirement> requirements, ItemStack result, int priority, int weight, int cookingTime, int potLevel) {
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
    public ItemStack assemble(CrockPotCookingRecipe.Wrapper pContainer, HolderLookup.Provider pRegistryAccess) {
        return result.copy();
    }

    public static Optional<CrockPotCookingRecipe> getRecipeFor(CrockPotCookingRecipe.Wrapper container, Level level) {
        var recipes = level.getRecipeManager().getRecipesFor(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get(), container, level).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).toList();
        var optionalMaxPriority = recipes.stream().mapToInt(CrockPotCookingRecipe::getPriority).max();
        if (optionalMaxPriority.isPresent()) {
            var maxPriority = optionalMaxPriority.getAsInt();
            var matchedRecipes = SimpleWeightedRandomList.<CrockPotCookingRecipe>builder();
            recipes.stream().filter(r -> r.getPriority() == maxPriority).forEach(r -> matchedRecipes.add(r, r.getWeight()));
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
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
        return result;
    }

    @Override
    public ItemStack getToastSymbol() {
        return CrockPotItems.CROCK_POT.get().getDefaultInstance();
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
        private static final MapCodec<CrockPotCookingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                RecipeCodecs.json(IRequirement::fromJson, IRequirement::toJson).listOf().fieldOf("requirements").forGetter(CrockPotCookingRecipe::getRequirements),
                RecipeCodecs.RESULT.fieldOf("result").forGetter(CrockPotCookingRecipe::getResult),
                Codec.INT.fieldOf("priority").forGetter(CrockPotCookingRecipe::getPriority),
                Codec.INT.optionalFieldOf("weight", 1).forGetter(CrockPotCookingRecipe::getWeight),
                Codec.INT.fieldOf("cookingtime").forGetter(CrockPotCookingRecipe::getCookingTime),
                Codec.INT.fieldOf("potlevel").forGetter(CrockPotCookingRecipe::getPotLevel)
        ).apply(instance, CrockPotCookingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, CrockPotCookingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<CrockPotCookingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, CrockPotCookingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static CrockPotCookingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var length = buffer.readVarInt();
            var requirements = IntStream.range(0, length).mapToObj(i -> IRequirement.fromNetwork(buffer)).toList();
            var result = ItemStack.STREAM_CODEC.decode(buffer);
            var priority = buffer.readVarInt();
            var weight = buffer.readVarInt();
            var cookingTime = buffer.readVarInt();
            var potLevel = buffer.readByte();
            return new CrockPotCookingRecipe(requirements, result, priority, weight, cookingTime, potLevel);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, CrockPotCookingRecipe recipe) {
            buffer.writeVarInt(recipe.getRequirements().size());
            recipe.getRequirements().forEach(requirement -> requirement.toNetwork(buffer));
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getResult());
            buffer.writeVarInt(recipe.getPriority());
            buffer.writeVarInt(recipe.getWeight());
            buffer.writeVarInt(recipe.getCookingTime());
            buffer.writeByte(recipe.getPotLevel());
        }
    }

    public static class Wrapper extends SimpleContainer implements net.minecraft.world.item.crafting.RecipeInput {
        @Override
        public int size() {
            return getContainerSize();
        }

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
