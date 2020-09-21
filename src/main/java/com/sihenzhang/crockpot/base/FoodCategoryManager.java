package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.stream.Collectors;

@ParametersAreNonnullByDefault
public final class FoodCategoryManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder()
            .registerTypeAdapter(CategoryDefinitionItem.class, new CategoryDefinitionItem.Serializer())
            .registerTypeAdapter(CategoryDefinitionTag.class, new CategoryDefinitionTag.Serializer())
            .registerTypeAdapter(
                    new TypeToken<EnumMap<FoodCategory, Float>>() {
                    }.getType(),
                    new Utils.EnumMapInstanceCreator<>(FoodCategory.class)
            )
            .create();
    private static final Logger LOGGER = LogManager.getLogger();
    private Map<Item, CategoryDefinitionItem> itemDef = ImmutableMap.of();
    private Map<String, CategoryDefinitionTag> tagDef = ImmutableMap.of();

    public FoodCategoryManager() {
        super(GSON_INSTANCE, "crock_pot_food_values");
    }

    public EnumMap<FoodCategory, Float> valuesOf(Item item) {
        if (itemDef.containsKey(item)) {
            return itemDef.get(item).getValues();
        }
        List<String> tags = item.getTags().stream().map(ResourceLocation::toString)
                .sorted(Comparator.comparingLong((String e) -> e.chars().filter(i -> i == '/').count()).reversed())
                .collect(Collectors.toList());
        for (String tag : tags) {
            if (tagDef.containsKey(tag)) {
                return tagDef.get(tag).getValues();
            }
        }
        return null;
    }

    public String serialize() {
        JsonArray defList = new JsonArray();

        itemDef.values().forEach(def -> {
            JsonObject o = new JsonObject();
            o.addProperty("type", "item");
            o.addProperty("item", Objects.requireNonNull(def.item.getRegistryName()).toString());
            o.add("values", GSON_INSTANCE.toJsonTree(def.getValues()));
            defList.add(o);
        });

        tagDef.values().forEach(def -> {
            JsonObject o = new JsonObject();
            o.addProperty("type", "tag");
            o.addProperty("tag", def.tag);
            o.add("values", GSON_INSTANCE.toJsonTree(def.getValues()));
            defList.add(o);
        });

        return defList.toString();
    }

    public void deserialize(String str) {
        JsonArray array = GSON_INSTANCE.fromJson(str, JsonArray.class);
        Map<Item, CategoryDefinitionItem> itemDef = new HashMap<>(16);
        Map<String, CategoryDefinitionTag> tagDef = new HashMap<>(16);
        for (JsonElement o : array) {
            JsonObject cast = o.getAsJsonObject();
            switch (Objects.requireNonNull(JSONUtils.getString(cast, "type"))) {
                case "item": {
                    Item item = JSONUtils.getItem(cast, "item");
                    if (itemDef.containsKey(item)) {
                        throw new RuntimeException("Duplicate item definition");
                    }
                    EnumMap<FoodCategory, Float> values = GSON_INSTANCE.fromJson(cast.get("values"), new TypeToken<EnumMap<FoodCategory, Float>>() {
                    }.getType());
                    CategoryDefinitionItem def = new CategoryDefinitionItem(item, values);
                    itemDef.put(item, def);
                    break;
                }
                case "tag": {
                    String tag = JSONUtils.getString(cast, "tag");
                    if (tagDef.containsKey(tag)) {
                        throw new RuntimeException("Duplicate tag definition");
                    }
                    EnumMap<FoodCategory, Float> values = GSON_INSTANCE.fromJson(cast.get("values"), new TypeToken<EnumMap<FoodCategory, Float>>() {
                    }.getType());
                    CategoryDefinitionTag def = new CategoryDefinitionTag(tag, values);
                    tagDef.put(tag, def);
                    break;
                }
                default:
            }
        }
        this.itemDef = ImmutableMap.copyOf(itemDef);
        this.tagDef = ImmutableMap.copyOf(tagDef);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        LOGGER.info("Start loading food categories");
        Map<Item, CategoryDefinitionItem> itemDef = new HashMap<>(16);
        Map<String, CategoryDefinitionTag> tagDef = new HashMap<>(16);

        for (Map.Entry<ResourceLocation, JsonObject> entry : objectIn.entrySet()) {
            ResourceLocation rl = entry.getKey();
            if (rl.getPath().startsWith("_")) {
                continue;
            }
            try {
                JsonObject o = entry.getValue();
                switch (Objects.requireNonNull(JSONUtils.getString(o, "type"))) {
                    case "item": {
                        Item item = JSONUtils.getItem(o, "item");
                        if (itemDef.containsKey(item)) {
                            throw new IllegalArgumentException("Duplicate definition for item " + item.getRegistryName());
                        }
                        EnumMap<FoodCategory, Float> values = GSON_INSTANCE.fromJson(o.get("values"), new TypeToken<EnumMap<FoodCategory, Float>>() {
                        }.getType());
                        CategoryDefinitionItem def = new CategoryDefinitionItem(item, values);
                        itemDef.put(item, def);
                        continue;
                    }
                    case "tag": {
                        String tag = JSONUtils.getString(o, "tag");
                        if (tagDef.containsKey(tag)) {
                            throw new IllegalArgumentException("Duplicate definition for tag: " + tag);
                        }
                        EnumMap<FoodCategory, Float> values = GSON_INSTANCE.fromJson(o.get("values"), new TypeToken<EnumMap<FoodCategory, Float>>() {
                        }.getType());
                        CategoryDefinitionTag def = new CategoryDefinitionTag(tag, values);
                        tagDef.put(tag, def);
                        continue;
                    }
                    default: {
                        throw new IllegalArgumentException("Invalid definition type");
                    }
                }
            } catch (Exception ex) {
                LOGGER.error("error loading " + rl.toString());
                throw new RuntimeException(ex);
            }
        }

        this.itemDef = ImmutableMap.copyOf(itemDef);
        this.tagDef = ImmutableMap.copyOf(tagDef);

        LOGGER.info("Categories loading complete.");
    }
}
