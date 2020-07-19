package com.sihenzhang.crockpot.recipe.requirements;

import com.sihenzhang.crockpot.recipe.RecipeInput;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Objects;

public class RequirementMustContainItem extends Requirement {
    Item item;

    public RequirementMustContainItem(Item item) {
        this.item = item;
    }

    public RequirementMustContainItem(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(RecipeInput recipeInput) {
        return recipeInput.stacks.stream().anyMatch(s -> s.getItem() == this.item);
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", "must_contain_item");
        nbt.putString("item", Objects.requireNonNull(item.getRegistryName()).toString());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!nbt.getString("type").equals("must_contain_item"))
            throw new IllegalArgumentException("requirement type doesn't match");
        this.item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("item")));
    }
}
