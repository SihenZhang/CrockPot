package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.CrockPotRegistry;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class FoodValuesDefinition extends AbstractRecipe {
    private final Set<ResourceLocation> names;
    private final FoodValues foodValues;
    private final boolean isTag;

    public FoodValuesDefinition(ResourceLocation id, Set<ResourceLocation> names, FoodValues foodValues, boolean isTag) {
        super(id);
        this.names = ImmutableSet.copyOf(names);
        this.foodValues = foodValues;
        this.isTag = isTag;
    }

    public Set<ResourceLocation> getNames() {
        return names;
    }

    public FoodValues getFoodValues() {
        return foodValues;
    }

    public boolean isTag() {
        return isTag;
    }

    @Nonnull
    public static FoodValues getFoodValues(Item item, RecipeManager recipeManager) {
        if (item == null || item == Items.AIR) {
            return FoodValues.create();
        }
        List<FoodValuesDefinition> allDefs = recipeManager.getAllRecipesFor(CrockPotRecipeTypes.FOOD_VALUES_RECIPE_TYPE);
        Optional<FoodValuesDefinition> itemDef = allDefs.stream()
                .filter(def -> !def.isTag() && def.getNames().stream().anyMatch(name -> name.equals(item.getRegistryName())))
                .findFirst();
        if (itemDef.isPresent()) {
            return itemDef.get().getFoodValues();
        } else {
            FoodValues foodValues = FoodValues.create();
            long maxCount = -1L;
            ImmutableMap.Builder<ResourceLocation, FoodValues> tagDefsBuilder = ImmutableMap.builder();
            allDefs.stream().filter(FoodValuesDefinition::isTag)
                    .forEach(tagDef -> tagDef.getNames().forEach(name -> tagDefsBuilder.put(name, tagDef.getFoodValues())));
            ImmutableMap<ResourceLocation, FoodValues> tagDefs = tagDefsBuilder.build();
            for (ResourceLocation tag : item.getTags()) {
                if (tagDefs.containsKey(tag)) {
                    long count = tag.toString().chars().filter(c -> c == '/').count();
                    if (count < maxCount) {
                        continue;
                    }
                    if (count > maxCount) {
                        maxCount = count;
                        foodValues.clear();
                    }
                    tagDefs.get(tag).entrySet().forEach(entry -> foodValues.put(entry.getKey(), Math.max(foodValues.get(entry.getKey()), entry.getValue())));
                }
            }
            return foodValues;
        }
    }

    @Nonnull
    public static Set<Item> getMatchedItems(FoodCategory category, RecipeManager recipeManager) {
        // make vanilla items and Crock Pot mod items at the top of the collection
        ImmutableSortedSet.Builder<Item> builder = ImmutableSortedSet.orderedBy((i1, i2) -> {
            ResourceLocation r1 = i1.getRegistryName();
            ResourceLocation r2 = i2.getRegistryName();
            String n1 = Objects.requireNonNull(r1).getNamespace();
            String n2 = Objects.requireNonNull(r2).getNamespace();
            float v1 = getFoodValues(i1, recipeManager).get(category);
            float v2 = getFoodValues(i2, recipeManager).get(category);
            if (MathUtils.fuzzyEquals(v1, v2)) {
                if ("minecraft".equals(n1)) {
                    return "minecraft".equals(n2) ? r1.compareTo(r2) : -1;
                } else if ("minecraft".equals(n2)) {
                    return 1;
                } else if (CrockPot.MOD_ID.equals(n1)) {
                    return CrockPot.MOD_ID.equals(n2) ? r1.compareTo(r2) : -1;
                } else if (CrockPot.MOD_ID.equals(n2)) {
                    return 1;
                } else {
                    return r1.compareTo(r2);
                }
            } else {
                return Float.compare(v1, v2);
            }
        });
        List<FoodValuesDefinition> allDefs = recipeManager.getAllRecipesFor(CrockPotRecipeTypes.FOOD_VALUES_RECIPE_TYPE);
        allDefs.stream().filter(def -> !def.isTag()).forEach(itemDef -> itemDef.getNames().forEach(name -> {
            Item item = ForgeRegistries.ITEMS.getValue(name);
            if (item != null && item != Items.AIR && itemDef.getFoodValues().has(category)) {
                builder.add(item);
            }
        }));
        allDefs.stream().filter(FoodValuesDefinition::isTag).forEach(tagDef -> tagDef.getNames().forEach(name -> {
            ITag<Item> tag = TagCollectionManager.getInstance().getItems().getTag(name);
            if (tag != null && tagDef.getFoodValues().has(category)) {
                // get all items with the tag
                Ingredient.IItemList tagList = new Ingredient.TagList(tag);
                tagList.getItems().forEach(stack -> {
                    Item item = stack.getItem();
                    // use getFoodValues method to make sure there's no higher priority definition
                    if (getFoodValues(item, recipeManager).has(category)) {
                        builder.add(item);
                    }
                });
            }
        }));
        return builder.build();
    }

    @Nonnull
    public static List<FoodCategoryMatchedItems> getFoodCategoryMatchedItemsList(RecipeManager recipeManager) {
        ImmutableList.Builder<FoodCategoryMatchedItems> builder = ImmutableList.builder();
        for (FoodCategory category : FoodCategory.values()) {
            builder.add(new FoodCategoryMatchedItems(category, getMatchedItems(category, recipeManager)));
        }
        return builder.build();
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return CrockPotRegistry.foodValues;
    }

    @Override
    public IRecipeType<?> getType() {
        return CrockPotRecipeTypes.FOOD_VALUES_RECIPE_TYPE;
    }

    public static class FoodCategoryMatchedItems {
        private final FoodCategory category;
        private final Set<Item> items;

        public FoodCategoryMatchedItems(FoodCategory category, Set<Item> items) {
            this.category = category;
            this.items = items;
        }

        public FoodCategory getCategory() {
            return category;
        }

        public Set<Item> getItems() {
            return items;
        }
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<FoodValuesDefinition> {
        @Override
        public FoodValuesDefinition fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            if (serializedRecipe.has("items") && serializedRecipe.has("tags")) {
                throw new JsonParseException("A food value definition entry needs either tags or items, not both");
            } else if (serializedRecipe.has("items")) {
                Set<ResourceLocation> items = new HashSet<>();
                JSONUtils.getAsJsonArray(serializedRecipe, "items").forEach(item -> items.add(new ResourceLocation(JSONUtils.convertToString(item, "item"))));
                FoodValues foodValues = FoodValues.fromJson(JSONUtils.getAsJsonObject(serializedRecipe, "values"));
                return new FoodValuesDefinition(recipeId, items, foodValues, false);
            } else if (serializedRecipe.has("tags")) {
                Set<ResourceLocation> tags = new HashSet<>();
                JSONUtils.getAsJsonArray(serializedRecipe, "tags").forEach(tag -> tags.add(new ResourceLocation(JSONUtils.convertToString(tag, "tag"))));
                FoodValues foodValues = FoodValues.fromJson(JSONUtils.getAsJsonObject(serializedRecipe, "values"));
                return new FoodValuesDefinition(recipeId, tags, foodValues, true);
            } else {
                throw new JsonParseException("A food value definition entry needs either tags or items");
            }
        }

        @Nullable
        @Override
        public FoodValuesDefinition fromNetwork(ResourceLocation recipeId, PacketBuffer buffer) {
            boolean isTag = buffer.readBoolean();
            Set<ResourceLocation> names = new HashSet<>();
            int length = buffer.readVarInt();
            for (int i = 0; i < length; i++) {
                names.add(buffer.readResourceLocation());
            }
            FoodValues foodValues = FoodValues.fromNetwork(buffer);
            return new FoodValuesDefinition(recipeId, names, foodValues, isTag);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, FoodValuesDefinition recipe) {
            buffer.writeBoolean(recipe.isTag());
            buffer.writeVarInt(recipe.getNames().size());
            recipe.getNames().forEach(buffer::writeResourceLocation);
            recipe.getFoodValues().toNetwork(buffer);
        }
    }
}
