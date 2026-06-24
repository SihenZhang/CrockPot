package com.sihenzhang.crockpot.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.util.NumberUtil;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Util;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.stream.IntStream;

public class ExplosionCraftingRecipe extends AbstractRecipe<ExplosionCraftingRecipe.Wrapper> {
    private static final RandomSource RANDOM = RandomSource.create();
    public static final MapCodec<ExplosionCraftingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Ingredient.CODEC.fieldOf("ingredient").forGetter(ExplosionCraftingRecipe::getIngredient),
            ItemStackTemplate.CODEC.fieldOf("result").forGetter(ExplosionCraftingRecipe::getResult),
            Codec.FLOAT.optionalFieldOf("lossrate", 0.0F).forGetter(ExplosionCraftingRecipe::getLossRate),
            Codec.BOOL.optionalFieldOf("onlyblock", false).forGetter(ExplosionCraftingRecipe::isOnlyBlock)
    ).apply(instance, ExplosionCraftingRecipe::new));
    public static final StreamCodec<RegistryFriendlyByteBuf, ExplosionCraftingRecipe> STREAM_CODEC = StreamCodec.composite(
            Ingredient.CONTENTS_STREAM_CODEC,
            ExplosionCraftingRecipe::getIngredient,
            ItemStackTemplate.STREAM_CODEC,
            ExplosionCraftingRecipe::getResult,
            ByteBufCodecs.FLOAT,
            ExplosionCraftingRecipe::getLossRate,
            ByteBufCodecs.BOOL,
            ExplosionCraftingRecipe::isOnlyBlock,
            ExplosionCraftingRecipe::new
    );

    private final Ingredient ingredient;
    private final ItemStackTemplate result;
    private final float lossRate;
    private final boolean onlyBlock;

    public ExplosionCraftingRecipe(Ingredient ingredient, ItemStackTemplate result, float lossRate, boolean onlyBlock) {
        var dummyInput = ingredient;
        var inputHasBlockItem = false;
        if (onlyBlock) {
            var items = ingredient.items().toList();
            inputHasBlockItem = items.stream().anyMatch(holder -> holder.value() instanceof BlockItem);
            if (inputHasBlockItem) {
                dummyInput = Ingredient.of(items.stream().map(holder -> holder.value()).filter(item -> item instanceof BlockItem));
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

    public ItemStackTemplate getResult() {
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
    public ItemStack assemble(Wrapper pContainer) {
        if (NumberUtil.isClose(lossRate, 0.0F)) {
            return result.create();
        }
        if (result.count() == 1) {
            return RANDOM.nextFloat() >= lossRate ? result.create() : ItemStack.EMPTY;
        }
        var count = (int) IntStream.range(0, result.count()).filter(i -> RANDOM.nextFloat() >= lossRate).count();
        if (count == 0) {
            return ItemStack.EMPTY;
        }
        return result.withCount(count).create();
    }

    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(ingredient));
    }

    public ItemStack getResultItem() {
        return this.result.create();
    }

    @Override
    public RecipeSerializer<? extends ExplosionCraftingRecipe> getSerializer() {
        return ModRecipes.EXPLOSION_CRAFTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<? extends ExplosionCraftingRecipe> getType() {
        return ModRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get();
    }

    public static class Wrapper extends SimpleContainer implements RecipeInput {
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

        @Override
        public int size() {
            return this.getContainerSize();
        }
    }
}
