package com.sihenzhang.crockpot.recipe.explosion;

import com.google.gson.*;
import com.sihenzhang.crockpot.util.JsonUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.math.MathHelper;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;
import java.util.function.Predicate;

public class ExplosionCraftingRecipe implements Predicate<Item> {
    private static final Random RAND = new Random();
    private final Ingredient input;
    private final Item output;
    private final float lossRate;
    private final boolean onlyBlock;

    public static final ExplosionCraftingRecipe EMPTY = new ExplosionCraftingRecipe(Ingredient.EMPTY, Items.AIR);

    public ExplosionCraftingRecipe(Ingredient input, Item output, float lossRate, boolean onlyBlock) {
        Ingredient dummyInput = input;
        boolean inputHasBlockItem = false;
        if (onlyBlock) {
            ItemStack[] items = input.getItems();
            inputHasBlockItem = Arrays.stream(items).anyMatch(stack -> stack.getItem() instanceof BlockItem);
            if (inputHasBlockItem) {
                dummyInput = Ingredient.of(Arrays.stream(items).filter(stack -> stack.getItem() instanceof BlockItem));
            }
        }
        this.input = dummyInput;
        this.output = output;
        this.lossRate = MathHelper.clamp(lossRate, 0.0F, 1.0F);
        this.onlyBlock = inputHasBlockItem;
    }

    public ExplosionCraftingRecipe(Ingredient input, Item output, float lossRate) {
        this(input, output, lossRate, false);
    }

    public ExplosionCraftingRecipe(Ingredient input, Item output, boolean onlyBlock) {
        this(input, output, 0.0F, onlyBlock);
    }

    public ExplosionCraftingRecipe(Ingredient input, Item output) {
        this(input, output, 0.0F, false);
    }

    public Ingredient getInput() {
        return this.input;
    }

    public Item getOutput() {
        return this.output;
    }

    public float getLossRate() {
        return this.lossRate;
    }

    public boolean isOnlyBlock() {
        return this.onlyBlock;
    }

    public boolean isEmpty() {
        return this == ExplosionCraftingRecipe.EMPTY || this.input.isEmpty() || this.output == null || this.output == Items.AIR || this.lossRate >= 1.0F;
    }

    public ItemStack createOutput() {
        if (RAND.nextFloat() >= this.lossRate) {
            return new ItemStack(output);
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public boolean test(Item item) {
        return this.input.test(item.getDefaultInstance());
    }

    public static class Serializer implements JsonDeserializer<ExplosionCraftingRecipe>, JsonSerializer<ExplosionCraftingRecipe> {
        @Override
        public ExplosionCraftingRecipe deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject object = json.getAsJsonObject();
            Ingredient input = JsonUtils.getAsIngredient(object, "input");
            Item output = JsonUtils.getAsItem(object, "output");
            float lossRate = MathHelper.clamp(JSONUtils.getAsFloat(object, "lossRate", 0.0F), 0.0F, 1.0F);
            boolean onlyBlock = JSONUtils.getAsBoolean(object, "onlyBlock", false);
            return new ExplosionCraftingRecipe(input, output, lossRate, onlyBlock);
        }

        @Override
        public JsonElement serialize(ExplosionCraftingRecipe src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject object = new JsonObject();
            object.add("input", src.input.toJson());
            object.addProperty("output", Objects.requireNonNull(src.output.getRegistryName()).toString());
            if (!MathUtils.fuzzyIsZero(src.lossRate)) {
                object.addProperty("lossRate", src.lossRate);
            }
            if (src.onlyBlock) {
                object.addProperty("onlyBlock", true);
            }
            return object;
        }
    }
}
