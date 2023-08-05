package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.JsonUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.IntStream;

public class ExplosionCraftingRecipe extends AbstractRecipe<ExplosionCraftingRecipe.Wrapper> {
    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;
    private final ItemStack result;
    private final float lossRate;
    private final boolean onlyBlock;

    public ExplosionCraftingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, float lossRate, boolean onlyBlock) {
        super(id);
        var dummyInput = ingredient;
        var inputHasBlockItem = false;
        if (onlyBlock) {
            var items = ingredient.getItems();
            inputHasBlockItem = Arrays.stream(items).anyMatch(stack -> stack.getItem() instanceof BlockItem);
            if (inputHasBlockItem) {
                dummyInput = Ingredient.of(Arrays.stream(items).filter(stack -> stack.getItem() instanceof BlockItem));
            }
        }
        this.ingredient = dummyInput;
        this.result = result;
        this.lossRate = Mth.clamp(lossRate, 0.0F, 1.0F);
        this.onlyBlock = inputHasBlockItem;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public float getLossRate() {
        return lossRate;
    }

    public boolean isOnlyBlock() {
        return onlyBlock;
    }

    @Override
    public boolean matches(Wrapper pContainer, Level pLevel) {
        return (pContainer.isFromBlock() || !onlyBlock) && ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Wrapper pContainer, RegistryAccess pRegistryAccess) {
        if (MathUtils.fuzzyIsZero(lossRate)) {
            return result.copy();
        }
        if (result.getCount() == 1) {
            return RANDOM.nextFloat() >= lossRate ? result.copy() : ItemStack.EMPTY;
        }
        var count = (int) IntStream.range(0, result.getCount()).filter(i -> RANDOM.nextFloat() >= lossRate).count();
        if (count == 0) {
            return ItemStack.EMPTY;
        }
        var output = result.copy();
        output.setCount(count);
        return output;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ExplosionCraftingRecipe> {
        @Override
        public ExplosionCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            var ingredient = JsonUtils.getAsIngredient(serializedRecipe, "ingredient");
            var result = JsonUtils.getAsItemStack(serializedRecipe, "result");
            var lossRate = Mth.clamp(GsonHelper.getAsFloat(serializedRecipe, "lossrate", 0.0F), 0.0F, 1.0F);
            var onlyBlock = GsonHelper.getAsBoolean(serializedRecipe, "onlyblock", false);
            return new ExplosionCraftingRecipe(recipeId, ingredient, result, lossRate, onlyBlock);
        }

        @Nullable
        @Override
        public ExplosionCraftingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var ingredient = Ingredient.fromNetwork(buffer);
            var result = buffer.readItem();
            var lossRate = buffer.readFloat();
            var onlyBlock = buffer.readBoolean();
            return new ExplosionCraftingRecipe(recipeId, ingredient, result, lossRate, onlyBlock);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, ExplosionCraftingRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeItem(recipe.getResult());
            buffer.writeFloat(recipe.getLossRate());
            buffer.writeBoolean(recipe.isOnlyBlock());
        }
    }

    public static class Wrapper extends SimpleContainer {
        private final boolean fromBlock;

        public Wrapper(ItemStack item, boolean fromBlock) {
            super(item);
            this.fromBlock = fromBlock;
        }

        public Wrapper(ItemStack item) {
            this(item, false);
        }

        public boolean isFromBlock() {
            return fromBlock;
        }
    }
}
