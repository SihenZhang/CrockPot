package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
                    if (itemDef.containsKey(def.item)) {
                        throw new RuntimeException("Duplicate item definition");
                    }
                    itemDef.put(def.item, def);
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
    protected void apply(Map<ResourceLocation, JsonObject> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        LOGGER.info("Start loading food categories");
        Map<Item, CategoryDefinitionItem> itemDef = new HashMap<>(16);
        Map<String, CategoryDefinitionTag> tagDef = new HashMap<>(16);

        for (Map.Entry<ResourceLocation, JsonObject> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            try {
                JsonObject o = entry.getValue();
                switch (Objects.requireNonNull(JSONUtils.getString(o, "type"))) {
                    case "item": {
                        CategoryDefinitionItem def = GSON_INSTANCE.fromJson(o, CategoryDefinitionItem.class);
                        if (itemDef.containsKey(def.item)) {
                            throw new IllegalArgumentException("Duplicate definition for item " + def.item.getRegistryName());
                        }
                        itemDef.put(def.item, def);
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
