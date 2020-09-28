package com.sihenzhang.crockpot.base;

import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.sihenzhang.crockpot.network.NetworkManager;
import com.sihenzhang.crockpot.network.PacketSyncCrockpotIngredients;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.PacketDistributor;
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

    public String serialize() {
        JsonArray jsonArray = new JsonArray();
        ingredients.values().forEach(i -> jsonArray.add(GSON_INSTANCE.toJson(i)));
        return jsonArray.toString();
    }

    public void deserialize(String str) {
        Map<Item, CrockPotIngredient> map = new HashMap<>(16);
        JsonArray jsonArray = GSON_INSTANCE.fromJson(str, JsonArray.class);
        jsonArray.forEach(s -> {
            CrockPotIngredient ingredient = GSON_INSTANCE.fromJson(s.getAsString(), CrockPotIngredient.class);
            map.put(ingredient.getItem(), ingredient);
        });
        this.ingredients = ImmutableMap.copyOf(map);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonObject> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        Map<Item, CrockPotIngredient> output = new HashMap<>(16);
        for (Map.Entry<ResourceLocation, JsonObject> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
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
        NetworkManager.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCrockpotIngredients(this.serialize()));
        LOGGER.info("Loaded {} crock pot ingredients", ingredients.size());
    }
}