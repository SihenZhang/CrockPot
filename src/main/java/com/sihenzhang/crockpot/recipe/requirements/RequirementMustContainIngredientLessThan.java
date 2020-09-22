package com.sihenzhang.crockpot.recipe.requirements;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sihenzhang.crockpot.recipe.RecipeInput;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.JsonToNBT;

import java.io.StringReader;
import java.util.Objects;

public class RequirementMustContainIngredientLessThan extends Requirement {
    Ingredient ingredient;
    int quantity;

    public RequirementMustContainIngredientLessThan(Ingredient ingredient, int quantity) {
        this.ingredient = ingredient;
        this.quantity = quantity;
    }

    public RequirementMustContainIngredientLessThan(CompoundNBT nbt) {
        deserializeNBT(nbt);
    }

    @Override
    public boolean test(RecipeInput recipeInput) {
        int q = 0;
        for (ItemStack stack : recipeInput.stacks) {
            if (ingredient.test(stack)) {
                q += stack.getCount();
            }
        }
        return q <= quantity;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString("type", "must_contain_ingredient_less_than");
        try {
            nbt.put("ingredient", JsonToNBT.getTagFromJson(ingredient.serialize().toString()));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        nbt.putInt("quantity", quantity);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!"must_contain_ingredient_less_than".equals(nbt.getString("type"))) {
            throw new IllegalArgumentException("requirement type doesn't match");
        }
        try {
            JsonReader reader = new JsonReader(new StringReader(Objects.requireNonNull(nbt.get("ingredient")).toString()));
            reader.setLenient(true);
            this.ingredient = Ingredient.deserialize(new JsonParser().parse(reader));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.quantity = nbt.getInt("quantity");
    }
}
