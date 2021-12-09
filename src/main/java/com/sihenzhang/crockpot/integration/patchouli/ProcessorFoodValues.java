package com.sihenzhang.crockpot.integration.patchouli;

import com.sihenzhang.crockpot.CrockPot;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.EnumUtils;
import vazkii.patchouli.api.IComponentProcessor;
import vazkii.patchouli.api.IVariable;
import vazkii.patchouli.api.IVariableProvider;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProcessorFoodValues implements IComponentProcessor {
    private String categoryName;
    private Set<Item> items;
    private List<IVariable> pagedItems;

    @Override
    public void setup(IVariableProvider variables) {
        categoryName = variables.get("category").asString();
        FoodCategory category = EnumUtils.getEnum(FoodCategory.class, this.categoryName.toUpperCase());
        items = FoodValuesDefinition.getMatchedItems(category, Minecraft.getInstance().level.getRecipeManager());
        pagedItems = PatchouliUtils.pagedItemVariables(items.stream().map(Item::getDefaultInstance).collect(Collectors.toList()), 42);
    }

    @Override
    public IVariable process(String key) {
        if (key.startsWith("item")) {
            int index = Integer.parseInt(key.substring(4)) - 1;
            if (index < 0 || index >= Math.min(items.size(), 42)) {
                return IVariable.from(ItemStack.EMPTY);
            }
            return pagedItems.get(index);
        } else if ("title".equals(key)) {
            return IVariable.wrap(I18n.get("item." + CrockPot.MOD_ID + ".food_category_" + categoryName.toLowerCase()));
        }
        return null;
    }
}
