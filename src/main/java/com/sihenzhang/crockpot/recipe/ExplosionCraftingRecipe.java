package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.util.JsonUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Random;

public class ExplosionCraftingRecipe extends AbstractCrockPotRecipe {
    private final Ingredient ingredient;
    private final ItemStack result;
    private final float lossRate;
    private final boolean onlyBlock;

    public ExplosionCraftingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, float lossRate, boolean onlyBlock) {
        super(id);
        Ingredient dummyInput = ingredient;
        boolean inputHasBlockItem = false;
        if (onlyBlock) {
            ItemStack[] items = ingredient.getItems();
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

    public boolean matches(ItemStack stack) {
        return this.ingredient.test(stack);
    }

    @Nullable
    public static ExplosionCraftingRecipe getRecipeFor(ItemStack stack, RecipeManager recipeManager) {
        return stack.isEmpty() ? null : recipeManager.getAllRecipesFor(CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get())
                .stream()
                .filter(r -> !r.isOnlyBlock() && r.matches(stack))
                .findFirst()
                .orElse(null);
    }

    @Nullable
    public static ExplosionCraftingRecipe getRecipeFor(BlockState state, RecipeManager recipeManager) {
        if (state == null) {
            return null;
        }
        Block block = state.getBlock();
        return block == Blocks.AIR || !(block.asItem() instanceof BlockItem) ? null : recipeManager.getAllRecipesFor(CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get())
                .stream()
                .filter(r -> r.matches(block.asItem().getDefaultInstance()))
                .findFirst()
                .orElse(null);
    }

    public ItemStack assemble(Random rand) {
        if (MathUtils.fuzzyIsZero(lossRate)) {
            return result.copy();
        }
        if (result.getCount() == 1) {
            return rand.nextFloat() >= lossRate ? result.copy() : ItemStack.EMPTY;
        }
        int count = (int) rand.doubles(result.getCount()).filter(d -> d >= lossRate).count();
        if (count == 0) {
            return ItemStack.EMPTY;
        }
        ItemStack output = result.copy();
        output.setCount(count);
        return output;
    }

    @Override
    public ItemStack getResultItem() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(this.ingredient);
        return ingredients;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get();
    }

    public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ExplosionCraftingRecipe> {
        @Override
        public ExplosionCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            Ingredient ingredient = JsonUtils.getAsIngredient(serializedRecipe, "ingredient");
            ItemStack result = JsonUtils.getAsItemStack(serializedRecipe, "result");
            float lossRate = Mth.clamp(GsonHelper.getAsFloat(serializedRecipe, "lossrate", 0.0F), 0.0F, 1.0F);
            boolean onlyBlock = GsonHelper.getAsBoolean(serializedRecipe, "onlyblock", false);
            return new ExplosionCraftingRecipe(recipeId, ingredient, result, lossRate, onlyBlock);
        }

        @Nullable
        @Override
        public ExplosionCraftingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float lossRate = buffer.readFloat();
            boolean onlyBlock = buffer.readBoolean();
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
}
