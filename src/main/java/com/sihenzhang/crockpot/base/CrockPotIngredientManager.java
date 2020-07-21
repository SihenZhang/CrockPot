package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;

@ParametersAreNonnullByDefault
public class CrockPotIngredientManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(CrockPotIngredient.class, new CrockPotIngredient.Serializer()).create();
    private static final Logger LOGGER = LogManager.getLogger();
    private Map<Item, CrockPotIngredient> ingredients = ImmutableMap.of();

    public CrockPotIngredientManager() {
        super(GSON_INSTANCE, "crock_pot_ingredient");
    }

    public CrockPotIngredient getIngredientFromItem(Item item) {
        return ingredients.get(item);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<Item, CrockPotIngredient> output = new HashMap<>();
        for (Map.Entry<ResourceLocation, JsonObject> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_"))
                continue;
            try {
                CrockPotIngredient ingredient = GSON_INSTANCE.fromJson(entry.getValue(), CrockPotIngredient.class);
                if (ingredient != null && ingredient.getItem() != Items.AIR) {
                    output.put(ingredient.getItem(), ingredient);
                }
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading crock pot ingredient {}", resourceLocation, exception);
            }
        }
        ingredients = ImmutableMap.copyOf(output);
        LOGGER.info("Loaded {} crock pot ingredients", ingredients.size());
    }
}
