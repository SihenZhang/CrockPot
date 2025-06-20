package com.sihenzhang.crockpot.integration.jei;

import com.google.common.collect.*;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class FoodValuesDefinitionCache {
    private static Table<FoodCategory, Float, Set<Item>> CACHE;

    private FoodValuesDefinitionCache() {
    }

    public static void regenerate(Level level) {
        CACHE = null;
        var table = HashBasedTable.<FoodCategory, Float, Set<Item>>create();
        var allDefs = level.getRecipeManager().getAllRecipesFor(CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get());
        allDefs.stream().filter(FoodValuesDefinition::isItem)
                .forEach(itemDef -> itemDef.getNames().forEach(name -> {
                    var item = ForgeRegistries.ITEMS.getValue(name);
                    if (item != null && item != Items.AIR) {
                        itemDef.getFoodValues().entrySet()
                                .forEach(entry -> table.row(entry.getKey())
                                        .computeIfAbsent(entry.getValue(), c -> new HashSet<>())
                                        .add(item)
                                );
                    }
                }));
        allDefs.stream().filter(def -> !def.isItem())
                .forEach(tagDef -> tagDef.getNames().forEach(name -> {
                    var tag = ItemTags.create(name);
                    if (ForgeRegistries.ITEMS.tags().isKnownTagName(tag)) {
                        var tagIngredient = new Ingredient.TagValue(tag);
                        tagIngredient.getItems().forEach(stack -> FoodValuesDefinition.getFoodValues(stack, level).entrySet()
                                .forEach(entry -> table.row(entry.getKey())
                                        .computeIfAbsent(entry.getValue(), c -> new HashSet<>())
                                        .add(stack.getItem())
                                )
                        );
                    }
                }));
        var builder = ImmutableTable.<FoodCategory, Float, Set<Item>>builder();
        table.cellSet().forEach(cell -> builder.put(
                cell.getRowKey(),
                cell.getColumnKey(),
                ImmutableSortedSet.copyOf(
                        Comparator.comparing(
                                ForgeRegistries.ITEMS::getKey,
                                Comparator.comparing((ResourceLocation key) -> !"minecraft".equals(key.getNamespace()))
                                        .thenComparing(key -> !CrockPot.MOD_ID.equals(key.getNamespace()))
                                        .thenComparing(Comparator.naturalOrder())
                        ),
                        cell.getValue()
                )
        ));
        CACHE = builder.build();
    }

    public static Set<Item> getMatchedItems(FoodCategory category) {
        if (CACHE != null) {
            var builder = ImmutableSet.<Item>builder();
            CACHE.row(category).entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> builder.addAll(entry.getValue()));
            return builder.build();
        }
        return Set.of();
    }

    public static Map<Float, Set<Item>> getMatchedItemsByValues(FoodCategory category) {
        return CACHE != null ? Map.copyOf(CACHE.row(category)) : Map.of();
    }
}
