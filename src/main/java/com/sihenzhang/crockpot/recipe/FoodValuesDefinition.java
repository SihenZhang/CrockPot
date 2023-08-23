package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class FoodValuesDefinition extends AbstractRecipe<Container> {
    private final Set<ResourceLocation> names;
    private final FoodValues foodValues;
    private final boolean item;

    public FoodValuesDefinition(ResourceLocation id, Set<ResourceLocation> names, FoodValues foodValues, boolean item) {
        super(id);
        this.names = ImmutableSet.copyOf(names);
        this.foodValues = foodValues;
        this.item = item;
    }

    public Set<ResourceLocation> getNames() {
        return names;
    }

    public FoodValues getFoodValues() {
        return foodValues;
    }

    public boolean isItem() {
        return item;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        var stack = pContainer.getItem(0);
        return item ? names.stream().anyMatch(name -> name.equals(ForgeRegistries.ITEMS.getKey(stack.getItem()))) :
                names.stream().anyMatch(name -> stack.is(ItemTags.create(name)));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public static FoodValues getFoodValues(ItemStack stack, Level level) {
        var allDefs = level.getRecipeManager().getRecipesFor(CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get(), new SimpleContainer(stack), level);
        if (allDefs.isEmpty()) {
            return FoodValues.create();
        }
        return allDefs.stream().filter(FoodValuesDefinition::isItem).findFirst().map(FoodValuesDefinition::getFoodValues).orElseGet(() -> {
            var foodValues = FoodValues.create();
            var maxCount = -1L;
            var tagDefs = new HashMap<ResourceLocation, FoodValues>();
            allDefs.forEach(def -> def.getNames().forEach(name -> tagDefs.put(name, def.getFoodValues())));
            for (var tag : stack.getTags().map(TagKey::location).filter(tagDefs::containsKey).toList()) {
                var count = tag.getPath().chars().filter(c -> c == '/').count();
                if (count < maxCount) {
                    continue;
                }
                if (count > maxCount) {
                    maxCount = count;
                    foodValues.clear();
                }
                tagDefs.get(tag).entrySet().forEach(entry -> foodValues.put(entry.getKey(),
                        Math.max(foodValues.get(entry.getKey()), entry.getValue())));
            }
            return foodValues;
        });
    }

    public static Set<ItemStack> getMatchedItems(FoodCategory category, Level level) {
        // make vanilla items and Crock Pot mod items at the top of the collection
        var builder = ImmutableSortedSet.orderedBy(
                Comparator.comparing((ItemStack stack) -> getFoodValues(stack, level).get(category))
                        .thenComparing(stack -> ForgeRegistries.ITEMS.getKey(stack.getItem()),
                                Comparator.comparing((ResourceLocation key) -> !"minecraft".equals(key.getNamespace()))
                                        .thenComparing(key -> !CrockPot.MOD_ID.equals(key.getNamespace()))
                                        .thenComparing(Comparator.naturalOrder())
                        )
        );
        var allDefs = level.getRecipeManager().getAllRecipesFor(CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get()).stream().filter(def -> def.getFoodValues().has(category)).toList();
        allDefs.stream().filter(FoodValuesDefinition::isItem).forEach(itemDef -> itemDef.getNames().forEach(name -> {
            var item = ForgeRegistries.ITEMS.getValue(name);
            if (item != null && item != Items.AIR) {
                builder.add(item.getDefaultInstance());
            }
        }));
        allDefs.stream().filter(def -> !def.isItem()).forEach(tagDef -> tagDef.getNames().forEach(name -> {
            var tag = ItemTags.create(name);
            if (ForgeRegistries.ITEMS.tags().isKnownTagName(tag)) {
                // get all items with the tag
                var tagIngredient = new Ingredient.TagValue(tag);
                tagIngredient.getItems().forEach(stack -> {
                    // use getFoodValues method to make sure there's no higher priority definition
                    if (getFoodValues(stack, level).has(category)) {
                        builder.add(stack);
                    }
                });
            }
        }));
        return builder.build();
    }

    @Nonnull
    public static List<FoodCategoryMatchedItems> getFoodCategoryMatchedItemsList(Level level) {
        var builder = ImmutableList.<FoodCategoryMatchedItems>builder();
        for (var category : FoodCategory.values()) {
            builder.add(new FoodCategoryMatchedItems(category, getMatchedItems(category, level)));
        }
        return builder.build();
    }

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.FOOD_VALUES_RECIPE_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get();
    }

    public record FoodCategoryMatchedItems(FoodCategory category, Set<ItemStack> items) {
    }

    public static class Serializer implements RecipeSerializer<FoodValuesDefinition> {
        @Override
        public FoodValuesDefinition fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            var foodValues = FoodValues.fromJson(GsonHelper.getAsJsonObject(serializedRecipe, "values"));
            if (serializedRecipe.has("items") && serializedRecipe.has("tags")) {
                throw new JsonParseException("A food value definition entry needs either tags or items, not both");
            } else if (serializedRecipe.has("items") || serializedRecipe.has("tags")) {
                var names = new HashSet<ResourceLocation>();
                var isItem = serializedRecipe.has("items");
                GsonHelper.getAsJsonArray(serializedRecipe, isItem ? "items" : "tags").forEach(name -> names.add(new ResourceLocation(GsonHelper.convertToString(name, isItem ? "item" : "tag"))));
                return new FoodValuesDefinition(recipeId, names, foodValues, isItem);
            } else {
                throw new JsonParseException("A food value definition entry needs either tags or items");
            }
        }

        @Nullable
        @Override
        public FoodValuesDefinition fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            var isItem = buffer.readBoolean();
            var names = new HashSet<ResourceLocation>();
            var length = buffer.readVarInt();
            for (var i = 0; i < length; i++) {
                names.add(buffer.readResourceLocation());
            }
            var foodValues = FoodValues.fromNetwork(buffer);
            return new FoodValuesDefinition(recipeId, names, foodValues, isItem);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, FoodValuesDefinition recipe) {
            buffer.writeBoolean(recipe.isItem());
            buffer.writeVarInt(recipe.getNames().size());
            recipe.getNames().forEach(buffer::writeResourceLocation);
            recipe.getFoodValues().toNetwork(buffer);
        }
    }
}
