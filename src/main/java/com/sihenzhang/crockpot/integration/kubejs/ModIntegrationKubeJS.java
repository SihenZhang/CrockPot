package com.sihenzhang.crockpot.integration.kubejs;

import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeTypesEvent;
import net.minecraft.resources.ResourceLocation;

public class ModIntegrationKubeJS extends KubeJSPlugin {
    @Override
    public void registerRecipeTypes(RegisterRecipeTypesEvent event) {
        event.register(new ResourceLocation("crockpot:crock_pot_cooking"), CrockPotCookingRecipeJS::new);
        event.register(new ResourceLocation("crockpot:explosion_crafting"), ExplosionCraftingRecipeJS::new);
        event.register(new ResourceLocation("crockpot:food_values"), FoodValuesDefinitionJS::new);
        event.register(new ResourceLocation("crockpot:piglin_bartering"), PiglinBarteringRecipeJS::new);
    }
}
