package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.NeoForgeExtraCodecs;

import javax.annotation.Nonnull;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class FoodValuesDefinition extends AbstractRecipe<RecipeInput> {
    private final Set<ResourceLocation> names;
    private final FoodValues foodValues;
    private final boolean item;

    public FoodValuesDefinition(Set<ResourceLocation> names, FoodValues foodValues, boolean item) {
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
    public boolean matches(RecipeInput input, Level level) {
        var stack = input.getItem(0);
        return item ? names.stream().anyMatch(name -> name.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()))) :
                names.stream().anyMatch(name -> stack.is(ItemTags.create(name)));
    }

    @Override
    public ItemStack assemble(RecipeInput input, HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider registries) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public static FoodValues getFoodValues(ItemStack stack, Level level) {
        var allDefs = level.getRecipeManager().getRecipesFor(ModRecipes.FOOD_VALUES_RECIPE_TYPE.get(), new SingleRecipeInput(stack), level);
        if (allDefs.isEmpty()) {
            return FoodValues.create();
        }
        return allDefs.stream().map(RecipeHolder::value).filter(FoodValuesDefinition::isItem).findFirst().map(FoodValuesDefinition::getFoodValues).orElseGet(() -> {
            var foodValues = FoodValues.create();
            var maxCount = -1L;
            var tagDefs = new HashMap<ResourceLocation, FoodValues>();
            allDefs.stream().map(RecipeHolder::value).forEach(def -> def.getNames().forEach(name -> tagDefs.put(name, def.getFoodValues())));
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
                        .thenComparing(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()),
                                Comparator.comparing((ResourceLocation key) -> !"minecraft".equals(key.getNamespace()))
                                        .thenComparing(key -> !CrockPot.MOD_ID.equals(key.getNamespace()))
                                        .thenComparing(Comparator.naturalOrder())
                        )
        );
        var allDefs = level.getRecipeManager().getAllRecipesFor(ModRecipes.FOOD_VALUES_RECIPE_TYPE.get()).stream().map(RecipeHolder::value).filter(def -> def.getFoodValues().has(category)).toList();
        allDefs.stream().filter(FoodValuesDefinition::isItem).forEach(itemDef -> itemDef.getNames().forEach(name -> {
            var item = BuiltInRegistries.ITEM.get(name);
            if (item != null && item != Items.AIR) {
                builder.add(item.getDefaultInstance());
            }
        }));
        allDefs.stream().filter(def -> !def.isItem()).forEach(tagDef -> tagDef.getNames().forEach(name -> {
            var tag = ItemTags.create(name);
            if (BuiltInRegistries.ITEM.getTag(tag).isPresent()) {
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

    @Override
    @Nonnull
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.FOOD_VALUES_RECIPE_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return ModRecipes.FOOD_VALUES_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<FoodValuesDefinition> {
        public static final MapCodec<FoodValuesDefinition> CODEC = RecordCodecBuilder.mapCodec(
                instance -> instance.group(
                        NeoForgeExtraCodecs.xor(
                                NeoForgeExtraCodecs.setOf(ResourceLocation.CODEC).fieldOf("items"),
                                NeoForgeExtraCodecs.setOf(ResourceLocation.CODEC).fieldOf("tags")
                        ).forGetter(recipe -> recipe.isItem() ? Either.left(recipe.names) : Either.right(recipe.names)),
                        FoodValues.CODEC.fieldOf("values").forGetter(recipe -> recipe.foodValues)
                ).apply(instance, (itemsOrTags, foodValues) -> {
                    var isItem = itemsOrTags.left().isPresent();
                    var names = isItem ? itemsOrTags.left().get() : itemsOrTags.right().get();
                    return new FoodValuesDefinition(names, foodValues, isItem);
                })
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, FoodValuesDefinition> STREAM_CODEC = StreamCodec.of(
                Serializer::toNetwork, Serializer::fromNetwork
        );

        @Override
        public MapCodec<FoodValuesDefinition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FoodValuesDefinition> streamCodec() {
            return STREAM_CODEC;
        }

        private static FoodValuesDefinition fromNetwork(RegistryFriendlyByteBuf buffer) {
            var isItem = buffer.readBoolean();
            var names = new HashSet<ResourceLocation>();
            var length = buffer.readVarInt();
            for (var i = 0; i < length; i++) {
                names.add(buffer.readResourceLocation());
            }
            var foodValues = FoodValues.STREAM_CODEC.decode(buffer);
            return new FoodValuesDefinition(names, foodValues, isItem);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, FoodValuesDefinition recipe) {
            buffer.writeBoolean(recipe.isItem());
            buffer.writeVarInt(recipe.getNames().size());
            recipe.getNames().forEach(buffer::writeResourceLocation);
            FoodValues.STREAM_CODEC.encode(buffer, recipe.foodValues);
        }
    }
}
