package com.sihenzhang.crockpot.integration.jei;

import com.google.common.collect.*;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.client.ClientRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.ModRecipes;
import com.sihenzhang.crockpot.registry.FoodCategory;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.Level;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public final class FoodValuesDefinitionCache {
    private static final Comparator<Identifier> ITEM_ID_ORDER = Comparator
            .comparing((Identifier key) -> !"minecraft".equals(key.getNamespace()))
            .thenComparing(key -> !CrockPot.MOD_ID.equals(key.getNamespace()))
            .thenComparing(Comparator.naturalOrder());
    private static final Comparator<ItemStack> STACK_ORDER = Comparator.comparing(
            stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()),
            ITEM_ID_ORDER
    );

    private static Table<Holder<FoodCategory>, Float, Set<ItemStack>> cache;

    private FoodValuesDefinitionCache() {
    }

    public static void regenerate(Level level) {
        cache = null;
        var table = HashBasedTable.<Holder<FoodCategory>, Float, Set<ItemStack>>create();
        var allDefs = ClientRecipes.getRecipes(ModRecipes.FOOD_VALUES_RECIPE_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .toList();
        allDefs.stream().filter(FoodValuesDefinition::isItem)
                .forEach(itemDef -> itemDef.getNames().forEach(name -> {
                    var item = BuiltInRegistries.ITEM.getValue(name);
                    if (item != null && item != Items.AIR) {
                        var stack = item.getDefaultInstance();
                        itemDef.getFoodValues().entrySet()
                                .forEach(entry -> table.row(entry.getKey())
                                        .computeIfAbsent(entry.getValue(), _ -> new HashSet<>())
                                        .add(stack)
                                );
                    }
                }));
        allDefs.stream().filter(def -> !def.isItem())
                .forEach(tagDef -> tagDef.getNames().forEach(name -> BuiltInRegistries.ITEM.get(ItemTags.create(name))
                        .ifPresent(holders -> holders.stream().forEach(holder -> {
                            var stack = holder.value().getDefaultInstance();
                            FoodValuesDefinition.getFoodValues(stack, level).entrySet()
                                    .forEach(entry -> table.row(entry.getKey())
                                            .computeIfAbsent(entry.getValue(), _ -> new HashSet<>())
                                            .add(stack)
                                    );
                        }))));
        var builder = ImmutableTable.<Holder<FoodCategory>, Float, Set<ItemStack>>builder();
        table.cellSet().forEach(cell -> builder.put(
                cell.getRowKey(),
                cell.getColumnKey(),
                ImmutableSortedSet.copyOf(STACK_ORDER, cell.getValue())
        ));
        cache = builder.build();
    }

    public static Set<ItemStack> getMatchedItems(Holder<FoodCategory> category) {
        if (cache != null) {
            var builder = ImmutableSet.<ItemStack>builder();
            cache.row(category).entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> builder.addAll(entry.getValue()));
            return builder.build();
        }
        return Set.of();
    }

    public static Map<Float, Set<ItemStack>> getMatchedItemsByValues(Holder<FoodCategory> category) {
        return cache != null ? Map.copyOf(cache.row(category)) : Map.of();
    }
}
