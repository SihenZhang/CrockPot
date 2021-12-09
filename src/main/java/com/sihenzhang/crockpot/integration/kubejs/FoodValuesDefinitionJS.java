package com.sihenzhang.crockpot.integration.kubejs;

import com.google.gson.JsonArray;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import dev.latvian.kubejs.util.ListJS;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashSet;
import java.util.Set;

public class FoodValuesDefinitionJS extends AbstractCrockPotRecipeJS {
    public Set<ResourceLocation> names = new HashSet<>();
    public FoodValues foodValues;
    public boolean isTag;

    @Override
    public void create(ListJS args) {
        this.foodValues = this.parseFoodValues(args.get(0));
        this.foodValues.entrySet().forEach(entry -> this.outputItems.add(this.parseResultItem(FoodCategory.getItemStack(entry.getKey()))));
        this.isTag = (Boolean) args.get(1);
        for (int i = 2; i < args.size(); i++) {
            this.define(args.get(i).toString());
        }
    }

    @Override
    public void deserialize() {
        foodValues = this.parseFoodValues(JSONUtils.getAsJsonObject(json, "values"));
        foodValues.entrySet().forEach(entry -> outputItems.add(this.parseResultItem(FoodCategory.getItemStack(entry.getKey()))));
        isTag = json.has("tags");
        JSONUtils.getAsJsonArray(json, isTag ? "tags" : "items").forEach(o -> this.define(o.getAsString()));
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
            ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(rl);
            if (tag != null) {
                inputItems.add(this.parseIngredientItem(Ingredient.of(tag)));
            }
        } else {
            Item item = ForgeRegistries.ITEMS.getValue(rl);
            if (item != null && item != Items.AIR) {
                inputItems.add(this.parseIngredientItem(Ingredient.of(item)));
            }
        }
        return this;
    }
}
