package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.registry.FoodCategory;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.*;

public class FoodValuesDefinition extends AbstractRecipe<SingleRecipeInput> {
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
    public boolean matches(SingleRecipeInput pContainer, Level pLevel) {
        var stack = pContainer.getItem(0);
        return item ? names.stream().anyMatch(name -> name.equals(BuiltInRegistries.ITEM.getKey(stack.getItem()))) :
                names.stream().anyMatch(name -> stack.is(ItemTags.create(name)));
    }

    @Override
    public ItemStack assemble(SingleRecipeInput pContainer, HolderLookup.Provider pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public static FoodValues getFoodValues(ItemStack stack, Level level) {
        var allDefs = level.getRecipeManager().getRecipesFor(CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get(), new SingleRecipeInput(stack), level).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).toList();
        if (allDefs.isEmpty()) {
            return FoodValues.create();
        }
        return allDefs.stream().filter(FoodValuesDefinition::isItem).findFirst().map(FoodValuesDefinition::getFoodValues).orElseGet(() -> {
            var foodValues = FoodValues.create();
            var maxCount = -1L;
            var tagDefs = new HashMap<ResourceLocation, FoodValues>();
            allDefs.forEach(def -> def.getNames().forEach(name -> tagDefs.put(name, def.getFoodValues())));
            for (var tag : stack.getTags().map(TagKey::location).filter(tagDefs::containsKey).toList()) {
                var count = getTagSpecificity(tag);
                if (count < maxCount) {
                    continue;
                }
                if (count > maxCount) {
                    maxCount = count;
                    foodValues.clear();
                }
                tagDefs.get(tag).entrySet().forEach(entry -> foodValues.put(entry.getKey(),
                        Math.max(foodValues.get(entry.getKey()), entry.getFloatValue())));
            }
            return foodValues;
        });
    }

    private static long getTagSpecificity(ResourceLocation tag) {
        var path = tag.getPath();
        // NeoForge added foods/ to these common tags; keep their original relative priority.
        if ("c".equals(tag.getNamespace()) && path.startsWith("foods/")) {
            var foodPath = path.substring("foods/".length());
            var separator = foodPath.indexOf('/');
            var category = separator < 0 ? foodPath : foodPath.substring(0, separator);
            if (Set.of("vegetable", "fruit", "berry", "raw_fish", "cooked_fish").contains(category)) {
                path = foodPath;
            }
        }
        return path.chars().filter(c -> c == '/').count();
    }

    public static Set<ItemStack> getMatchedItems(Holder<FoodCategory> category, Level level) {
        // make vanilla items and Crock Pot mod items at the top of the collection
        var builder = ImmutableSortedSet.orderedBy(
                Comparator.comparing((ItemStack stack) -> getFoodValues(stack, level).get(category))
                        .thenComparing(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()),
                                Comparator.comparing((ResourceLocation key) -> !"minecraft".equals(key.getNamespace()))
                                        .thenComparing(key -> !CrockPot.MOD_ID.equals(key.getNamespace()))
                                        .thenComparing(Comparator.naturalOrder())
                        )
        );
        var allDefs = level.getRecipeManager().getAllRecipesFor(CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get()).stream().map(net.minecraft.world.item.crafting.RecipeHolder::value).filter(def -> def.getFoodValues().has(category)).toList();
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
        return CrockPotRecipes.FOOD_VALUES_RECIPE_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public RecipeType<?> getType() {
        return CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<FoodValuesDefinition> {
        private static final MapCodec<FoodValuesDefinition> CODEC = RecordCodecBuilder.<Fields>mapCodec(instance -> instance.group(
                ResourceLocation.CODEC.listOf().optionalFieldOf("items").forGetter(Fields::items),
                ResourceLocation.CODEC.listOf().optionalFieldOf("tags").forGetter(Fields::tags),
                FoodValues.CODEC.fieldOf("values").forGetter(Fields::values)
        ).apply(instance, Fields::new)).flatXmap(Serializer::fromFields, recipe -> DataResult.success(new Fields(
                recipe.isItem() ? Optional.of(List.copyOf(recipe.names)) : Optional.empty(),
                recipe.isItem() ? Optional.empty() : Optional.of(List.copyOf(recipe.names)),
                recipe.getFoodValues()
        )));
        private static final StreamCodec<RegistryFriendlyByteBuf, FoodValuesDefinition> STREAM_CODEC = StreamCodec.of(Serializer::toNetwork, Serializer::fromNetwork);

        @Override
        public MapCodec<FoodValuesDefinition> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, FoodValuesDefinition> streamCodec() {
            return STREAM_CODEC;
        }

        private record Fields(Optional<List<ResourceLocation>> items, Optional<List<ResourceLocation>> tags,
                              FoodValues values) {
        }

        private static DataResult<FoodValuesDefinition> fromFields(Fields fields) {
            if (fields.items().isPresent() && fields.tags().isPresent()) {
                return DataResult.error(() -> "A food value definition entry needs either tags or items, not both");
            }
            if (fields.items().isEmpty() && fields.tags().isEmpty()) {
                return DataResult.error(() -> "A food value definition entry needs either tags or items");
            }
            var isItem = fields.items().isPresent();
            var names = isItem ? fields.items().orElseThrow() : fields.tags().orElseThrow();
            return DataResult.success(new FoodValuesDefinition(new HashSet<>(names), fields.values(), isItem));
        }

        private static FoodValuesDefinition fromNetwork(RegistryFriendlyByteBuf buffer) {
            var isItem = buffer.readBoolean();
            var names = new HashSet<ResourceLocation>();
            var length = buffer.readVarInt();
            for (var i = 0; i < length; i++) {
                names.add(buffer.readResourceLocation());
            }
            var foodValues = FoodValues.fromNetwork(buffer);
            return new FoodValuesDefinition(names, foodValues, isItem);
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, FoodValuesDefinition recipe) {
            buffer.writeBoolean(recipe.isItem());
            buffer.writeVarInt(recipe.getNames().size());
            recipe.getNames().forEach(buffer::writeResourceLocation);
            recipe.getFoodValues().toNetwork(buffer);
        }
    }
}
