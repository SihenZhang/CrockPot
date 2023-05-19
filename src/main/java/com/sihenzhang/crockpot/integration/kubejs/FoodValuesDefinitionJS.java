package com.sihenzhang.crockpot.integration.kubejs;

import com.google.gson.JsonArray;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import dev.latvian.mods.kubejs.recipe.IngredientMatch;
import dev.latvian.mods.kubejs.recipe.ItemInputTransformer;
import dev.latvian.mods.kubejs.recipe.ItemOutputTransformer;
import dev.latvian.mods.kubejs.recipe.RecipeArguments;
import dev.latvian.mods.kubejs.util.ListJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FoodValuesDefinitionJS extends AbstractCrockPotRecipeJS {
    public final List<Ingredient> inputItems = new ArrayList<>();
    public final List<ItemStack> outputItems = new ArrayList<>();
    public Set<ResourceLocation> names = new HashSet<>();
    public FoodValues foodValues;
    public boolean isTag;

    @Override
    public void create(RecipeArguments args) {
        this.foodValues = this.parseFoodValues(args.get(0));
        this.foodValues.entrySet().forEach(entry -> this.outputItems.add(this.parseItemOutput(FoodCategory.getItemStack(entry.getKey()))));
        this.isTag = (Boolean) args.get(1);
        for (int i = 2; i < args.size(); i++) {
            this.define(args.get(i).toString());
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
        foodValues = this.parseFoodValues(GsonHelper.getAsJsonObject(json, "values"));
        foodValues.entrySet().forEach(entry -> outputItems.add(this.parseItemOutput(FoodCategory.getItemStack(entry.getKey()))));
        isTag = json.has("tags");
        GsonHelper.getAsJsonArray(json, isTag ? "tags" : "items").forEach(o -> this.define(o.getAsString()));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("values", foodValues.toJson());
        }
        if (serializeInputs) {
            JsonArray arr = new JsonArray();
            names.forEach(name -> arr.add(name.toString()));
            json.add(isTag ? "tags" : "items", arr);
        }
    }

    public FoodValuesDefinitionJS define(String name) {
        String nameWithoutHashSymbol = name.replace("#", "");
        ResourceLocation rl = new ResourceLocation(nameWithoutHashSymbol);
        names.add(rl);
        if (isTag) {
            TagKey<Item> tag = ItemTags.create(rl);
            if (ForgeRegistries.ITEMS.tags().isKnownTagName(tag)) {
                inputItems.add(this.parseItemInput(Ingredient.of(tag)));
            }
        } else {
            Item item = ForgeRegistries.ITEMS.getValue(rl);
            if (item != null && item != Items.AIR) {
                inputItems.add(this.parseItemInput(Ingredient.of(item)));
            }
        }
        return this;
    }
}
