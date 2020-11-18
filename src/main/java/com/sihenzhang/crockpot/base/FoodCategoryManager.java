package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.sihenzhang.crockpot.CrockPot;
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
    public EnumMap<FoodCategory, Float> valuesOf(Item item) {
        if (itemDef.containsKey(item)) {
            return itemDef.get(item).getValues();
        }
        EnumMap<FoodCategory, Float> values = new EnumMap<>(FoodCategory.class);
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
                    values.clear();
                }
                for (Map.Entry<FoodCategory, Float> category : tagDef.get(tagName).getValues().entrySet()) {
                    values.put(category.getKey(), Math.max(values.getOrDefault(category.getKey(), 0F), category.getValue()));
                }
            }
        }
        return values;
    }

    @Nonnull
    public Collection<Item> getMatchingItems(FoodCategory category, float value) {
        // make vanilla items and Crock Pot mod items at the top of the collection
        SortedSet<Item> items = new TreeSet<>((o1, o2) -> {
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
        itemDef.forEach((item, categoryDefinitionItem) -> {
            if (categoryDefinitionItem.getValues().getOrDefault(category, 0F) == value) {
                items.add(item);
            }
        });
        tagDef.forEach((tag, categoryDefinitionTag) -> {
            // determine whether the tag itself meets the condition
            if (categoryDefinitionTag.getValues().getOrDefault(category, 0F) == value) {
                ITag<Item> itag = TagCollectionManager.getManager().getItemTags().get(new ResourceLocation(tag));
                if (itag != null) {
                    // get all items with the tag
                    Ingredient.IItemList tagList = new Ingredient.TagList(itag);
                    tagList.getStacks().forEach(stack -> {
                        Item item = stack.getItem();
                        // use valuesOf method to make sure there's no higher priority definition
                        if (valuesOf(item).getOrDefault(category, 0F) == value) {
                            items.add(item);
                        }
                    });
                }
            }
        });
        return items;
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
            switch (Objects.requireNonNull(JSONUtils.getString(o, "type"))) {
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
        Map<Item, CategoryDefinitionItem> itemDef = new HashMap<>(16);
        Map<String, CategoryDefinitionTag> tagDef = new HashMap<>(16);

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            try {
                JsonObject o = entry.getValue().getAsJsonObject();
                switch (Objects.requireNonNull(JSONUtils.getString(o, "type"))) {
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

        LOGGER.info("Categories loading complete.");
    }
}
