package com.sihenzhang.crockpot.integration.kubejs;

import dev.latvian.mods.kubejs.recipe.IngredientMatch;
import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ExplosionCraftingRecipeJS extends AbstractCrockPotRecipeJS {

    public final List<Ingredient> inputItems = new ArrayList<>();
    public final List<ItemStack> outputItems = new ArrayList<>();

    @Override
    public void create(RecipeArguments args) {
        outputItems.add(this.parseItemOutput(args.get(0)));
        inputItems.add(this.parseItemInput(args.get(1)));
        if (args.size() >= 3) {
            this.lossRate(((Number) args.get(2)).floatValue());
        }
        if (args.size() >= 4) {
            this.onlyBlock((Boolean) args.get(3));
        }
    }

    @Override
    public boolean hasInput(IngredientMatch ingredientMatch) {
        throw new RuntimeException("PLEASE IMPLEMENT THIS");
    }

    @Override
    public boolean hasOutput(IngredientMatch ingredientMatch) {
        throw new RuntimeException("PLEASE IMPLEMENT THIS");
    }

    @Override
    public boolean replaceInput(IngredientMatch ingredientMatch, Ingredient ingredient, ItemInputTransformer itemInputTransformer) {
        throw new RuntimeException("PLEASE IMPLEMENT THIS");
    }

    @Override
    public boolean replaceOutput(IngredientMatch ingredientMatch, ItemStack itemStack, ItemOutputTransformer itemOutputTransformer) {
        throw new RuntimeException("PLEASE IMPLEMENT THIS");
    }

    @Override
    public void deserialize() {
        outputItems.add(this.parseItemOutput(json.get("result")));
        inputItems.add(this.parseItemInput(json.get("ingredient")));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", itemToJson(outputItems.get(0)));
        }
        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
        }
    }

    public ExplosionCraftingRecipeJS lossRate(float lossRate) {
        json.addProperty("lossrate", Mth.clamp(lossRate, 0.0F, 1.0F));
        return this;
    }

    public ExplosionCraftingRecipeJS onlyBlock(boolean onlyBlock) {
        json.addProperty("onlyblock", onlyBlock);
        return this;
    }

    public ExplosionCraftingRecipeJS onlyBlock() {
        return this.onlyBlock(true);
    }
}
