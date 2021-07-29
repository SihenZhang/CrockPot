package com.sihenzhang.crockpot.base;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedSet;
import com.google.gson.*;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public final class FoodCategoryManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(CategoryDefinitionItem.class, new CategoryDefinitionItem.Serializer())
            .registerTypeAdapter(CategoryDefinitionTag.class, new CategoryDefinitionTag.Serializer())
            .create();
    private static final Logger LOGGER = LogManager.getLogger();
    private Map<Item, CategoryDefinitionItem> itemDef = ImmutableMap.of();
    private Map<String, CategoryDefinitionTag> tagDef = ImmutableMap.of();

    public FoodCategoryManager() {
        super(GSON_INSTANCE, "crock_pot_food_values");
    }

    @Nonnull
    public FoodValues getFoodValues(Item item) {
        if (itemDef.containsKey(item)) {
            return itemDef.get(item).getFoodValues();
        }
        boolean isEmpty = true;
        FoodValues foodValues = FoodValues.create();
        long maxCount = -1L;
        for (ResourceLocation tag : item.getTags()) {
            String tagName = tag.toString();
            if (tagDef.containsKey(tagName)) {
                long count = tagName.chars().filter(c -> c == '/').count();
                if (count < maxCount) {
                    continue;
                }
                if (count > maxCount) {
                    maxCount = count;
                    foodValues.clear();
                }
                isEmpty = false;
                tagDef.get(tagName).getFoodValues().entrySet().forEach(entry -> foodValues.put(entry.getKey(), Math.max(foodValues.get(entry.getKey()), entry.getValue())));
            }
        }
        return isEmpty ? FoodValues.EMPTY : foodValues;
    }

    @Nonnull
    public Set<Item> getMatchedItems(FoodCategory category, float value) {
        ImmutableSortedSet.Builder<Item> builder = ImmutableSortedSet.orderedBy((o1, o2) -> {
            ResourceLocation r1 = o1.getRegistryName();
            ResourceLocation r2 = o2.getRegistryName();
            String n1 = Objects.requireNonNull(r1).getNamespace();
            String n2 = Objects.requireNonNull(r2).getNamespace();
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
        });
        // make vanilla items and Crock Pot mod items at the top of the collection
        itemDef.forEach((item, categoryDefinitionItem) -> {
            if (MathUtils.fuzzyEquals(categoryDefinitionItem.getFoodValues().get(category), value)) {
                builder.add(item);
            }
        });
        tagDef.forEach((tag, categoryDefinitionTag) -> {
            // determine whether the tag itself meets the condition
            if (MathUtils.fuzzyEquals(categoryDefinitionTag.getFoodValues().get(category), value)) {
                ITag<Item> itag = TagCollectionManager.getInstance().getItems().getTag(new ResourceLocation(tag));
                if (itag != null) {
                    // get all items with the tag
                    Ingredient.IItemList tagList = new Ingredient.TagList(itag);
                    tagList.getItems().forEach(stack -> {
                        Item item = stack.getItem();
                        // use getFoodValues method to make sure there's no higher priority definition
                        if (MathUtils.fuzzyEquals(getFoodValues(item).get(category), value)) {
                            builder.add(item);
                        }
                    });
                }
            }
        });
        return builder.build();
    }

    public List<FoodCategoryMatchedItems> getFoodCategoryMatchedItemsList() {
        ImmutableList.Builder<FoodCategoryMatchedItems> foodCategoryToItemsBuilder = ImmutableList.builder();
        for (FoodCategory category : FoodCategory.values()) {
            ImmutableSortedSet.Builder<Item> builder = ImmutableSortedSet.orderedBy((o1, o2) -> {
                ResourceLocation r1 = o1.getRegistryName();
                ResourceLocation r2 = o2.getRegistryName();
                String n1 = Objects.requireNonNull(r1).getNamespace();
                String n2 = Objects.requireNonNull(r2).getNamespace();
                float v1 = getFoodValues(o1).get(category);
                float v2 = getFoodValues(o2).get(category);
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
            // make vanilla items and Crock Pot mod items at the top of the collection
            this.itemDef.forEach((item, categoryDefinitionItem) -> {
                if (categoryDefinitionItem.getFoodValues().has(category)) {
                    builder.add(item);
                }
            });
            tagDef.forEach((tag, categoryDefinitionTag) -> {
                // determine whether the tag itself meets the condition
                if (categoryDefinitionTag.getFoodValues().has(category)) {
                    ITag<Item> itag = TagCollectionManager.getInstance().getItems().getTag(new ResourceLocation(tag));
                    if (itag != null) {
                        // get all items with the tag
                        Ingredient.IItemList tagList = new Ingredient.TagList(itag);
                        tagList.getItems().forEach(stack -> {
                            Item item = stack.getItem();
                            // use getFoodValues method to make sure there's no higher priority definition
                            if (getFoodValues(item).has(category)) {
                                builder.add(item);
                            }
                        });
                    }
                }
            });
            foodCategoryToItemsBuilder.add(new FoodCategoryMatchedItems(category, builder.build()));
        }
        return foodCategoryToItemsBuilder.build();
    }

    public String serialize() {
        JsonArray defList = new JsonArray();

        itemDef.values().forEach(def -> {
            JsonObject o = GSON_INSTANCE.toJsonTree(def).getAsJsonObject();
            o.addProperty("type", "item");
            defList.add(o);
        });

        tagDef.values().forEach(def -> {
            JsonObject o = GSON_INSTANCE.toJsonTree(def).getAsJsonObject();
            o.addProperty("type", "tag");
            defList.add(o);
        });

        return defList.toString();
    }

    public void deserialize(String str) {
        JsonArray array = GSON_INSTANCE.fromJson(str, JsonArray.class);
        Map<Item, CategoryDefinitionItem> itemDef = new HashMap<>(16);
        Map<String, CategoryDefinitionTag> tagDef = new HashMap<>(16);
        for (JsonElement e : array) {
            JsonObject o = e.getAsJsonObject();
            switch (Objects.requireNonNull(JSONUtils.getAsString(o, "type"))) {
                case "item": {
                    CategoryDefinitionItem def = GSON_INSTANCE.fromJson(o, CategoryDefinitionItem.class);
                    // Skip not registered items
                    if (def.item != null) {
                        if (itemDef.containsKey(def.item)) {
                            throw new RuntimeException("Duplicate item definition");
                        }
                        itemDef.put(def.item, def);
                    }
                    break;
                }
                case "tag": {
                    CategoryDefinitionTag def = GSON_INSTANCE.fromJson(o, CategoryDefinitionTag.class);
                    if (tagDef.containsKey(def.tag)) {
                        throw new RuntimeException("Duplicate tag definition");
                    }
                    tagDef.put(def.tag, def);
                    break;
                }
                default:
            }
        }
        this.itemDef = ImmutableMap.copyOf(itemDef);
        this.tagDef = ImmutableMap.copyOf(tagDef);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        LOGGER.info("Start loading food categories");
        Stopwatch stopwatch = Stopwatch.createStarted();

        Map<Item, CategoryDefinitionItem> itemDef = new HashMap<>(16);
        Map<String, CategoryDefinitionTag> tagDef = new HashMap<>(16);

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            try {
                JsonObject o = entry.getValue().getAsJsonObject();
                switch (Objects.requireNonNull(JSONUtils.getAsString(o, "type"))) {
                    case "item": {
                        CategoryDefinitionItem def = GSON_INSTANCE.fromJson(o, CategoryDefinitionItem.class);
                        // Skip unregistered items
                        if (def.item != null) {
                            if (itemDef.containsKey(def.item)) {
                                throw new IllegalArgumentException("Duplicate definition for item " + def.item.getRegistryName());
                            }
                            itemDef.put(def.item, def);
                        }
                        break;
                    }
                    case "tag": {
                        CategoryDefinitionTag def = GSON_INSTANCE.fromJson(o, CategoryDefinitionTag.class);
                        if (tagDef.containsKey(def.tag)) {
                            throw new IllegalArgumentException("Duplicate definition for tag: " + def.tag);
                        }
                        tagDef.put(def.tag, def);
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException("Invalid definition type");
                    }
                }
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading crock pot food category {}", resourceLocation, exception);
            }
        }

        this.itemDef = ImmutableMap.copyOf(itemDef);
        this.tagDef = ImmutableMap.copyOf(tagDef);

        stopwatch.stop();
        LOGGER.info("Categories loading complete in {}.", stopwatch);
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
}
