package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
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
            .create();
    private static final Logger LOGGER = LogManager.getLogger();
    private Map<Item, CategoryDefinitionItem> itemDef = ImmutableMap.of();
    private Map<String, CategoryDefinitionTag> tagDef = ImmutableMap.of();

    public FoodCategoryManager() {
        super(GSON_INSTANCE, "crock_pot_ingredient");
    }

    public EnumMap<FoodCategory, Float> valuesOf(Item item) {
        if (itemDef.containsKey(item)) return itemDef.get(item).getValues();
        List<String> tags = item.getTags().stream().map(ResourceLocation::toString)
                .sorted(Comparator.comparingInt(e -> (int) e.chars().filter(i -> i == '/').count()))
                .collect(Collectors.toList());
        for (String tag : tags) {
            if (tagDef.containsKey(tag)) return tagDef.get(tag).getValues();
        }
        return null;
    }

    public String serialize() {
        // TODO
        return "";
    }

    public void deserialize(String str) {
        // TODO
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        // TODO
//        Map<Item, CategoryDefinitionItem> output = new HashMap<>(16);
//        for (Map.Entry<ResourceLocation, JsonObject> entry : objectIn.entrySet()) {
//            ResourceLocation resourceLocation = entry.getKey();
//            if (resourceLocation.getPath().startsWith("_")) {
//                continue;
//            }
//            try {
//                CategoryDefinitionItem ingredient = GSON_INSTANCE.fromJson(entry.getValue(), CategoryDefinitionItem.class);
//                if (ingredient != null && ingredient.getItem() != Items.AIR) {
//                    output.put(ingredient.getItem(), ingredient);
//                }
//            } catch (IllegalArgumentException | JsonParseException exception) {
//                LOGGER.error("Parsing error loading crock pot ingredient {}", resourceLocation, exception);
//            }
//        }
//        ingredients = ImmutableMap.copyOf(output);
//        NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCrockpotIngredients(this.serialize()));
//        LOGGER.info("Loaded {} crock pot ingredients", ingredients.size());
    }
}
