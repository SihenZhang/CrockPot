package com.sihenzhang.crockpot.recipe.requirements;

import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.sihenzhang.crockpot.recipe.RecipeInput;
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
        return recipeInput.stacks.stream().filter(stack -> ingredient.test(stack)).count() <= quantity;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        nbt.putString(RequirementConstants.TYPE, RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN.name().toLowerCase());
        try {
            nbt.put(RequirementConstants.INGREDIENT, JsonToNBT.getTagFromJson(ingredient.serialize().toString()));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
        nbt.putInt(RequirementConstants.QUANTITY, quantity);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        if (!RequirementType.MUST_CONTAIN_INGREDIENT_LESS_THAN.name().equals(nbt.getString(RequirementConstants.TYPE).toUpperCase())) {
            throw new IllegalArgumentException(RequirementConstants.REQUIREMENT_TYPE_NOT_MATCH);
        }
        JsonReader reader = new JsonReader(new StringReader(Objects.requireNonNull(nbt.get(RequirementConstants.INGREDIENT)).toString()));
        reader.setLenient(true);
        this.ingredient = Ingredient.deserialize(new JsonParser().parse(reader));
        this.quantity = nbt.getInt(RequirementConstants.QUANTITY);
    }
}
