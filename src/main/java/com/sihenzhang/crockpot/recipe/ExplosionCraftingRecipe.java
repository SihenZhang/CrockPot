package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
        this.lossRate = MathHelper.clamp(lossRate, 0.0F, 1.0F);
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
        return stack.isEmpty() ? null : recipeManager.getAllRecipesFor(CrockPotRecipeTypes.EXPLOSION_CRAFT_RECIPE_TYPE)
                .stream()
                .filter(r -> !r.isOnlyBlock() && r.matches(stack))
                .findAny()
                .orElse(null);
    }

    @Nullable
    public static ExplosionCraftingRecipe getRecipeFor(BlockState state, RecipeManager recipeManager) {
        if (state == null) {
            return null;
        }
        Block block = state.getBlock();
        return block == Blocks.AIR || !(block.asItem() instanceof BlockItem) ? null : recipeManager.getAllRecipesFor(CrockPotRecipeTypes.EXPLOSION_CRAFT_RECIPE_TYPE)
                .stream()
                .filter(r -> r.matches(block.asItem().getDefaultInstance()))
                .findAny()
                .orElse(null);
    }

    public ItemStack assemble(Random rand) {
        return rand.nextFloat() >= this.lossRate ? this.result.copy() : ItemStack.EMPTY;
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
    public IRecipeSerializer<?> getSerializer() {
        return CrockPotRegistry.explosionCrafting;
    }

    @Override
    public IRecipeType<?> getType() {
        return CrockPotRecipeTypes.EXPLOSION_CRAFT_RECIPE_TYPE;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ExplosionCraftingRecipe> {
        @Override
        public ExplosionCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            Ingredient ingredient = JsonUtils.getAsIngredient(serializedRecipe, "ingredient");
            ItemStack result = JsonUtils.getAsItemStack(serializedRecipe, "result");
            float lossRate = MathHelper.clamp(JSONUtils.getAsFloat(serializedRecipe, "lossrate", 0.0F), 0.0F, 1.0F);
            boolean onlyBlock = JSONUtils.getAsBoolean(serializedRecipe, "onlyblock", false);
            return new ExplosionCraftingRecipe(recipeId, ingredient, result, lossRate, onlyBlock);
        }

        @Nullable
        @Override
        public ExplosionCraftingRecipe fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float lossRate = buffer.readFloat();
            boolean onlyBlock = buffer.readBoolean();
            return new ExplosionCraftingRecipe(recipeId, ingredient, result, lossRate, onlyBlock);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, ExplosionCraftingRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeItem(recipe.getResult());
            buffer.writeFloat(recipe.getLossRate());
            buffer.writeBoolean(recipe.isOnlyBlock());
        }
    }
}
