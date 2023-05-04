package com.sihenzhang.crockpot.integration.patchouli;

import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.util.StringUtils;
import net.minecraft.network.chat.TranslatableComponent;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class ProcessorExplosionCrafting implements IComponentProcessor {
    private ExplosionCraftingRecipe recipe;

    @Override
    public void setup(IVariableProvider variables) {
        recipe = PatchouliUtils.getRecipe(variables.get("recipe").asString());
    }

    @Override
    public IVariable process(String key) {
        return switch (key) {
            case "input" -> PatchouliUtils.ingredientVariable(recipe.getIngredient());
            case "output" -> IVariable.from(recipe.getResult());
            case "explosionTooltip" ->
                    IVariable.from(new TranslatableComponent("integration.crockpot.book.explosion_crafting.explosion"));
            case "isOnlyBlock" -> IVariable.wrap(recipe.isOnlyBlock());
            case "onlyBlockTooltip" ->
                    IVariable.from(new TranslatableComponent("integration.crockpot.book.explosion_crafting.only_block"));
            case "rate" -> IVariable.wrap(StringUtils.format(1.0F - recipe.getLossRate(), "0.##%"));
            case "title" -> IVariable.from(recipe.getResult().getHoverName());
            default -> null;
        };
    }
}
