package com.sihenzhang.crockpot.integration.kubejs;

import dev.latvian.kubejs.KubeJSPlugin;
import dev.latvian.kubejs.recipe.RegisterRecipeHandlersEvent;

public class ModIntegrationKubeJS extends KubeJSPlugin {
    @Override
    public void addRecipes(RegisterRecipeHandlersEvent event) {
        event.register("crockpot:crock_pot_cooking", CrockPotCookingRecipeJS::new);
        event.register("crockpot:explosion_crafting", ExplosionCraftingRecipeJS::new);
        event.register("crockpot:food_values", FoodValuesDefinitionJS::new);
        event.register("crockpot:piglin_bartering", PiglinBarteringRecipeJS::new);
    }
}
