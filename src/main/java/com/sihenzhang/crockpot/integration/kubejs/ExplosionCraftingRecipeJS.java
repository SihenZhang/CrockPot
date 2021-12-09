package com.sihenzhang.crockpot.integration.kubejs;

import dev.latvian.kubejs.util.ListJS;
import net.minecraft.util.math.MathHelper;

public class ExplosionCraftingRecipeJS extends AbstractCrockPotRecipeJS {
    @Override
    public void create(ListJS args) {
        outputItems.add(this.parseResultItem(args.get(0)));
        inputItems.add(this.parseIngredientItem(args.get(1)));
        if (args.size() >= 3) {
            this.lossRate(((Number) args.get(2)).floatValue());
        }
        if (args.size() >= 4) {
            this.onlyBlock((Boolean) args.get(3));
        }
    }

    @Override
    public void deserialize() {
        outputItems.add(this.parseResultItem(json.get("result")));
        inputItems.add(this.parseIngredientItem(json.get("ingredient")));
    }

    @Override
    public void serialize() {
        if (serializeOutputs) {
            json.add("result", outputItems.get(0).toResultJson());
        }
        if (serializeInputs) {
            json.add("ingredient", inputItems.get(0).toJson());
        }
    }

    public ExplosionCraftingRecipeJS lossRate(float lossRate) {
        json.addProperty("lossrate", MathHelper.clamp(lossRate, 0.0F, 1.0F));
        return this;
    }

    public ExplosionCraftingRecipeJS onlyBlock(boolean onlyBlock) {
        json.addProperty("onlyblock", onlyBlock);
        return this;
    }

    public ExplosionCraftingRecipeJS onlyBlock() {
        return this.onlyBlock(true);
    }
}
