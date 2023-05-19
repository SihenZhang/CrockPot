package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.util.JsonUtils;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class ParrotFeedingRecipe extends AbstractRecipe {
    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;
    private final RangedItem result;

    public ParrotFeedingRecipe(ResourceLocation id, Ingredient ingredient, RangedItem result) {
        super(id);
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess registryAccess) {
        return result.getInstance(RANDOM);
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public RangedItem getResult() {
        return result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(result.item, result.max);
    }

    @Override
    public ItemStack getToastSymbol() {
        return CrockPotItems.BIRDCAGE.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.PARROT_FEEDING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.PARROT_FEEDING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ParrotFeedingRecipe> {
        @Override
        public ParrotFeedingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            var ingredient = JsonUtils.getAsIngredient(pSerializedRecipe, "ingredient");
            var result = RangedItem.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
            return new ParrotFeedingRecipe(pRecipeId, ingredient, result);
        }

        @Nullable
        @Override
        public ParrotFeedingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            var ingredient = Ingredient.fromNetwork(pBuffer);
            var result = RangedItem.fromNetwork(pBuffer);
            return new ParrotFeedingRecipe(pRecipeId, ingredient, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf pBuffer, ParrotFeedingRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pRecipe.result.toNetwork(pBuffer);
        }
    }
}
