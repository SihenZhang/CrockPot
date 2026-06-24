package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.util.RecipeUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public class DryingRecipe extends AbstractRecipe<SingleRecipeInput> {
    public static final MapCodec<DryingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.optionalFieldOf("ingredient").forGetter(DryingRecipe::getIngredient),
            IRequirement.CODEC.listOf().optionalFieldOf("requirements", List.of()).forGetter(DryingRecipe::getRequirements),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(DryingRecipe::getResult),
            Codec.INT.fieldOf("dryingtime").forGetter(DryingRecipe::getDryingTime)
    ).apply(instance, DryingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, DryingRecipe> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public DryingRecipe decode(RegistryFriendlyByteBuf buffer) {
            var ingredient = buffer.readBoolean()
                    ? Optional.of(Ingredient.CONTENTS_STREAM_CODEC.decode(buffer))
                    : Optional.<Ingredient>empty();
            var requirements = IRequirement.STREAM_CODEC.apply(ByteBufCodecs.list()).decode(buffer);
            var result = ItemStackTemplate.STREAM_CODEC.decode(buffer);
            var dryingTime = ByteBufCodecs.VAR_INT.decode(buffer);
            return new DryingRecipe(ingredient, requirements, result, dryingTime);
        }

        @Override
        public void encode(RegistryFriendlyByteBuf buffer, DryingRecipe recipe) {
            buffer.writeBoolean(recipe.ingredient.isPresent());
            recipe.ingredient.ifPresent(value -> Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, value));
            IRequirement.STREAM_CODEC.apply(ByteBufCodecs.list()).encode(buffer, recipe.requirements);
            ItemStackTemplate.STREAM_CODEC.encode(buffer, recipe.result);
            ByteBufCodecs.VAR_INT.encode(buffer, recipe.dryingTime);
        }
    };

    private final Optional<Ingredient> ingredient;
    private final List<IRequirement> requirements;
    private final ItemStackTemplate result;
    private final int dryingTime;

    public DryingRecipe(Optional<Ingredient> ingredient, List<IRequirement> requirements, ItemStackTemplate result, int dryingTime) {
        if (ingredient.isEmpty() && requirements.isEmpty()) {
            throw new IllegalArgumentException("A drying recipe needs either an ingredient or food value requirements");
        }
        this.ingredient = ingredient;
        this.requirements = ImmutableList.copyOf(requirements);
        this.result = result;
        this.dryingTime = Math.max(dryingTime, 1);
    }

    public static Optional<RecipeHolder<DryingRecipe>> getRecipeFor(ItemStack stack, Level level) {
        var input = new SingleRecipeInput(stack);
        var recipes = RecipeUtil.getRecipesFor(ModRecipes.DRYING_RECIPE_TYPE.get(), input, level)
                .filter(recipe -> !ItemStack.isSameItemSameComponents(recipe.value().assemble(input), stack))
                .toList();
        return recipes.stream()
                .filter(recipe -> recipe.value().isIngredientRecipe())
                .findFirst()
                .or(() -> recipes.stream().findFirst());
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        var stack = input.getItem(0);
        if (stack.isEmpty()) {
            return false;
        }
        if (ingredient.isPresent()) {
            return ingredient.get().test(stack);
        }
        var foodValues = FoodValuesDefinition.getFoodValues(stack, level);
        var wrapper = new CrockPotCookingRecipe.Wrapper(List.of(stack.copyWithCount(1)), foodValues, 0);
        return requirements.stream().allMatch(requirement -> requirement.test(wrapper));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input) {
        return result.create();
    }

    public boolean isIngredientRecipe() {
        return ingredient.isPresent();
    }

    public Optional<Ingredient> getIngredient() {
        return ingredient;
    }

    public List<IRequirement> getRequirements() {
        return requirements;
    }

    public ItemStackTemplate getResult() {
        return result;
    }

    public int getDryingTime() {
        return dryingTime;
    }

    public NonNullList<Ingredient> getIngredients() {
        return ingredient.map(value -> {
            var ingredients = NonNullList.<Ingredient>create();
            ingredients.add(value);
            return ingredients;
        }).orElseGet(NonNullList::create);
    }

    public ItemStack getResultItem() {
        return result.create();
    }

    @Override
    public RecipeSerializer<? extends DryingRecipe> getSerializer() {
        return ModRecipes.DRYING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends DryingRecipe> getType() {
        return ModRecipes.DRYING_RECIPE_TYPE.get();
    }
}
