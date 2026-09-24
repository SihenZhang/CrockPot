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
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
    public ItemStack assemble(Wrapper pContainer, HolderLookup.Provider pRegistryAccess) {
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
    public ItemStack getResultItem(HolderLookup.Provider registryAccess) {
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
        private static final MapCodec<ExplosionCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
                Ingredient.CODEC.fieldOf("ingredient").forGetter(ExplosionCraftingRecipe::getIngredient),
                RecipeCodecs.RESULT.fieldOf("result").forGetter(ExplosionCraftingRecipe::getResult),
                Codec.FLOAT.optionalFieldOf("lossrate", 0.0F).forGetter(ExplosionCraftingRecipe::getLossRate),
                Codec.BOOL.optionalFieldOf("onlyblock", false).forGetter(ExplosionCraftingRecipe::isOnlyBlock)
        ).apply(instance, ExplosionCraftingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, ExplosionCraftingRecipe> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

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

    public static class Wrapper extends SimpleContainer implements net.minecraft.world.item.crafting.RecipeInput {
        @Override
        public int size() {
            return getContainerSize();
        }

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
