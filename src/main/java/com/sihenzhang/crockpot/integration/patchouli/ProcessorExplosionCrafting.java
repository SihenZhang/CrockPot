package com.sihenzhang.crockpot.integration.patchouli;

import com.google.common.base.Preconditions;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.recipe.explosion.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.util.MathUtils;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.registries.ForgeRegistries;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class ProcessorExplosionCrafting implements IComponentProcessor {
    private ExplosionCraftingRecipe recipe;

    @Override
    public void setup(IVariableProvider variables) {
        String input = variables.get("input").asString();
        Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(input));
        Preconditions.checkArgument(item != null, "input cannot be null");
        if (item instanceof BlockItem) {
            recipe = CrockPot.EXPLOSION_CRAFTING_RECIPE_MANAGER.match(((BlockItem) item).getBlock().defaultBlockState());
        } else {
            recipe = CrockPot.EXPLOSION_CRAFTING_RECIPE_MANAGER.match(item.getDefaultInstance());
        }
    }

    @Override
    public IVariable process(String key) {
        switch (key) {
            case "input":
                return PatchouliUtils.ingredientVariable(recipe.getInput());
            case "output":
                return IVariable.from(recipe.getOutput().getDefaultInstance());
            case "explosionTooltip":
                return IVariable.from(new TranslationTextComponent("integration.crockpot.book.explosion_crafting.explosion"));
            case "isOnlyBlock":
                return IVariable.wrap(recipe.isOnlyBlock());
            case "onlyBlockTooltip":
                return IVariable.from(new TranslationTextComponent("integration.crockpot.book.explosion_crafting.only_block"));
            case "rate":
                return IVariable.wrap(MathUtils.format(1.0F - recipe.getLossRate(), "0.##%"));
            case "title":
                return IVariable.from(recipe.getOutput().getDefaultInstance().getHoverName());
            default:
                return null;
        }
    }
}
