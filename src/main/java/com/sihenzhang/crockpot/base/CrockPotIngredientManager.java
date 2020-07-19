package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public class CrockPotIngredientManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = (new GsonBuilder()).registerTypeAdapter(CrockPotIngredient.class, new CrockPotIngredient.Serializer()).create();
    private static final Logger LOGGER = LogManager.getLogger();
    private List<CrockPotIngredient> ingredients = ImmutableList.of();

    public CrockPotIngredientManager() {
        super(GSON_INSTANCE, "crock_pot_ingredient");
    }

    public CrockPotIngredient getIngredientFromItem(Item item) {
        for (CrockPotIngredient ingredient : ingredients) {
            if (item == ingredient.getItem()) {
                return ingredient;
            }
        }
        return null;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        List<CrockPotIngredient> output = new ArrayList<>();
        for (Map.Entry<ResourceLocation, JsonObject> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_"))
                continue;
            try {
                CrockPotIngredient ingredient = GSON_INSTANCE.fromJson(entry.getValue(), CrockPotIngredient.class);
                output.removeIf(ing -> ingredient.getItem() == ing.getItem());
                output.add(ingredient);
            } catch (IllegalArgumentException | JsonParseException jsonparseexception) {
                LOGGER.error("Parsing error loading crock pot ingredient {}", resourceLocation, jsonparseexception);
            }
        }
        ingredients = ImmutableList.copyOf(output);
        LOGGER.info("Loaded {} crock pot ingredients", ingredients.size());
    }
}
