package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.gson.JsonParseException;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.core.FoodValues;
import com.sihenzhang.crockpot.registry.FoodCategory;
import com.sihenzhang.crockpot.util.RecipeUtil;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;

import javax.annotation.Nonnull;
import java.util.*;

public class FoodValuesDefinition extends AbstractRecipe<SingleRecipeInput> {
    public static final MapCodec<FoodValuesDefinition> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
            Identifier.CODEC.listOf().optionalFieldOf("items", List.of()).forGetter(def -> def.item ? List.copyOf(def.names) : List.of()),
            Identifier.CODEC.listOf().optionalFieldOf("tags", List.of()).forGetter(def -> def.item ? List.of() : List.copyOf(def.names)),
            FoodValues.CODEC.fieldOf("values").forGetter(FoodValuesDefinition::getFoodValues)
    ).apply(instance, FoodValuesDefinition::fromFields));
    public static final StreamCodec<RegistryFriendlyByteBuf, FoodValuesDefinition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.BOOL,
            FoodValuesDefinition::isItem,
            Identifier.STREAM_CODEC.apply(ByteBufCodecs.list()),
            def -> List.copyOf(def.names),
            ByteBufCodecs.fromCodecWithRegistries(FoodValues.CODEC),
            FoodValuesDefinition::getFoodValues,
            FoodValuesDefinition::new
    );

    private final Set<Identifier> names;
    private final FoodValues foodValues;
    private final boolean item;

    public FoodValuesDefinition(Set<Identifier> names, FoodValues foodValues, boolean item) {
        this.names = ImmutableSet.copyOf(names);
        this.foodValues = foodValues;
        this.item = item;
    }

    private FoodValuesDefinition(boolean item, List<Identifier> names, FoodValues foodValues) {
        this(new HashSet<>(names), foodValues, item);
    }

    private static FoodValuesDefinition fromFields(List<Identifier> items, List<Identifier> tags, FoodValues foodValues) {
        if (!items.isEmpty() && !tags.isEmpty()) {
            throw new JsonParseException("A food value definition entry needs either tags or items, not both");
        }
        if (!items.isEmpty()) {
            return new FoodValuesDefinition(new HashSet<>(items), foodValues, true);
        }
        if (!tags.isEmpty()) {
            return new FoodValuesDefinition(new HashSet<>(tags), foodValues, false);
        }
        throw new JsonParseException("A food value definition entry needs either tags or items");
    }

    public Set<Identifier> getNames() {
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
    public ItemStack assemble(SingleRecipeInput pContainer) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public static FoodValues getFoodValues(ItemStack stack, Level level) {
        var allDefs = RecipeUtil.getRecipesFor(ModRecipes.FOOD_VALUES_RECIPE_TYPE.get(), new SingleRecipeInput(stack), level)
                .map(RecipeHolder::value)
                .toList();
        if (allDefs.isEmpty()) {
            return FoodValues.create();
        }
        return allDefs.stream().filter(FoodValuesDefinition::isItem).findFirst().map(FoodValuesDefinition::getFoodValues).orElseGet(() -> {
            var foodValues = FoodValues.create();
            var maxCount = -1L;
            var tagDefs = new HashMap<Identifier, FoodValues>();
            allDefs.forEach(def -> def.getNames().forEach(name -> tagDefs.put(name, def.getFoodValues())));
            for (var tag : stack.typeHolder().tags().map(TagKey::location).filter(tagDefs::containsKey).toList()) {
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

    public static Set<ItemStack> getMatchedItems(Holder<FoodCategory> category, Level level) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return Set.of();
        }
        var builder = ImmutableSortedSet.orderedBy(
                Comparator.comparing((ItemStack stack) -> getFoodValues(stack, serverLevel).get(category))
                        .thenComparing(stack -> BuiltInRegistries.ITEM.getKey(stack.getItem()),
                                Comparator.comparing((Identifier key) -> !"minecraft".equals(key.getNamespace()))
                                        .thenComparing(key -> !CrockPot.MOD_ID.equals(key.getNamespace()))
                                        .thenComparing(Comparator.naturalOrder())
                        )
        );
        var allDefs = serverLevel.recipeAccess().recipeMap().byType(ModRecipes.FOOD_VALUES_RECIPE_TYPE.get()).stream()
                .map(RecipeHolder::value)
                .filter(def -> def.getFoodValues().has(category))
                .toList();
        allDefs.stream().filter(FoodValuesDefinition::isItem).forEach(itemDef -> itemDef.getNames().forEach(name -> {
            var item = BuiltInRegistries.ITEM.getValue(name);
            if (item != null && item != Items.AIR) {
                builder.add(item.getDefaultInstance());
            }
        }));
        allDefs.stream().filter(def -> !def.isItem()).forEach(tagDef -> tagDef.getNames().forEach(name -> {
            var tag = ItemTags.create(name);
            BuiltInRegistries.ITEM.get(tag).ifPresent(holders -> holders.stream().forEach(holder -> {
                var stack = holder.value().getDefaultInstance();
                if (getFoodValues(stack, serverLevel).has(category)) {
                    builder.add(stack);
                }
            }));
        }));
        return builder.build();
    }

    @Override
    @Nonnull
    public RecipeSerializer<? extends FoodValuesDefinition> getSerializer() {
        return ModRecipes.FOOD_VALUES_RECIPE_SERIALIZER.get();
    }

    @Override
    @Nonnull
    public RecipeType<? extends FoodValuesDefinition> getType() {
        return ModRecipes.FOOD_VALUES_RECIPE_TYPE.get();
    }
}
