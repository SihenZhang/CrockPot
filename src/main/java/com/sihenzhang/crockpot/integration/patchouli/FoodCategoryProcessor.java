package com.sihenzhang.crockpot.integration.patchouli;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.EnumUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

public class FoodCategoryProcessor implements IComponentProcessor {
    private String categoryName;
    private float categoryValue;
    private Item[] items;

    @Override
    public void setup(IVariableProvider variables) {
        JsonObject categoryObj = variables.get("category").unwrap().getAsJsonObject();
        this.categoryName = categoryObj.get("name").getAsString();
        this.categoryValue = categoryObj.get("value").getAsFloat();
        FoodCategory category = EnumUtils.getEnum(FoodCategory.class, this.categoryName.toUpperCase());
        this.items = CrockPot.FOOD_CATEGORY_MANAGER.getMatchingItems(category, this.categoryValue).toArray(new Item[0]);
    }

    @Override
    public IVariable process(String key) {
        if (key.startsWith("item")) {
            int index = Integer.parseInt(key.substring(4)) - 1;
            ItemStack stack = index < 0 || index >= items.length ? ItemStack.EMPTY : new ItemStack(items[index]);
            return IVariable.from(stack);
        } else if ("cname".equals(key)) {
            return IVariable.wrap(I18n.format("item." + CrockPot.MOD_ID + ".food_category_" + categoryName.toLowerCase()));
        } else if ("cvalue".equals(key)) {
            return IVariable.wrap(categoryValue);
        } else if ("title".equals(key)) {
            return IVariable.wrap(I18n.format("item." + CrockPot.MOD_ID + ".food_category_" + categoryName.toLowerCase()) + " x " + categoryValue);
        }
        return null;
    }
}
