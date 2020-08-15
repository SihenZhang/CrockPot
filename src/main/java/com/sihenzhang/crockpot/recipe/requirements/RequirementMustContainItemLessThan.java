package com.sihenzhang.crockpot.recipe.requirements;

import com.sihenzhang.crockpot.recipe.RecipeInput;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RequirementMustContainItemLessThan extends Requirement {
    List<Item> items;
    int quantity;

    public RequirementMustContainItemLessThan(List<Item> items, int quantity) {
        this.items = items;
        this.quantity = quantity;
    }

    public RequirementMustContainItemLessThan(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(RecipeInput recipeInput) {
        int q = 0;
        for (ItemStack stack : recipeInput.stacks) {
            if (items.contains(stack.getItem())) {
                q += stack.getCount();
            }
        }
        return q <= quantity;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", "must_contain_item_less_than");
        ListNBT list = new ListNBT();
        items.stream().map(i -> Objects.requireNonNull(i.getRegistryName()).toString()).forEach(r -> list.add(StringNBT.valueOf(r)));
        nbt.put("items", list);
        nbt.putInt("quantity", quantity);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!nbt.getString("type").equals("must_contain_item_less_than"))
            throw new IllegalArgumentException("requirement type doesn't match");
        ListNBT list = (ListNBT) nbt.get("items");
        assert list != null;
        items = new ArrayList<>(list.size());
        list.stream().map(r -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(r.getString()))).forEach(this.items::add);
        this.quantity = nbt.getInt("quantity");
    }
}
