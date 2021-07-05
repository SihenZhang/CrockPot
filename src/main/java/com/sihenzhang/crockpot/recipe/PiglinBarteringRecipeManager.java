package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableList;
import com.google.gson.*;
import net.minecraft.client.resources.JsonReloadListener;
import net.minecraft.item.ItemStack;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PiglinBarteringRecipeManager extends JsonReloadListener {
    private static final Gson GSON_INSTANCE = new GsonBuilder().registerTypeAdapter(PiglinBarteringRecipe.class, new PiglinBarteringRecipe.Serializer()).create();
    private static final Logger LOGGER = LogManager.getLogger();
    private List<PiglinBarteringRecipe> recipes = ImmutableList.of();

    public PiglinBarteringRecipeManager() {
        super(GSON_INSTANCE, "piglin_bartering");
    }

    public List<PiglinBarteringRecipe> getRecipes() {
        return recipes;
    }

    public PiglinBarteringRecipe matches(ItemStack stack) {
        return recipes.stream().filter(r -> r.test(stack)).findFirst().orElse(null);
    }

    public String serialize() {
        JsonArray recipeList = new JsonArray();
        recipes.forEach(recipe -> {
            recipeList.add(GSON_INSTANCE.toJsonTree(recipe).getAsJsonObject());
        });
        return recipeList.toString();
    }

    public void deserialize(String str) {
        JsonArray array = GSON_INSTANCE.fromJson(str, JsonArray.class);
        List<PiglinBarteringRecipe> recipes = new ArrayList<>();
        for (JsonElement e : array) {
            JsonObject o = e.getAsJsonObject();
            PiglinBarteringRecipe recipe = GSON_INSTANCE.fromJson(o, PiglinBarteringRecipe.class);
            recipes.add(recipe);
        }
        this.recipes = recipes;
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {
        List<PiglinBarteringRecipe> recipes = new ArrayList<>();

        for (Map.Entry<ResourceLocation, JsonElement> entry : objectIn.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            if (resourceLocation.getPath().startsWith("_")) {
                continue;
            }
            try {
                PiglinBarteringRecipe recipe = GSON_INSTANCE.fromJson(entry.getValue(), PiglinBarteringRecipe.class);
                recipes.add(recipe);
            } catch (IllegalArgumentException | JsonParseException exception) {
                LOGGER.error("Parsing error loading piglin bartering recipe {}", resourceLocation, exception);
            }
        }

        this.recipes = ImmutableList.copyOf(recipes);

        LOGGER.info("Loaded {} piglin bartering recipes", recipes.size());
    }
}
