package com.sihenzhang.crockpot.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.Util;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.Arrays;
import java.util.stream.IntStream;

public class ExplosionCraftingRecipe extends AbstractRecipe<ExplosionCraftingRecipe.Wrapper> {
    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;
    private final ItemStack result;
    private final float lossRate;
    private final boolean onlyBlock;

    public ExplosionCraftingRecipe(Ingredient ingredient, ItemStack result, float lossRate, boolean onlyBlock) {
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
    public ItemStack assemble(Wrapper input, HolderLookup.Provider registries) {
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
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.EXPLOSION_CRAFTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ExplosionCraftingRecipe> {
        public static final MapCodec<ExplosionCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result),
                        Codec.floatRange(0.0F, 1.0F).optionalFieldOf("lossRate", 0.0F).forGetter(recipe -> recipe.lossRate),
                        Codec.BOOL.optionalFieldOf("onlyBlock", false).forGetter(recipe -> recipe.onlyBlock)
                ).apply(instance, ExplosionCraftingRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, ExplosionCraftingRecipe> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<ExplosionCraftingRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, ExplosionCraftingRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static ExplosionCraftingRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            var ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buffer);
            var result = ItemStack.STREAM_CODEC.decode(buffer);
            var lossRate = buffer.readFloat();
            var onlyBlock = buffer.readBoolean();
            return new ExplosionCraftingRecipe(ingredient, result, lossRate, onlyBlock);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, ExplosionCraftingRecipe recipe) {
            Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, recipe.getIngredient());
            ItemStack.STREAM_CODEC.encode(buffer, recipe.getResult());
            buffer.writeFloat(recipe.getLossRate());
            buffer.writeBoolean(recipe.isOnlyBlock());
        }
    }

    public record Wrapper(ItemStack item, boolean isFromBlock) implements RecipeInput {
        public Wrapper(ItemStack item) {
            this(item, false);
        }

        @Override
        public ItemStack getItem(int index) {
            if (index != 0) {
                throw new IllegalArgumentException("No item for index " + index);
            }
            return this.item;
        }

        @Override
        public int size() {
            return 1;
        }
    }
}
